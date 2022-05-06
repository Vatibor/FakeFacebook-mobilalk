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

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private Button signupButton;
    private TextInputLayout textFieldEmailReg, textFieldPasswordReg,
            textFieldPasswordAgainReg, textFieldFullnameReg;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textFieldEmailReg = findViewById(R.id.textFieldEmailReg);
        textFieldPasswordReg = findViewById(R.id.textFieldPasswordReg);
        textFieldPasswordAgainReg = findViewById(R.id.textFieldPasswordAgainReg);
        textFieldFullnameReg = findViewById(R.id.textFieldFullnameReg);
        loadingPB = findViewById(R.id.idPBLoading);

        mAuth = FirebaseAuth.getInstance();

        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Objects.requireNonNull(textFieldEmailReg.getEditText()).getText().toString();
                String password = Objects.requireNonNull(textFieldPasswordReg.getEditText()).getText().toString();
                String passwordAgain = Objects.requireNonNull(textFieldPasswordAgainReg.getEditText()).getText().toString();
                String fullName = Objects.requireNonNull(textFieldFullnameReg.getEditText()).getText().toString();

                boolean isValid = true;
                if(email.isEmpty()){
                    textFieldEmailReg.setError("Email is required!");
                    isValid = false;
                } else {
                    textFieldEmailReg.setErrorEnabled(false);
                }
                if(password.isEmpty()){
                    textFieldPasswordReg.setError("Password is required!");
                    isValid = false;
                } else {
                    textFieldPasswordReg.setErrorEnabled(false);
                }
                if(passwordAgain.isEmpty()){
                    textFieldPasswordAgainReg.setError("Password again field is required!");
                    isValid = false;
                } else if(!password.equals(passwordAgain)){
                    textFieldPasswordAgainReg.setError("Password and confirm password field don't equals!");
                    isValid = false;
                } else {
                    textFieldPasswordAgainReg.setErrorEnabled(false);
                }

                if(fullName.isEmpty()){
                    textFieldFullnameReg.setError("Full name field is required!");
                    isValid = false;
                } else {
                    textFieldFullnameReg.setErrorEnabled(false);
                }

                if(isValid){
                    loadingPB.setVisibility(View.VISIBLE);
                    Log.i(LOG_TAG, email + ", " + password + ", " + passwordAgain + ", " + fullName);

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loadingPB.setVisibility(View.GONE);
                                Log.d(LOG_TAG, "User created successfully.");
                                Toast.makeText(RegistrationActivity.this, "User created successfully! :)", Toast.LENGTH_LONG).show();
                                goLoginPage();
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Log.d(LOG_TAG, "User wasn't created! :(");
                                Toast.makeText(RegistrationActivity.this, "User wasn't created! :(" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void goLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}