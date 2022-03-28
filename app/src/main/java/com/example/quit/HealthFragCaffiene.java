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

public class HealthFragCaffiene extends Fragment {

    RecyclerView recyclerView;
    private List<Health_Category> categories;
    private List<Integer> progressBars;
    private Date soberSince;
    private long soberTimeinMilliSeconds;
    float timeSoberHours;

    public HealthFragCaffiene() {
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
        List<String> improvedSleep = new ArrayList<>();
        improvedSleep.add("After 24 hours, any effects of previously consumed caffeine should be completely gone. This leads to improved sleep!");
        improvedSleep.add("Many of the withdrawal symptoms from caffeine occur in the first few days. You're likely to start experiencing some now, like headache, anxiety, and irritability");
        improvedSleep.add("The first few days are the hardest, but keep pushing! You'll enjoy many benefits later if you can make it through the beginning!");

        List<String> withdrawalSymptoms = new ArrayList<>();
        withdrawalSymptoms.add("After 9 days all or nearly all physical withdrawal symptoms should have passed");
        withdrawalSymptoms.add("You will likely still have psychological urges for caffeine, especially on slow mornings");
        withdrawalSymptoms.add("Now that the hard part is over, you will soon start to enjoy the bulk of the quality of life benefits from being caffeine free!");

        List<String> moodRegulation = new ArrayList<>();
        moodRegulation.add("Shortly after the withdrawal symptoms pass, you should start to notice a more regular and improved mood!");
        moodRegulation.add("Many people use caffeine to help cut short their bad mood in the mornings. Without that reliance, your mood will naturally get better");

        List<String> decreasedAnxiety = new ArrayList<>();
        decreasedAnxiety.add("After 2 weeks of no caffeine, you should notice that anxiety spikes begin to reduce");
        decreasedAnxiety.add("Caffeine causes anxiety in many people who use it, and clearing caffeine out of your system can help reduce these unwanted spikes, especially after withdrawal symptoms pass.");

        List<String> lbp = new ArrayList<>();
        lbp.add("1 month after quitting caffeine, you're likely to experience lower blood pressure levels");
        lbp.add("Caffeine, especially when used around strenuous workouts, is known to cause increased blood pressure in some individuals. By cutting caffeine out you don't need to worry about the potential side effects of increased blood pressure caused by caffeine!");

        List<String> generalHealth = new ArrayList<>();
        generalHealth.add("Unless you used to drink your coffee black, after a month and a half without caffeine you may start to notice some benefits from cutting out the extra calories and sugars from caffeinated beverages");
        generalHealth.add("Drinking sugary drinks like non-black coffee or energy drinks is known to increase your chance of getting type 2 diabetes. You can sleep better at night knowing you've lowered that risk!");
        generalHealth.add("Congratulations on making it this far in your recovery! Addiction can be a lifelong battle, but remember the accomplishments you've made and let them continue to motivate your future journey!");



        categories = new ArrayList<>();
        categories.add(new Health_Category("Improved Sleep", improvedSleep, timeSober, 1));
        categories.add(new Health_Category("Withdrawal Symptoms", withdrawalSymptoms, timeSober, 9));
        categories.add(new Health_Category("Mood Regulation", moodRegulation, timeSober, 10));
        categories.add(new Health_Category("Decreased Anxiety", decreasedAnxiety, timeSober, 14));
        categories.add(new Health_Category("Lower Blood Pressure", lbp, timeSober, 30));
        categories.add(new Health_Category("General Health", generalHealth, timeSober, 45));

        categories.add(new Health_Category("Filler For Button", generalHealth, 1, 1));

    }

    private void initRecyclerView() {
        Health_Adapter health_adapter = new Health_Adapter(categories, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(health_adapter);
    }

}