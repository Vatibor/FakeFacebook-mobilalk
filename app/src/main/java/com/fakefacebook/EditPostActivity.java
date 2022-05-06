package com.fakefacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditPostActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private Button updateButton, cancelButton, deleteButton;
    private TextInputLayout textFieldLay, imageUrlFieldLay;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String postID;

    private PostModel postModel;

//    private LocalDateTime dateTime;
//    private DateTimeFormatter myFormatDate;
    // TODO user name get to the post
//    private FirebaseUser user;

    private String formattedDateSource;
    private String userEmailSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        firebaseDatabase = FirebaseDatabase.getInstance();

        textFieldLay = findViewById(R.id.textFieldNP);
        imageUrlFieldLay = findViewById(R.id.imageUrlFieldNP);
        updateButton = findViewById(R.id.updatePostButton);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deletePostButton);
        loadingPB = findViewById(R.id.idPBLoading);
        postModel = getIntent().getParcelableExtra("post");
        if (postModel != null) {
            postID = postModel.getPostID();
            Objects.requireNonNull(textFieldLay.getEditText()).setText(postModel.getTextField());
            Objects.requireNonNull(imageUrlFieldLay.getEditText()).setText(postModel.getImageUrlField());
            formattedDateSource = postModel.getDateTime();
            userEmailSource = postModel.getUserName();
        }

        databaseReference = firebaseDatabase.getReference("Posts").child(postID);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);

                String textField = Objects.requireNonNull(textFieldLay.getEditText()).getText().toString();
                String imageUrlField = Objects.requireNonNull(imageUrlFieldLay.getEditText()).getText().toString();
                String formattedDate = formattedDateSource;
                String userEmail = userEmailSource;

                Map<String, Object> map = new HashMap<>();
                map.put("userName", userEmail);
                map.put("textField", textField);
                map.put("imageUrlField", imageUrlField);
                map.put("dateTime", formattedDate);
                map.put("postID", postID);

                // addValueEventListener
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditPostActivity.this, "Post is updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditPostActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditPostActivity.this, "Something went wrong... :( " + error.toException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPostActivity.this, MainActivity.class));
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });
    }

    private void deletePost() {
        databaseReference.removeValue();
        Toast.makeText(this, "Post deleted...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditPostActivity.this, MainActivity.class));
    }
}