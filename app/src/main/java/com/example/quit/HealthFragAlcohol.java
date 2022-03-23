package com.example.quit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ProgressBar item7progress;
    private ProgressBar item8progress;
    private Button changeAddiction;

    private List<String> groupList;
    private List<String> childList;
    private Map<String,List<String>> collection;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;


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
                            float goalTime1 = 1;         //enter days to goal
                            float goalTime2 = 3 ;
                            float goalTime3 = 7 ;
                            float goalTime4 = 10 ;
                            float goalTime5 = 14 ;
                            float goalTime6 = 30;
                            float goalTime7 = 60;
                            float goalTime8 = 365;

                            float timeSoberHours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - user.getSoberSince().getTime());

                            item1progress.setProgress(getProgressPercent(timeSoberHours, goalTime1));
                            item2progress.setProgress(getProgressPercent(timeSoberHours, goalTime2));
                            item3progress.setProgress(getProgressPercent(timeSoberHours, goalTime3));
                            item4progress.setProgress(getProgressPercent(timeSoberHours, goalTime4));
                            item5progress.setProgress(getProgressPercent(timeSoberHours, goalTime5));
                            item6progress.setProgress(getProgressPercent(timeSoberHours, goalTime6));
                            item7progress.setProgress(getProgressPercent(timeSoberHours, goalTime7));
                            item8progress.setProgress(getProgressPercent(timeSoberHours, goalTime8));
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
        item7progress = theView.findViewById(R.id.item7progress);
        item8progress = theView.findViewById(R.id.item8progress);

        /*

        //Instantiate expandable lists
        createGroupList();
        createCollection();

        //Get the listview
        expandableListView = theView.findViewById(R.id.BS);

        //expandableListAdapter = new MyExpandableListAdapter(this.getContext(), group, collection);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();

                return true;
            }
        });


        */

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

    private void createCollection() {
        String[] bloodSugar = {"After 24 hours your blood sugar may begin to normalize as the alcohol leaves your system,",
                "During this time, early withdrawal symptoms will likely start to take effect. These can include things like tremors, sweating, anxiety, fatigue, and depression",
                "Don't let the early symptoms stop you! Push through the temporary discomfort to reach your improved life!"
        };
        String[] withdrawalSymptoms = {"After 72 hours your early symptoms should start to become more manageable!",
                "This timeframe can vary for each individual, but if you notice symptoms getting worse beyond this point or start experiencing more severe symptoms like seizures, contact your doctor immediately",
                "Use the first feelings of getting better as motivation to keep going! There is a long road ahead, but enjoy these small victories along the way!"
        };
        String[] improvedSleep = {"After 1 week you can expect your symptoms to start ending completely and your sleep to dramatically improve!",
                "You may have used alcohol in the past to help you fall asleep, but when sober you will be getting a much higher quality night's rest",
                "Getting more sleep can provide countless benefits to your life! Your improved lifestyle can help motivate you on your way to full recovery!"
        };
        String[] hydrationLevels = {"After 10 days your hydration levels will be substantially improved! This will improve many aspects of your health, including the health and function of your skin, hair, and organs!",
                "Some long term side effects, like nightmares and anxiety, can start to show themselves",
                "You've reached a huge milestone! Give yourself credit and enjoy a well deserved sense of accomplishment. If you're still having some struggles on your journey, don't worry, you're not alone. Keep going strong and if you need help, visit the resources page!"
        };
        String[] weightLoss = {"After 2 weeks you may start to notice some weight loss! Alcohol has a lot of calories, and by cutting those calories out the scale might start to show some lower numbers!",
                "Remember that giving up alcohol is only one part of a healthier lifestyle. If you want to lose weight, try not to replace your old alcohol calories with new calorie sources",
                "Weight loss is great, but it's only a small piece of the improved life your working toward. Keep the momentum going!"
        };
        String[] organHealth = {"After a month of no alcohol your body will be benefiting in all sorts of ways! Your skin may start to clear up and look healthier, blood pressure may return to normal levels, and your liver health will greatly improve",
                "If you had longer lasting symptoms, you should finally start to notice some relief",
                "Congratulations, 1 whole month down! Be proud of the improved care you're showing your body!"
        };
        String[] generalHealth = {"After 3 months you should feel a general sense of improved health and energy. Quitting alcohol has improved your health in so many ways that you're sure to feel it somewhere by this point",
                "Don't be ashamed or worried if some of your symptoms persist. Over time the benefits of quitting alcohol will start to outweigh the challenges!",
                "You're well on your way to enjoying an improved and healthier life! Remember the struggles you've overcame to get here!"
        };
        String[] fullRecovery = {"After 1 year of being sober most people will be completely or very close to completely recovered! Your risk of cancer, liver disease, stroke, and heart disease will be greatly diminished!",
                "Addiction recovery can be a lifelong struggle, remember what it took to get to this point and stay strong in the years to come",
                "You've retaken control of your life and proven to yourself that you can accomplish one of the most difficult things a person can do. Congratulations on a full recovery!"
        };
        collection = new HashMap<String, List<String>>();
        for(String group : groupList){
            switch (group){
                case "Blood Sugar":         loadChild(bloodSugar);
                                            break;
                case "Withdrawal Symptoms": loadChild(withdrawalSymptoms);
                                            break;
                case "Improved Sleep":      loadChild(improvedSleep);
                                            break;
                case "Hydration Levels":    loadChild(hydrationLevels);
                                            break;
                case "Weight Loss":         loadChild(weightLoss);
                                            break;
                case "Organ Health":        loadChild(organHealth);
                                            break;
                case "General Health":      loadChild(generalHealth);
                                            break;
                case "Full Recovery":       loadChild(fullRecovery);
                                            break;
            }
            collection.put(group,childList);
        }
    }

    private void loadChild(String[] groups) {
        childList = new ArrayList<>();
        for(String group : groups){
            childList.add(group);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("Blood Sugar");
        groupList.add("Withdrawal Symptoms");
        groupList.add("Improved Sleep");
        groupList.add("Hydration Levels");
        groupList.add("Weight Loss");
        groupList.add("Organ Health");
        groupList.add("General Health");
        groupList.add("Full Recovery");
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


