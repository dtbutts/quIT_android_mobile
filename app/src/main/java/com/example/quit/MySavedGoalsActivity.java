package com.example.quit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.CommentAdapter;
import Adapter.SavedGoalsAdapter;
import Model.Comment;
import Model.Goal;

public class MySavedGoalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedGoalsAdapter savedGoalsAdapter;
    private List<Goal> goalList;
    private TextView makeGoalMessage;

    EditText newComment;
    TextView submitPost;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saved_goals);

        Toolbar toolbar = findViewById(R.id.savedGoalsToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("My Saved Goals");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        makeGoalMessage = findViewById(R.id.makeGoalMessage);

        recyclerView = findViewById(R.id.recycler_view_my_saved_goals);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)) ;
        recyclerView.addItemDecoration(dividerItemDecoration);


        goalList = new ArrayList<>();
        savedGoalsAdapter = new SavedGoalsAdapter(this, goalList);
        //goalAdapter.setHasStableIds(true);
        recyclerView.setAdapter(savedGoalsAdapter);

//        rVPost = findViewById(R.id.recycler_view);
//        rVPost.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManagerPost = new LinearLayoutManager(SocialFragment.getContext());
//        rVPost.setLayoutManager(linearLayoutManager);
//        commentList = new ArrayList<>();
//        commentAdapter = new CommentAdapter(this, commentList);
//        recyclerView.setAdapter(commentAdapter);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        findSavedGoals();
    }

    private void findSavedGoals(){
        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .whereEqualTo("saved", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            goalList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Goal goal = document.toObject(Goal.class);
                                goalList.add(goal);
//                                if(document.getId().equals(postuid)){

                            }

                            //test if there are any saved goals here
                            if(goalList.size() < 1){    //if there are no goals
                                makeGoalMessage.setVisibility(View.VISIBLE);

                            }else{                      //else don't show message
                                makeGoalMessage.setVisibility(View.INVISIBLE);
                            }


                            savedGoalsAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
