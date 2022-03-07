package com.example.quit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import Adapter.GoalAdapter;
import Adapter.PostAdapter;
import Model.Comment;
import Model.Goal;
import Model.Post;

public class GoalsFragment extends Fragment {
    private ImageView addGoal, savedGoals;
    private Activity activity;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private GoalAdapter goalAdapter;
    private List<Goal> goalLists;
    private TextView makeGoalMessage;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.goals_frag,container, false);

        addGoal = view.findViewById(R.id.addGoal);
        savedGoals = view.findViewById(R.id.savedGoals);
        makeGoalMessage = view.findViewById(R.id.makeGoalMessage);

        addGoal.setClickable(true);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, CreateGoalActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        savedGoals.setClickable(true);
        savedGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, MySavedGoalsActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        Toolbar toolbar = view.findViewById(R.id.goalsToolbar);
        activity = getActivity();
        if(activity!=null){
            ((AppCompatActivity)activity).setSupportActionBar(toolbar);
            ((AppCompatActivity)activity).getSupportActionBar().setTitle("Goals");
        }

        recyclerView = view.findViewById(R.id.recycler_view_goals);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.getItemAnimator().setChangeDuration(0);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider)) ;
        recyclerView.addItemDecoration(dividerItemDecoration);

        goalLists= new ArrayList<>();
        goalAdapter = new GoalAdapter(getContext(), goalLists);
        goalAdapter.setHasStableIds(true);
        recyclerView.setAdapter(goalAdapter);

        readGoals();

        return view;
    }

    private void readGoals(){
        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            goalLists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Goal goal = document.toObject(Goal.class);
                                if(goal.getSaved()!=true){
                                    goalLists.add(goal);
                                }
                                Log.d("findComments", "In Loop");

                            }

                            //Provides message in case of no goals
                            if(goalLists.size() < 1){
                                makeGoalMessage.setVisibility(View.VISIBLE);
                            }else{
                                makeGoalMessage.setVisibility(View.INVISIBLE);
                            }

                            goalAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        readGoals();
    }
}
