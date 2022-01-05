package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quit.CommentsActivity;
import com.example.quit.MyPostCommentsActivity;
import com.example.quit.R;
import com.example.quit.SocialFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Model.Post;
import Model.User;

public class SavedPostAdapter extends RecyclerView.Adapter<SavedPostAdapter.ViewHolder>{
    public Context mContext;
    public List<Post> mPost;
    private FirebaseFirestore db;

    private FirebaseUser firebaseUser;

    public SavedPostAdapter(Context mContext, List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public SavedPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_saved_post_item, parent, false);

        return new SavedPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPostAdapter.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = mPost.get(holder.getAdapterPosition());
        holder.title.setVisibility(View.VISIBLE);
        holder.title.setText(post.getTitle());
        holder.thePost.setText(post.getThePost());
        holder.date.setText(post.getDate());

        if(post.getImageUri()== "" || post.getImageUri() == null){
            holder.uploadImage.setVisibility(View.GONE);
            //Glide.with(mContext).load(post.getImageUri()).into(holder.uploadImage);
        }
        else{
            holder.uploadImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(post.getImageUri()).into(holder.uploadImage);
        }

        publisherInfo(holder.username, holder.profileImage, post.getPublisher());


        holder.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deleteReferences(post.getPostuid());
                                mPost.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), mPost.size());
                                //notifyDataSetChanged();

//                                updateSocialFragListofPosts();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want unsave this post?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    private void deleteReferences(final String postuid) {
        Log.d("TODAY",  "postuid "+ postuid);
        db.collection("Saves")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TODAY", "postid "+document.get("postuid") +" postuid "+ postuid);
                                if(document.get("postuid").equals(postuid))
                                document.getReference().delete();
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        DocumentReference saveReference = db.collection("Saves")
                .document(firebaseUser.getUid());
        saveReference.collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() >0)
                            {
                                //do nothing, there are still saved posts
                            }
                            else{
                                saveReference.delete();
                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView removeImage, profileImage, uploadImage;

        public TextView username, thePost, title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            removeImage = itemView.findViewById(R.id.saveRemove);
            username = itemView.findViewById(R.id.usernameSaved);
            thePost = itemView.findViewById(R.id.postSaved);
            title = itemView.findViewById(R.id.titleSaved);
            profileImage = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.date);
            db = FirebaseFirestore.getInstance();
            uploadImage = itemView.findViewById(R.id.savedPostImage);

        }
    }


    private void publisherInfo(TextView username, ImageView profileImage, String user_id){
        db.collection("userAccount")
                .document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                username.setText(user.getUsername());
                                Glide.with(mContext).load(user.getImageUri()).into(profileImage);
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
