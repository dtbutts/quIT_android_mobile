package com.example.quit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import Model.User;


public class HealthFragAlcohol extends Fragment{

    //variables here
    private Date soberSince;
    private long soberTimeinMilliSeconds;
    private ProgressBar item1progress;
    private ProgressBar item2progress;
    private ProgressBar item3progress;
    private ProgressBar item4progress;
    private ProgressBar item5progress;
    private ProgressBar item6progress;
    private Button changeAddiction;


    //required empty public constructor
    public HealthFragAlcohol(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseFirestore db;
        FirebaseAuth mAuth;

        //instantiate dastabase variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference dRef  = db.collection("userAccount").document(firebaseUser.getUid());

        // grab addiction type variable to be used in the navigation
        dRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        if (user.getAddictionType() != null) {
                            soberSince = user.getSoberSince();
                            soberTimeinMilliSeconds = soberSince.getTime();
                            Log.wtf("DTB", String.valueOf(soberTimeinMilliSeconds));

                            //Now set the progress bars here
                            float goalTime1 = 25;         //enter days to goal
                            long goalTime2 = 7 ;
                            long goalTime3 = 25 ;
                            long goalTime4 = 25 ;
                            long goalTime5 = 100 ;
                            long goalTime6 = 3;

                            float timeSoberHours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - user.getSoberSince().getTime());

                            item1progress.setProgress(getProgressPercent(timeSoberHours, goalTime1));
                            item2progress.setProgress(getProgressPercent(timeSoberHours, goalTime2));
                            item3progress.setProgress(getProgressPercent(timeSoberHours, goalTime3));
                            item4progress.setProgress(getProgressPercent(timeSoberHours, goalTime4));
                            item5progress.setProgress(getProgressPercent(timeSoberHours, goalTime5));
                            item6progress.setProgress(getProgressPercent(timeSoberHours, goalTime6));


                        }
                    } else {
                        //nothing
                    }
                } else {
                    //nothing
                }
            }
        });

        View theView = inflater.inflate(R.layout.health_frag_alcohol, container, false);

        //instantiate fields from xml
        item1progress = theView.findViewById(R.id.item1progress);
        item2progress = theView.findViewById(R.id.item2progress);
        item3progress = theView.findViewById(R.id.item3progress);
        item4progress = theView.findViewById(R.id.item4progress);
        item5progress = theView.findViewById(R.id.item5progress);
        item6progress = theView.findViewById(R.id.item6progress);

        //set up nav for change addiction button
        changeAddiction = theView.findViewById(R.id.changeAddiction);

        //set up if button is pressed
        changeAddiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dRef.update("addictionType", "z");

                ((MainActivity)getActivity()).updateAddictionVariable();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddictionSelectionFragment()).commit();
            }
        });

        return theView;
    }

    //used to get a percentage from the time sober and a goal time
    private int getProgressPercent(float timeSoberHours, float goalTime){
        int setTo;
        goalTime = ((float)24) * goalTime;

        float percentOfGoal = (timeSoberHours / goalTime) * ((float) 100);
        setTo = Math.round(percentOfGoal);

        return setTo;
    }

}


