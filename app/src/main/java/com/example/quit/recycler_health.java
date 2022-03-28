package com.example.quit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;

import Model.User;

public class recycler_health extends Fragment {

    RecyclerView recyclerView;
    private List<Health_Category> categories;
    private List<Integer> progressBars;
    private Date soberSince;
    private long soberTimeinMilliSeconds;
    float timeSoberHours;

    public recycler_health() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View theView =  inflater.inflate(R.layout.recycler_health, container, false);

        FirebaseFirestore db;
        FirebaseAuth mAuth;

        //instantiate database variables
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
                            if(soberSince == null)
                            {
                                soberTimeinMilliSeconds = 0;
                            }
                            else {
                                soberTimeinMilliSeconds = soberSince.getTime();
                            }

                            //Now set the progress bars here
                            float goalTime1 = 1;         //enter days to goal
                            float goalTime2 = 3 ;
                            float goalTime3 = 7 ;
                            float goalTime4 = 10 ;
                            float goalTime5 = 14 ;
                            float goalTime6 = 30;
                            float goalTime7 = 60;
                            float goalTime8 = 365;


                            float timeSoberHours;
                            if(soberSince == null)
                            {
                                timeSoberHours = 0;
                            }
                            else {
                                timeSoberHours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - soberTimeinMilliSeconds);
                            }

                            initCategories(timeSoberHours);
                            recyclerView = theView.findViewById(R.id.health_recycler);
                            initRecyclerView();

                            /*
                            item1progress.setProgress(getProgressPercent(timeSoberHours, goalTime1));
                            item2progress.setProgress(getProgressPercent(timeSoberHours, goalTime2));
                            item3progress.setProgress(getProgressPercent(timeSoberHours, goalTime3));
                            item4progress.setProgress(getProgressPercent(timeSoberHours, goalTime4));
                            item5progress.setProgress(getProgressPercent(timeSoberHours, goalTime5));
                            item6progress.setProgress(getProgressPercent(timeSoberHours, goalTime6));
                            item7progress.setProgress(getProgressPercent(timeSoberHours, goalTime7));
                            item8progress.setProgress(getProgressPercent(timeSoberHours, goalTime8));
                             */
                        }
                    } else {
                        //nothing
                    }
                } else {
                    //nothing
                }
            }
        });

        Log.wtf("DTB", String.valueOf(timeSoberHours));
        //initCategories(timeSoberHours);

        //recyclerView = theView.findViewById(R.id.health_recycler);
        //initRecyclerView();

        return theView;
    }

    private void initCategories(float timeSober){
        List<String> bloodSugar = new ArrayList<>();
        bloodSugar.add("After 24 hours your blood sugar may begin to normalize as the alcohol leaves your system,");
        bloodSugar.add("During this time, early withdrawal symptoms will likely start to take effect. These can include things like tremors, sweating, anxiety, fatigue, and depression");
        bloodSugar.add("Don't let the early symptoms stop you! Push through the temporary discomfort to reach your improved life!");

        List<String> withdrawalSymptoms = new ArrayList<>();
        withdrawalSymptoms.add("After 72 hours your early symptoms should start to become more manageable!");
        withdrawalSymptoms.add("This timeframe can vary for each individual, but if you notice symptoms getting worse beyond this point or start experiencing more severe symptoms like seizures, contact your doctor immediately");
        withdrawalSymptoms.add("Use the first feelings of getting better as motivation to keep going! There is a long road ahead, but enjoy these small victories along the way!");

        List<String> improvedSleep = new ArrayList<>();
        improvedSleep.add("After 1 week you can expect your symptoms to start ending completely and your sleep to dramatically improve!");
        improvedSleep.add("You may have used alcohol in the past to help you fall asleep, but when sober you will be getting a much higher quality night's rest");
        improvedSleep.add("Getting more sleep can provide countless benefits to your life! Your improved lifestyle can help motivate you on your way to full recovery!");

        List<String> hydrationLevels = new ArrayList<>();
        hydrationLevels.add("After 10 days your hydration levels will be substantially improved! This will improve many aspects of your health, including the health and function of your skin, hair, and organs!");
        hydrationLevels.add("Some long term side effects, like nightmares and anxiety, can start to show themselves");
        hydrationLevels.add("You've reached a huge milestone! Give yourself credit and enjoy a well deserved sense of accomplishment. If you're still having some struggles on your journey, don't worry, you're not alone. Keep going strong and if you need help, visit the resources page!");

        List<String> weightLoss = new ArrayList<>();
        weightLoss.add("After 2 weeks you may start to notice some weight loss! Alcohol has a lot of calories, and by cutting those calories out the scale might start to show some lower numbers!");
        weightLoss.add("Remember that giving up alcohol is only one part of a healthier lifestyle. If you want to lose weight, try not to replace your old alcohol calories with new calorie sources");
        weightLoss.add("Weight loss is great, but it's only a small piece of the improved life your working toward. Keep the momentum going!");

        List<String> organHealth = new ArrayList<>();
        organHealth.add("After a month of no alcohol your body will be benefiting in all sorts of ways! Your skin may start to clear up and look healthier, blood pressure may return to normal levels, and your liver health will greatly improve");
        organHealth.add("If you had longer lasting symptoms, you should finally start to notice some relief");
        organHealth.add("Congratulations, 1 whole month down! Be proud of the improved care you're showing your body!");

        List<String> generalHealth = new ArrayList<>();
        generalHealth.add("After 3 months you should feel a general sense of improved health and energy. Quitting alcohol has improved your health in so many ways that you're sure to feel it somewhere by this point");
        generalHealth.add("Don't be ashamed or worried if some of your symptoms persist. Over time the benefits of quitting alcohol will start to outweigh the challenges!");
        generalHealth.add("You're well on your way to enjoying an improved and healthier life! Remember the struggles you've overcame to get here!");

        List<String> fullRecovery = new ArrayList<>();
        fullRecovery.add("After 1 year of being sober most people will be completely or very close to completely recovered! Your risk of cancer, liver disease, stroke, and heart disease will be greatly diminished!");
        fullRecovery.add("Addiction recovery can be a lifelong struggle, remember what it took to get to this point and stay strong in the years to come");
        fullRecovery.add("You've retaken control of your life and proven to yourself that you can accomplish one of the most difficult things a person can do. Congratulations on a full recovery!");


        categories = new ArrayList<>();
        categories.add(new Health_Category("Blood Sugar", bloodSugar, timeSober, 1));
        categories.add(new Health_Category("Withdrawal Symptoms", withdrawalSymptoms, timeSober, 3));
        categories.add(new Health_Category("Improved Sleep", improvedSleep, timeSober, 7));
        categories.add(new Health_Category("Hydration Levels", hydrationLevels, timeSober, 10));
        categories.add(new Health_Category("Weight Loss", weightLoss, timeSober, 14));
        categories.add(new Health_Category("Organ Health", organHealth, timeSober, 30));
        categories.add(new Health_Category("General Health", organHealth, timeSober, 60));
        categories.add(new Health_Category("Full Recovery", fullRecovery, timeSober, 365));

        categories.add(new Health_Category("Filler For Button", fullRecovery, 1, 1));

    }

    private void initRecyclerView() {
        Health_Adapter health_adapter = new Health_Adapter(categories, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(health_adapter);
    }

}