package com.sanchezemir.appcobranza.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanchezemir.appcobranza.R;
import com.sanchezemir.appcobranza.models.User;
import com.sanchezemir.appcobranza.providers.AuthProviders;
import com.sanchezemir.appcobranza.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    Button mButtonRegistro;
    //firebase
    AuthProviders mAuthProviders;
    //firestore
    UserProvider mUserProvider;
    //Alerta
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mCircleImageViewBack = findViewById(R.id.back_login);
        mTextInputUsername = findViewById(R.id.tviUsuario);
        mTextInputEmail = findViewById(R.id.tviEmail);
        mTextInputPassword = findViewById(R.id.tviContraseña);
        mTextInputConfirmPassword = findViewById(R.id.tviPwdConfirm);
        mButtonRegistro = findViewById(R.id.btnRegistrar);

        mAuthProviders = new AuthProviders();
        mUserProvider = new UserProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonRegistro.setOnClickListener((view) -> {
            registro();
        });

        mCircleImageViewBack.setOnClickListener((view) -> {
            finish();
        });

    }

    private void registro() {
        String username = mTextInputUsername.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        String cpassword = mTextInputConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !cpassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(cpassword)) {
                    if (password.length() >= 6) {
                        createUser(username, email, password);
                    } else {
                        Toast.makeText(this, "La contraseña debe ser tener 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coninciden", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Has insertado todos los campos pero correo no es valido", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Te falta insertar algunos campos", Toast.LENGTH_LONG).show();
        }

    }

    private void createUser(final String username, final String email, final String password) {
        mDialog.show();
        mAuthProviders.register(email, password).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                String id = mAuthProviders.getUid();

                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setUsername(username);

                mUserProvider.create(user).addOnCompleteListener((task2) -> {
                    mDialog.dismiss();
                    if (task2.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "El usuario se almaceno correctamente en la base de datos", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Verifica que sea una email valido
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}