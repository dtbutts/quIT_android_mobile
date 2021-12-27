package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quit.CommentsActivity;
import com.example.quit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.thePost.setText(post.getThePost());

        publisherInfo(holder.username, holder.publisher, post.getPublisher());
        findingLikes(post.getPostuid(),holder.likeImage);
        findingSaves(post.getPostuid(), holder.saveImage);
        numberOfLikes(holder.likes, post.getPostuid());
        numberOfComments(holder.comments, post.getPostuid());

        holder.likeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(holder.likeImage.getTag().equals("like")){
                    Map<String, Object> likes = new HashMap<>();
                    likes.put(firebaseUser.getUid(),true);
                    likes.put("postid", post.getPostuid());
                    db.collection("Likes")
                            .document(post.getPostuid())
                            .collection("Sub")
                            .add(likes)
                            //.set(likes)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
//                                    findingLikes(post.getPostuid(),holder.likeImage);
//                                    numberOfLikes(holder.likes, post.getPostuid());
                                    notifyItemChanged(holder.getAdapterPosition());
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    return;
                                }
                            });
                }
                else{
                    db.collection("Likes")
                            .document(post.getPostuid())
                            .collection("Sub")
                            .whereEqualTo("postid", post.getPostuid())
                            .whereEqualTo(firebaseUser.getUid(),true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        //should only be a for loop of size 1
                                        for (DocumentSnapshot ds: task.getResult()){
                                            ds.getReference().delete();

                                        }
                                        //notifyDataSetChanged();
                                        notifyItemChanged(holder.getAdapterPosition());
                                    }
                                }

                            });
                }
            }
        }
        );

        holder.saveImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(holder.saveImage.getTag().equals("save")){
                    Map<String, Object> saves = new HashMap<>();
                    //saves.put(firebaseUser.getUid(),true);
                    saves.put("postuid", post.getPostuid());
                    saves.put("publisher",post.getPublisher());
                    saves.put("thePost", post.getThePost());
                    saves.put("title", post.getTitle());
                    Long Timestamp = System.currentTimeMillis();
                    saves.put("timestamp", Timestamp);
                    db.collection("Saves")
                            .document(firebaseUser.getUid())
                            .collection("Sub")
                            .add(saves)
                            //.set(likes)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
//                                   findingLikes(post.getPostuid(),holder.likeImage);
//                                   numberOfLikes(holder.likes, post.getPostuid());
                                    notifyItemChanged(holder.getAdapterPosition());
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    return;
                                }
                            });
                }
                else{
                    db.collection("Saves")
                            .document(firebaseUser.getUid())
                            .collection("Sub")
                            .whereEqualTo("postuid", post.getPostuid())
                            //.whereEqualTo(firebaseUser.getUid(),true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        //should only be a for loop of size 1
                                        for (DocumentSnapshot ds: task.getResult()){
                                            ds.getReference().delete();

                                        }
                                        //notifyDataSetChanged();
                                        notifyItemChanged(holder.getAdapterPosition());
                                    }
                                }

                            });
                }
            }
        }
        );

        holder.commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", post.getPostuid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);
                //notifyDataSetChanged();

                //notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", post.getPostuid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);
                //notifyDataSetChanged();
                //notifyItemChanged(holder.getAdapterPosition());
            }
        });
        //notifyDataSetChanged();
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

    private void numberOfComments(final TextView comments, String postuid){

        db.collection("Comments")
                .document(postuid)
                .collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(document.getId().equals(postuid)){
                                    count++;
                            }
                            comments.setText("View all "+ count+ " comments");
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void findingLikes(String postuid, final ImageView imageView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Likes")
                .document(postuid)
                .collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            boolean isLiked= false;
                            for (DocumentSnapshot ds: task.getResult()){
                                if(ds.get(firebaseUser.getUid())!=null){
                                    isLiked=true;
                                    imageView.setImageResource(R.drawable.ic_liked);
                                    imageView.setTag("liked");
                                }

                            }
                            if(!isLiked){
                                imageView.setImageResource(R.drawable.ic_like);
                                //Log.d("Set TAG in findingLikes() to like", "like");
                                imageView.setTag("like");
                            }
                        }
                    }
                });
    }

    private void findingSaves(String postuid, final ImageView saveView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Saves")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            boolean isSaved= false;
                            for (DocumentSnapshot ds: task.getResult()){
                                if(ds.get("postuid").equals(postuid)){
                                    isSaved=true;
                                    saveView.setImageResource(R.drawable.ic_saved);
                                    saveView.setTag("liked");
                                }

                            }
                            if(!isSaved){
                                saveView.setImageResource(R.drawable.ic_save);
                                //Log.d("Set TAG in findingLikes() to like", "like");
                                saveView.setTag("save");
                            }
                        }
                    }
                });
    }

    private void numberOfLikes(final TextView likes, String postuid){

        db.collection("Likes")
                .document(postuid)
                .collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    //if(document.getId().equals(postuid)){
                                        count++;
                                    //}
                            }
                            likes.setText(count+" likes");

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
