package com.example.quit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GoalsFragment extends Fragment {
    private ImageView addGoal;
    private Activity activity;
    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.goals_frag,container, false);

        addGoal = view.findViewById(R.id.addGoal);

        addGoal.setClickable(true);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, CreateGoalActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        return view;
    }
}
