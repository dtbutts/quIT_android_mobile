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

public class HealthFragNicotine extends Fragment {

    RecyclerView recyclerView;
    private List<Health_Category> categories;
    private List<Integer> progressBars;
    private Date soberSince;
    private long soberTimeinMilliSeconds;
    float timeSoberHours;

    public HealthFragNicotine() {
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
        List<String> heartHealth = new ArrayList<>();
        heartHealth.add("After 24 hours of not using nicotine, your risk of a heart attack can drop by up to half!");
        heartHealth.add("During this time the early symptoms of nicotine withdrawal like headaches, mood swings, anxiety, and irritability will start to take place.");
        heartHealth.add("These early hours and days are the hardest part physically when quitting nicotine. Push through the temporary discomfort toward your healthier life!");

        List<String> tasteSmell = new ArrayList<>();
        tasteSmell.add("After 48 hours you may start to notice an improved sense of taste and smell!");
        tasteSmell.add("At this stage you're likely in the midst of some withdrawal symptoms like anxiety and irritability.");
        tasteSmell.add("Keep going, the benefits to come are worth the discomfort!");

        List<String> withdrawalSymptoms = new ArrayList<>();
        withdrawalSymptoms.add("After 3 days, the worst of the physical withdrawal symptoms should have passed!");
        withdrawalSymptoms.add("You will most likely still feel psychological cravings, but the physical symptoms should be gone or almost gone by this time.");
        withdrawalSymptoms.add("You made it through the painful part! Your cravings might still come and go, but use your achievement as motivation to push through the urges!");

        List<String> circulation = new ArrayList<>();
        circulation.add("After 2 weeks your blood circulation will start to improve! You might notice physical activity becoming easier!");
        circulation.add("This period and benefit can continue to get stronger up to week 12 for some individuals.");
        circulation.add("From here things should be a lot easier! Most if not all of the psychological difficulties will be behind you now!");

        List<String> lungCapacity = new ArrayList<>();
        lungCapacity.add("Depending on your old method of consuming nicotine, your lung capacity after 3 months will be markedly improved!");
        lungCapacity.add("Side effects like coughing and wheezing will subside and your risk of respiratory disease will be greatly lowered!");
        lungCapacity.add("Congratulations on making it this far in your recovery!");

        List<String> generalHealth = new ArrayList<>();
        generalHealth.add("After a year and beyond all nicotine caused health risks will be diminished considerably!");
        generalHealth.add("You've greatly improved your chances of avoiding things like heart disease, stroke, and cancer!");
        generalHealth.add("You made it to a year sober from nicotine! Addiction can be a lifelong battle, but remember the difficult accomplishment you've achieved and use it as motivation to stay sober!");



        categories = new ArrayList<>();
        categories.add(new Health_Category("Heart Health", heartHealth, timeSober, 1));
        categories.add(new Health_Category("Taste and Smell", tasteSmell, timeSober, 2));
        categories.add(new Health_Category("Withdrawal Symptoms", withdrawalSymptoms, timeSober, 3));
        categories.add(new Health_Category("Circulation", circulation, timeSober, 14));
        categories.add(new Health_Category("Lung Capacity", lungCapacity, timeSober, 90));
        categories.add(new Health_Category("General Health", generalHealth, timeSober, 365));

        categories.add(new Health_Category("Filler For Button", generalHealth, 1, 1));

    }

    private void initRecyclerView() {
        Health_Adapter health_adapter = new Health_Adapter(categories, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(health_adapter);
    }

}