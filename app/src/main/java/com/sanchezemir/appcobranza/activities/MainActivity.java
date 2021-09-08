package com.sanchezemir.appcobranza.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanchezemir.appcobranza.R;
import com.sanchezemir.appcobranza.models.User;
import com.sanchezemir.appcobranza.providers.AuthProviders;
import com.sanchezemir.appcobranza.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    TextView mTextViewRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    Button mButtonTerminos;
    SignInButton mSignInButton;
    //firebase
    AuthProviders mAuthProviders;
    //Auth google
    private GoogleSignInClient mGoogleSignInClient;
    //
    private final int REQUEST_CODE_GOOGLE = 1;
    //Firestore
    UserProvider mUserProvider;
    //dialog
    AlertDialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRegister = findViewById(R.id.txvRegistro);
        mTextInputEmail = findViewById(R.id.loginEmail);
        mTextInputPassword = findViewById(R.id.loginPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonTerminos = findViewById(R.id.btnTerminos);
        mSignInButton = findViewById(R.id.btnLoginGoogle);

        mAuthProviders = new AuthProviders();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mUserProvider = new UserProvider();

        //evento para inicio con google
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    //ciclo de vida de android
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthProviders.getUserSession() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //metodo encargado de disparar con google
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("ERROR", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
                Toast.makeText(this, "No se pudo iniciar sesion con google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mDialog.show();
        mAuthProviders.googleLogin(acct).addOnCompleteListener(this, (task) -> {

            if (task.isSuccessful()) {
                //verificando si el usuario existe
                String id = mAuthProviders.getUid();
                checkUserExist(id);
            } else {
                mDialog.dismiss();
                // If sign in fails, display a message to the user.
                Log.w("ERROR", "signInWithCredential:failure", task.getException());
                Toast.makeText(MainActivity.this, "No se pudo iniciar sesion con google", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserExist(final String id) {
        mUserProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mDialog.dismiss();
                    //llamando a otra pantalla
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    String email = mAuthProviders.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUserProvider.create(user).addOnCompleteListener((task) -> {
                        mDialog.dismiss();
                        if (task.isSuccessful()) {
                            //llamando a otra pantalla
                            Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "No se pudo almacenar los datos del usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void login() {
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        mDialog.show();
        //llamar al auth
        mAuthProviders.login(email, password).addOnCompleteListener((task) -> {
            mDialog.dismiss();
            if (task.isSuccessful()) {
                //llamando a otra pantalla
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "El email y la contraseña que ingresaste no son correctas", Toast.LENGTH_LONG).show();
            }
        });

        Log.d("CAMPO", "email: " + email);
        Log.d("CAMPO", "password: " + password);
    }

}