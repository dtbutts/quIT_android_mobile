package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder>{
    public Context mContext;
    public List<Post> mPost;
    private FirebaseFirestore db;

    private FirebaseUser firebaseUser;
    public MyPostAdapter(Context mContext, List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public MyPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_post_item, parent, false);

        return new MyPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);
        holder.title.setVisibility(View.VISIBLE);
        holder.title.setText(post.getTitle());
        holder.thePost.setText(post.getThePost());
        holder.date.setText(post.getDate());

        if(post.getImageUri()!= "" && post.getImageUri() != null){
            holder.uploadImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(post.getImageUri()).into(holder.uploadImage);
        }

        publisherInfo(holder.username, holder.profileImage, post.getPublisher());
        findingLikes(post.getPostuid(),holder.likeImage);
        numberOfLikes(holder.likes, post.getPostuid());
        numberOfComments(holder.comments, post.getPostuid());

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
                                notifyDataSetChanged();

//                                updateSocialFragListofPosts();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to permanently delete this post?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MyPostCommentsActivity.class);
                intent.putExtra("postid", post.getPostuid());
                mContext.startActivity(intent);
                //notifyDataSetChanged();
                //notifyItemChanged(holder.getAdapterPosition());
            }
        });



    }

//    private void updateSocialFragListofPosts() {
//        List<Post> tmp = new ArrayList<>();
//        db.collection("Posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()){
//                                tmp.add(document.toObject(Post.class));
//                            }
//                            postAdapter = new PostAdapter(new SocialFragment().getContext(), tmp);
//                            postAdapter.notifyDataSetChanged();
//                        } else {
//                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

    private void deleteReferences(String postuid) {
        db.collection("Posts")
                .document(postuid)
                .delete();

        DocumentReference likeReference = db.collection("Likes")
                .document(postuid);

        likeReference.collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        likeReference.delete();

        DocumentReference commentReference = db.collection("Comments")
                .document(postuid);

        commentReference.collection("Sub")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        commentReference.delete();
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView likeImage, removeImage, profileImage, uploadImage;

        public TextView username, thePost, likes, title, comments, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            likeImage = itemView.findViewById(R.id.myLike);
            removeImage = itemView.findViewById(R.id.myRemove);
            username = itemView.findViewById(R.id.usernameMyPost);
            thePost = itemView.findViewById(R.id.myThePostItem);
            likes = itemView.findViewById(R.id.myLikes);
            title = itemView.findViewById(R.id.titleDescriptionMyPost);
            comments = itemView.findViewById(R.id.myComments);
            profileImage = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.date);
            db = FirebaseFirestore.getInstance();
            uploadImage = itemView.findViewById(R.id.myPostImage);
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
