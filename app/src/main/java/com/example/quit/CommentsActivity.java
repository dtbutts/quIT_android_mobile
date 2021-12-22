package com.example.quit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import Adapter.PostAdapter;

public class CommentsActivity extends AppCompatActivity {

    EditText newComment;
    TextView submitPost;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        newComment = findViewById(R.id.newComment);
        submitPost = findViewById(R.id.submitComment);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");

        submitPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(newComment.getText().equals("")){
                    Toast.makeText(CommentsActivity.this,
                            "You must have at least 1 character in your comment",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    addNewComment();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addNewComment() {
        HashMap<String, Object> comments = new HashMap<>();
        comments.put("comment", newComment.getText().toString());
        comments.put("publisher", firebaseUser.getUid());
        db.collection("Comments")
                .document(postid)
                .collection("Sub")
                //.set(comments);
                .add(comments);
        newComment.setText("");
    }
}
