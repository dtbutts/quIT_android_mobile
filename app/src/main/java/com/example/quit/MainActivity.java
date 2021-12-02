package com.example.quit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static BottomNavigationView bottomNav;
    public static SharedPreferences prefs;
    public static boolean firstStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_frag);
        //if sharedpreferences()
        //setcontentView to create account fragment
        //then when the sumbit button is clicked
        //have it run the below operations
        //else run this below stuff
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        //new
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("firstStart", true);
        if(firstStart){
            fragmentManager = getSupportFragmentManager();
            if(findViewById(R.id.fragment_container)!=null){
                if(savedInstanceState !=null){
                    return;
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EnterDataFragment enterDataFragment = new EnterDataFragment();
                fragmentTransaction.add(R.id.fragment_container, enterDataFragment, null);
                bottomNav.setVisibility(View.GONE);
                fragmentTransaction.commit();
            }
        }

        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        //new
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new HomeFragment()).commit();
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment =null;
                switch(item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment= new HomeFragment();
                        break;
                    case R.id.nav_money:
                        selectedFragment=new MoneyFragment();
                        break;
                    case R.id.nav_goals:
                        selectedFragment= new GoalsFragment();
                        break;
                    case R.id.nav_social:
                        selectedFragment= new SocialFragment();
                        break;
                    case R.id.nav_health:
                        selectedFragment= new HealthFragment();
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
}