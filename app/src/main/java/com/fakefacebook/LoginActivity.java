package com.fakefacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getName();
    private Button loginButton, registrationButton;
    private TextInputLayout email;
    private TextInputLayout password;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingPB = (ProgressBar) findViewById(R.id.idPBLoading);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        registrationButton = findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(this);

        email = findViewById(R.id.textFieldEmail);
        password = findViewById(R.id.textFieldPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                String emailStr = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                String passwordStr = Objects.requireNonNull(password.getEditText()).getText().toString();

                boolean isValid = true;
                if (emailStr.isEmpty()) {
                    email.setError("Email is required!");
                    isValid = false;
                } else {
                    email.setErrorEnabled(false);
                }
                if (passwordStr.isEmpty()) {
                    password.setError("Password is required!");
                    isValid = false;
                } else {
                    email.setErrorEnabled(false);
                }

                if (isValid) {
                    loadingPB.setVisibility(View.VISIBLE);
                    Log.i(LOG_TAG, "Bejelentkezett: " + emailStr + ", jelszó: " + passwordStr);

                    mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                                Log.d(LOG_TAG, "User logged in successfully!");
                                Toast.makeText(LoginActivity.this, "User login successfully! :)", Toast.LENGTH_LONG).show();
                                goMainPage();
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Log.d(LOG_TAG, "User logged in fail!");
                                Toast.makeText(LoginActivity.this, "User login fail! :( " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                break;
            case R.id.registrationButton:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void goMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

    }
}

