package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quit.CommentsActivity;
import com.example.quit.R;
import com.example.quit.UpdateGoalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import Model.Comment;
import Model.Goal;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder>{

    private Context mContext;
    private List<Goal> mGoal;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public GoalAdapter(Context mContext, List<Goal> mGoal) {
        this.mContext = mContext;
        this.mGoal = mGoal;
    }

    @NonNull
    @Override
    public GoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.goal_item, parent, false);
       return new GoalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.ViewHolder holder, int position) {
        Goal goal = mGoal.get(position);

        holder.theGoal.setText(goal.getTheGoal());
        Integer current = goal.getCurrent();
        Integer total = goal.getTotalNeeded();
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        if(goal.getDeadline()!=null){
            holder.deadline.setText("Deadline: "+dateFormat.format(goal.getDeadline()));
        }
        else{
            holder.deadline.setVisibility(View.GONE);
        }
        holder.percentComplete.setText(""+current+"/"+total +" "+goal.getMeasurement());
        holder.progressBar.setProgress(current);
        holder.progressBar.setMax(total);

        holder.goalRelativeLayout.setClickable(true);
        holder.goalRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateGoalActivity.class);
                intent.putExtra("goalUid", goal.getGoalUid());
                intent.putExtra("measurement", goal.getMeasurement());
//                intent.putExtra("postid", p.getPostuid());
//                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGoal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;
        public RelativeLayout goalRelativeLayout;
        public TextView theGoal, deadline, percentComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            theGoal = itemView.findViewById(R.id.theGoal);
            deadline = itemView.findViewById(R.id.deadline);
            percentComplete = itemView.findViewById(R.id.percentComplete);
            progressBar = itemView.findViewById(R.id.progressBar1);
            db = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            goalRelativeLayout = itemView.findViewById(R.id.goalRelativeLayout);
        }
    }
}
