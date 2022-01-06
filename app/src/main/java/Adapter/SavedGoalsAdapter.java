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

public class SavedGoalsAdapter extends RecyclerView.Adapter<SavedGoalsAdapter.ViewHolder>{

    private Context mContext;
    private List<Goal> mGoal;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public SavedGoalsAdapter(Context mContext, List<Goal> mGoal) {
        this.mContext = mContext;
        this.mGoal = mGoal;
    }

    @NonNull
    @Override
    public SavedGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.saved_goal_item, parent, false);
        return new SavedGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedGoalsAdapter.ViewHolder holder, int position) {
        Goal goal = mGoal.get(position);

        holder.theGoal.setText(goal.getTheGoal());
        Integer current = goal.getCurrent();
        Integer total = goal.getTotalNeeded();
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        if(goal.getDeadline()!=null){
            holder.complete.setText("Completed on "+dateFormat.format(goal.getDeadline()));
        }
        else{
            holder.complete.setVisibility(View.GONE);
        }
        holder.percentComplete.setText(""+current+"/"+total +" "+goal.getMeasurement());
        holder.progressBar.setProgress(current);
        holder.progressBar.setMax(total);

        int tmpInt = (int) (current.floatValue()/total.floatValue() *100);
        holder.actualPercent.setText(tmpInt+"%");

        holder.remove.setClickable(true);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Goals")
                        .document(firebaseUser.getUid())
                        .collection("Sub")
                        .document(goal.getGoalUid())
                        .delete();
                mGoal.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
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
        public ImageView remove;
        public TextView theGoal, complete, percentComplete, actualPercent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            theGoal = itemView.findViewById(R.id.theGoal);
            complete = itemView.findViewById(R.id.completed);
            percentComplete = itemView.findViewById(R.id.percentComplete);
            progressBar = itemView.findViewById(R.id.progressBar1);
            db = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            goalRelativeLayout = itemView.findViewById(R.id.goalRelativeLayout);
            remove = itemView.findViewById(R.id.removeSavedGoal);
            actualPercent = itemView.findViewById(R.id.actualPercent);
        }
    }
}
