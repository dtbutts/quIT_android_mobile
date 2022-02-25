package com.example.quit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.User;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static BottomNavigationView bottomNav;
    public static SharedPreferences prefs;
    public static boolean firstStart;
    public boolean hasSelectedAddiction = false;

    //database variables
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String addictType = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_frag);

        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        //instantiate dastabase variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference dRef  = db.collection("userAccount").document(firebaseUser.getUid());

        //firstStart = prefs.getBoolean("firstStart", true);

//        Bundle intent = getIntent().getExtras();
//        if(intent!=null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new SocialFragment()).commit();
//            intent = null;
//        }

        //check to see if it is the first time the user has opened the app for
        //initialization and create account info
//        if(firstStart){
//            fragmentManager = getSupportFragmentManager();
//            if(findViewById(R.id.fragment_container)!=null){
//                if(savedInstanceState !=null){
//                    return;
//                }
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                EnterDataFragment enterDataFragment = new EnterDataFragment();
//                fragmentTransaction.add(R.id.fragment_container, enterDataFragment, null);
//                bottomNav.setVisibility(View.GONE);
//                fragmentTransaction.commit();
//            }
//        }
//
//        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            HomeFragment homeFragment = new HomeFragment();
//            fragmentTransaction.add(R.id.fragment_container, homeFragment, null);
//            //bottomNav.setVisibility(View.GONE);
//            fragmentTransaction.commit();
        //}

        //new
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new HomeFragment()).commit();
        //create bottom navigation for each fragment. It will display in the main activity's
        //fragment_container

            // grab addiction type variable to be used in the navigation
            dRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            if (user.getAddictionType() != null) {
                                addictType = user.getAddictionType();
                            }
                        } else {
                            //nothing
                        }
                    } else {
                        //nothing
                    }
                }
            });




        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment =null;
                switch(item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment= new HomeFragment();
                        break;
                    case R.id.nav_money:
                        Log.wtf("DTB", "Clicked on money Frag");
                        selectedFragment=new MoneyStartFragment();
                        break;
                    case R.id.nav_goals:
                        selectedFragment= new GoalsFragment();
                        break;
                    case R.id.nav_social:
                        selectedFragment= new SocialFragment();
                        break;
                    case R.id.nav_health:
                        //navigate to a specific health page based off database field "addictionType", if none, go to selection page
                        if(addictType.equals("z") || addictType.isEmpty()){
                            selectedFragment= new AddictionSelectionFragment();
                        }else if(addictType.equals("n")){
                            selectedFragment = new HealthFragNicotine();
                        }else if(addictType.equals("c")){
                            selectedFragment = new HealthFragCaffiene();
                        }else if(addictType.equals("a")){
                            selectedFragment = new HealthFragAlcohol();
                        }
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }
//    private NavigationBarView.OnItemSelectedListener itemSelectedListener=
//            new BottomNavigationView.OnItemSelectedListener(){
//        @Override
//                public boolean on
//            }


    //this can be called to update the
    public void updateAddictionVariable() {

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
                            addictType = user.getAddictionType();
                        }
                    } else {
                        //nothing
                    }
                } else {
                    //nothing
                }
            }
        });
    }
}