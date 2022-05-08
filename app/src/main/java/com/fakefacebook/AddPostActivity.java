package com.fakefacebook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private Button postButton, cancelButton;
    private TextInputLayout textFieldLay, imageUrlFieldLay;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String postID;
    private NotificationHandler mNotificationHandler;

    private LocalDateTime dateTime;
    private DateTimeFormatter myFormatDate;
    // TODO user name get to the post
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        textFieldLay = findViewById(R.id.textFieldNP);
        imageUrlFieldLay = findViewById(R.id.imageUrlFieldNP);
        postButton = findViewById(R.id.newPostButton);
        cancelButton = findViewById(R.id.cancelButton);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        mNotificationHandler = new NotificationHandler(this);

        postButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String textField = Objects.requireNonNull(textFieldLay.getEditText()).getText().toString();
                String imageUrlField = Objects.requireNonNull(imageUrlFieldLay.getEditText()).getText().toString();
                dateTime = LocalDateTime.now();
                myFormatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                boolean isValid = true;
                if (textField.isEmpty()) {
                    textFieldLay.setError("Please write something...");
                    isValid = false;
                } else {
                    textFieldLay.setErrorEnabled(false);
                }
                if (imageUrlField.isEmpty()) {
                    imageUrlFieldLay.setError("Please add this input an image url!");
                    isValid = false;
                } else {
                    imageUrlFieldLay.setErrorEnabled(false);
                }

                if (isValid) {
                    loadingPB.setVisibility(View.VISIBLE);
                    String formattedDate = dateTime.format(myFormatDate);

                    // get user email as username
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String userEmail = user.getEmail();
                    postID = userEmail.replaceAll("[^a-zA-Z0-9]", "") + formattedDate.replaceAll("[^a-zA-Z0-9]", "");
                    Log.d(LOG_TAG, "Post Created: " + postID + ", " +  userEmail + "," + formattedDate + ", " + textField + ", " + imageUrlField);

                    PostModel postModel = new PostModel(postID, userEmail, formattedDate, textField, imageUrlField);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            loadingPB.setVisibility(View.GONE);
                            Log.d(LOG_TAG, "onDataChange functions");
                            databaseReference.child(postID).setValue(postModel);
                            Toast.makeText(AddPostActivity.this, "Awesome! You created a post!", Toast.LENGTH_SHORT).show();
                            mNotificationHandler.send("New post on your feed!");
                            startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddPostActivity.this, "Something went wrong: " + error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}