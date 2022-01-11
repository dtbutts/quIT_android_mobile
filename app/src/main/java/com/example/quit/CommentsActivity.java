package com.example.quit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.CommentAdapter;
import Adapter.PostAdapter;
import Model.Comment;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView rVPost;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    EditText newComment;
    TextView submitPost;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.commentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view_comments);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)) ;
        recyclerView.addItemDecoration(dividerItemDecoration);


        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        commentAdapter.setHasStableIds(true);
        recyclerView.setAdapter(commentAdapter);

//        rVPost = findViewById(R.id.recycler_view);
//        rVPost.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManagerPost = new LinearLayoutManager(SocialFragment.getContext());
//        rVPost.setLayoutManager(linearLayoutManager);
//        commentList = new ArrayList<>();
//        commentAdapter = new CommentAdapter(this, commentList);
//        recyclerView.setAdapter(commentAdapter);

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
                if(newComment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this,
                            "You must have at least 1 character in your comment",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    addNewComment();
//                    try {
//                        adapter.notifyDataSetChanged();
//                    }catch (NullPointerException nullPointerException){
//
//                    }
                    finish();
//                    Intent i=new Intent(view.getContext(), MainActivity.class);
////                    i.putExtra("back", "toSocial");
//                    startActivity(i);
//                    MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
//                    MainActivity.bottomNav.setVisibility(View.GONE);
                    //adapter.notifyDataSetChanged();
                }
            }
        });
        findComments();
    }

    private void addNewComment() {
        HashMap<String, Object> comments = new HashMap<>();
        comments.put("comment", newComment.getText().toString());
        comments.put("publisher", firebaseUser.getUid());
        Long Timestamp = System.currentTimeMillis();
        comments.put("timestamp", Timestamp);
        comments.put("postuid", postid);
        DocumentReference key = db.collection("Posts").document();
        comments.put("commentuid", key.getId());
        db.collection("Comments")
                .document(postid)
                .collection("Sub")
                .document(key.getId())
                .set(comments);
                //.add(comments);
        newComment.setText("");
    }
    private void findComments(){
        Log.d("findComments", "Gets Ran");
        db.collection("Comments")
                .document(postid)
                .collection("Sub")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Comment comment = document.toObject(Comment.class);
                                commentList.add(comment);
                                Log.d("findComments", "In Loop");
//                                if(document.getId().equals(postuid)){

                            }
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
