package com.example.quit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_frag);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
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