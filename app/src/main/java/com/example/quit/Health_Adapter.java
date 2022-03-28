package com.example.quit;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.annotation.Nonnegative;

public class Health_Adapter extends RecyclerView.Adapter {

    private static final String TAG = "HealthAdapter";
    List<Health_Category> categories;
    Context context;

    public Health_Adapter(List<Health_Category> categories, Context context){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == categories.size() - 1)
        {
            //Last position
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if(viewType == 1) {
            view = layoutInflater.inflate(R.layout.select_addiction_button, parent, false);
            return new ButtonVH(view);
        }

        view = layoutInflater.inflate(R.layout.health_box, parent, false);
        return new CategoryVH(view);

    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, int position) {

        if(position != categories.size() - 1) {
            CategoryVH categoryVH = (CategoryVH) holder;

            Health_Category category = categories.get(position);
            categoryVH.nameTextView.setText(category.getName());
            categoryVH.factsTextView.setText(category.getFacts());

            int prog = getProgressPercent(category.getCurrentProgress(), category.getTotalProgress());

            categoryVH.progressBar.setProgress(prog);

            if(prog >= 100){
                categoryVH.progressBarPercent.setText("100%");
            }
            else {
                categoryVH.progressBarPercent.setText(String.valueOf(prog) + "%");
            }

            boolean isExpanded = categories.get(position).isExpanded();
            if (isExpanded) {
                categoryVH.showMoreTextView.setText("Show Less");
            } else {
                categoryVH.showMoreTextView.setText("Show More");
            }
            categoryVH.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
        else
        {
            ButtonVH buttonVH = (ButtonVH) holder;


        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    class CategoryVH extends RecyclerView.ViewHolder{
        private static final String TAG = "CategoryVH";

        LinearLayout expandableLayout;
        TextView nameTextView, factsTextView, showMoreTextView, progressBarPercent;
        ProgressBar progressBar;

        public CategoryVH(@NonNull final View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.health_category);
            showMoreTextView = itemView.findViewById(R.id.show_more);
            factsTextView = itemView.findViewById(R.id.expandable_text);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBarPercent = itemView.findViewById(R.id.percentComplete);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);


            showMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Health_Category category = categories.get(getAdapterPosition());
                    category.setExpanded(!category.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    TextView showView = view.findViewById(R.id.show_more);

                }
            });
        }
    }

    class ButtonVH extends RecyclerView.ViewHolder{
        private static final String TAG = "ButtonVH";

        Button button;

        public ButtonVH(@NonNull final View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.changeAddictionButton);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AddictionSelectionFragment()).commit();



                }
            });
        }
    }

    private int getProgressPercent(float timeSoberHours, float goalTime){
        if(timeSoberHours == 0)
        {
            return 0;
        }
        int setTo;
        goalTime = ((float)24) * goalTime;

        float percentOfGoal = (timeSoberHours / goalTime) * ((float) 100);
        setTo = Math.round(percentOfGoal);

        return setTo;
    }

}
