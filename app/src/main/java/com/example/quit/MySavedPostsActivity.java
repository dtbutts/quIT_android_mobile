package com.example.quit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.quit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.CommentAdapter;
import Adapter.MyPostAdapter;
import Adapter.PostAdapter;
import Adapter.SavedPostAdapter;
import Model.Post;

public class MySavedPostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedPostAdapter savedPostAdapter;
    private List<Post> postLists;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saved_posts);

        Toolbar toolbar = findViewById(R.id.mySavedPostsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view_my_saved_posts);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        savedPostAdapter = new SavedPostAdapter(this, postLists);
        recyclerView.setAdapter(savedPostAdapter);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findSavedPosts();
    }

    private void findSavedPosts(){
        db.collection("Saves")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postLists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Post post = document.toObject(Post.class);
                                postLists.add(post);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            savedPostAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
