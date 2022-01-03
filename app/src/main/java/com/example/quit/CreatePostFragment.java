package com.example.quit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Model.User;

public class CreatePostFragment extends Fragment {
    EditText title, thePost;
    ImageView profileImage;
    TextView username;
    Button cancel, post;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    Activity activity;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.create_post,container, false);
        title = view.findViewById(R.id.titleOfPost);
        thePost = view.findViewById(R.id.thePost);
        cancel = view.findViewById(R.id.cancelPost);
        post = view.findViewById(R.id.submitPost);
        username = view.findViewById(R.id.usernameCreate);
        profileImage = view.findViewById(R.id.profile_image);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        publisherInfo(username);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if(activity!=null){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
                }
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
            }
        });
        Log.i("thePost", "OG");
        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("thePost", "First");
                String Title = title.getText().toString();
                if(Title==null){
                    //makeToast (required)
                    //return;
                    Toast.makeText(v.getContext(),
                            "You must select a title",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("thePost", "Second");
                String ThePost = thePost.getText().toString();
                if(ThePost==null){
                    //makeToast (required)
                    //return;
                    Toast.makeText(v.getContext(),
                            "You must create a Post",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("thePost", "Second");
                Toast.makeText(v.getContext(),
                        "We got here",
                        Toast.LENGTH_LONG).show();
                submitPostToDatabase(Title, ThePost, view);

                activity = getActivity();
                if(activity!=null){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
                }
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
            }
        });

        return view;
    }
    private void submitPostToDatabase(String Title, String ThePost, View v) {
        Map<String, Object> post = new HashMap<>();
        post.put("title", Title);
        post.put("thePost", ThePost);
        //DocumentReference reference;
        //reference = db.collection("userAccount").

        Log.d("SSSSSSSSSSSSSSSSSS", firebaseUser.getUid());
        post.put("publisher", firebaseUser.getUid());
        //DocumentReference key = db.collection("userAccount").document();
        Log.i("thePost", "Third");
        DocumentReference key = db.collection("Posts").document();
        post.put("postuid", key.getId());
        Long Timestamp = System.currentTimeMillis();
        post.put("timestamp", Timestamp);
        String Date = new SimpleDateFormat("MMMM dd, yyyy").format(Calendar.getInstance().getTime());
        post.put("date", Date);

        db.collection("Posts")
                .document(key.getId())
                .set(post)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(v.getContext(),
                                "Post Successful!",
                                Toast.LENGTH_LONG).show();
                        Log.i("thePost", "Fourth");
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(),
                                "Post failed!",
                                Toast.LENGTH_LONG).show();
                        Log.i("thePost", "Fifth");
                        return;
                    }
        });

    }

    private void publisherInfo(TextView username){
        db.collection("userAccount")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                username.setText(user.getUsername());
                                //publisher.setText(user.getUsername());
                                Glide.with(getContext()).load(user.getImageUri()).into(profileImage);
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            // Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}