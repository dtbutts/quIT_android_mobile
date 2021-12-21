package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Model.Post;
import Model.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    public Context mContext;
    public List<Post> mPost;
    private FirebaseFirestore db;

    private FirebaseUser firebaseUser;
    public PostAdapter(Context mContext, List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);
        holder.title.setVisibility(View.VISIBLE);
        holder.title.setText(post.getTitle());
        holder.title.setVisibility(View.VISIBLE);
        holder.title.setVisibility(View.VISIBLE);
        holder.thePost.setText(post.getThePost());
        Log.d("NOTICE MEEEEEEEE", post.getPublisher());
        publisherInfo(holder.username, holder.publisher, post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView likeImage, commentImage, saveImage;
        public TextView username, thePost, likes, publisher,title, comments;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            db = FirebaseFirestore.getInstance();
            likeImage = itemView.findViewById(R.id.like);
            commentImage = itemView.findViewById(R.id.comment);
            saveImage = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.usernamePost);
            thePost = itemView.findViewById(R.id.thePostItem);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            title = itemView.findViewById(R.id.titleDescription);
            comments = itemView.findViewById(R.id.comments);

        }
    }
    private void publisherInfo(TextView username, TextView publisher, String user_id){
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
                                publisher.setText(user.getUsername());
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
