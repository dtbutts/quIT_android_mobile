package com.example.quit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewHealthFragAlcohol extends Fragment {

    private String group1, group2, group3;
    private ExpandableListView expandableListView1, expandableListView2, expandableListView3;
    private ExpandableListAdapter expandableListAdapter;
    private Map<String,List<String>> collection1, collection2, collection3;

    public NewHealthFragAlcohol() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_new_health_frag_alcohol, container, false);

        createGroups();
        createCollection();

        expandableListView1 = theView.findViewById(R.id.expandable1);
        expandableListView2 = theView.findViewById(R.id.expandable2);
        expandableListView3 = theView.findViewById(R.id.expandable3);


        expandableListAdapter = new MyExpandableListAdapter(this.getContext(), group1, collection1);
        expandableListView1.setAdapter(expandableListAdapter);
        expandableListView1.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView1.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView1.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();

                return true;
            }
        });


        expandableListAdapter = new MyExpandableListAdapter(this.getContext(), group2, collection2);
        expandableListView2.setAdapter(expandableListAdapter);
        expandableListView2.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView2.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();

                return true;
            }
        });


        expandableListAdapter = new MyExpandableListAdapter(this.getContext(), group3, collection3);
        expandableListView3.setAdapter(expandableListAdapter);
        expandableListView3.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView3.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView3.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();

                return true;
            }
        });


        return theView;
    }

    private void createGroups() {
        group1 = "Blood Sugar";
        group2 = "Withdrawal Symptoms";
        group3 = "Improved Sleep";
    }

    private void createCollection() {

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

        /*
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
        */

        collection1 = new HashMap<>();
        collection1.put(group1, bloodSugar);

        collection2 = new HashMap<>();
        collection2.put(group2, withdrawalSymptoms);

        collection3 = new HashMap<>();
        collection3.put(group3, improvedSleep);


    }
}