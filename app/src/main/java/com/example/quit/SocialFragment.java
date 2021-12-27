package com.example.quit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.PostAdapter;
import Model.Post;

public class SocialFragment extends Fragment {
    ImageView compose, myPosts, savedPosts;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;
    private FirebaseFirestore db;
    private Context mContext;


    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.social_frag,container, false);
        compose = view.findViewById(R.id.compose);
        savedPosts = view.findViewById(R.id.saved);
        myPosts = view.findViewById(R.id.my_posts);
        recyclerView = view.findViewById(R.id.recycler_view);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.getItemAnimator().setChangeDuration(0);

        db = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists= new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);
        compose.setClickable(true);
        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreatePostFragment()).commit();
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new CreatePostFragment()).commit();
            }
        });
        myPosts.setClickable(true);
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPostsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        savedPosts.setClickable(true);
        savedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MySavedPostsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        readPosts();
        return view;
    }
    private void readPosts(){
        db.collection("Posts")
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
                            postAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //this function is used to update the View all "" comments when a comment is submitted
    @Override
    public void onResume() {
        db.collection("Posts")
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
                            postAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //postAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
