package Adapter;

import android.content.Context;
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
import com.example.quit.MainActivity;
import com.example.quit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Model.Comment;
import Model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context mContext;
    private List<Comment> mComment;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);

        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment comment = mComment.get(position);

        if(comment.getPublisher().equals(firebaseUser.getUid())){
            holder.removeComment.setVisibility(View.VISIBLE);
            holder.removeComment.setClickable(true);
            holder.removeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeComment(comment.getPostuid(), comment.getCommentuid());
                    mComment.remove(holder.getAdapterPosition());
                    //notifyItemChanged(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
        else{
            holder.removeComment.setVisibility(View.GONE);
        }

        holder.comment.setText(comment.getComment());
        Log.d("comment in onbindviewholder", holder.comment.getText().toString());
        getPublisherOfCommentInfo(holder.username, holder.profileImage, comment.getPublisher());

//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("publisherid", comment.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
    }

    private void removeComment(String postuid, String commentuid) {
        db.collection("Comments")
                .document(postuid)
                .collection("Sub")
                .document(commentuid)
                .delete();

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username, comment;
        public ImageView profileImage, removeComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameComment);
            comment = itemView.findViewById(R.id.commentItem);
            profileImage = itemView.findViewById(R.id.profile_image);
            removeComment = itemView.findViewById(R.id.removeComment);
            db = FirebaseFirestore.getInstance();

        }


    }

    private void getPublisherOfCommentInfo(TextView username, ImageView profileImage, String publisherid){
        db.collection("userAccount").document(publisherid)
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
