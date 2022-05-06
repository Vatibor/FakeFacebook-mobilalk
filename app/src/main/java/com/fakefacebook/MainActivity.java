package com.fakefacebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PostAdapter.PostClickInterface{
    private static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView postRV;
    private ProgressBar loadingBP;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<PostModel> postModelsArrayList;
    private PostAdapter postAdapter;
    private RelativeLayout bottomsheetRL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postRV = findViewById(R.id.idPosts);
        loadingBP = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.addFABButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        postModelsArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(postModelsArrayList, this, this);
        postRV.setLayoutManager(new LinearLayoutManager(this));
        postRV.setAdapter(postAdapter);

        bottomsheetRL = findViewById(R.id.idRLBSheet);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPostActivity.class));
            }
        });

        getAllPosts();
    }

    private void getAllPosts() {
        postModelsArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingBP.setVisibility(View.GONE);
                postModelsArrayList.add(snapshot.getValue(PostModel.class));
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingBP.setVisibility(View.GONE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingBP.setVisibility(View.GONE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingBP.setVisibility(View.GONE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPostClick(int position) {
        displayBottomSheet(postModelsArrayList.get(position));
    }

    private void displayBottomSheet(PostModel postModel){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, bottomsheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        Button editBtn = layout.findViewById(R.id.editButton);
//        Button deleteBtn = layout.findViewById(R.id.deleteButton);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditPostActivity.class);
                intent.putExtra("post", postModel);
                startActivity(intent);
            }
        });

//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO delete func create
//                Log.d(LOG_TAG, "Ez " + databaseReference.child(postModel.getPostID()));
//                databaseReference.child(postModel.getPostID()).removeValue();
//
//
//                Toast.makeText(MainActivity.this, "Post deleted!", Toast.LENGTH_SHORT).show();
//
//                getAllPosts();
//            }
//        });
    }
}