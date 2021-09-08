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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanchezemir.appcobranza.R;
import com.sanchezemir.appcobranza.models.User;
import com.sanchezemir.appcobranza.providers.AuthProviders;
import com.sanchezemir.appcobranza.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
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

        setContentView(R.layout.activity_complete_profile);

        mTextInputUsername = findViewById(R.id.tviUsuarioComplete);
        mButtonRegistro = findViewById(R.id.btnConfirmarComplete);

        mAuthProviders = new AuthProviders();
        mUserProvider = new UserProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonRegistro.setOnClickListener((view) -> {
            registro();
        });

    }

    private void registro() {
        String username = mTextInputUsername.getText().toString();

        if (!username.isEmpty()) {
            updateUser(username);
        } else {
            Toast.makeText(this, "Te falta insertar algunos campos", Toast.LENGTH_LONG).show();
        }

    }

    private void updateUser(final String username) {

        String id = mAuthProviders.getUid();
        User user = new User();
        user.setUsername(username);
        user.setId(id);
        mDialog.show();
        mUserProvider.update(user).addOnCompleteListener((task) -> {
            mDialog.dismiss();
            if (task.isSuccessful()) {
                Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}