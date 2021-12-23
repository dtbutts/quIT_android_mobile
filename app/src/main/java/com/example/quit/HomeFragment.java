package com.example.quit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import Model.User;

public class HomeFragment extends Fragment {
    Button startSobriety;
    Timer timer;
    TimerTask timerTask;
    TextView yearCounter, dayCounter, hourCounter, minCounter, secCounter;
    RelativeLayout yearsCounterLayout;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    Long timeInMilliSeconds = 0L;
    long years, days, hours, mins, secs;
    boolean started = false;

    protected Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity =(Activity) context;
        }
    }
    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Log.d("Checking", "onCreateView");
        View view =inflater.inflate(R.layout.home_frag,container, false);
        startSobriety = view.findViewById(R.id.startSobriety);
        yearCounter = view.findViewById(R.id.yearCounter);
        dayCounter= view.findViewById(R.id.dayCounter);
        hourCounter= view.findViewById(R.id.hourCounter);
        minCounter= view.findViewById(R.id.minCounter);
        secCounter= view.findViewById(R.id.secCounter);
        yearsCounterLayout = (RelativeLayout) view.findViewById(R.id.yearsCounterLayout);
        yearsCounterLayout.setVisibility(View.GONE);
        timer = new Timer();
//        getActivity().registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        }, new IntentFilter("bcNewMessage"));

        //set started to whatever db.collection.current user button is
        //set time variable to db.collection.currentuser total time
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getTotalTimeElapsed();
        getButtonStatus();
//        if(started){
//            startSobriety.setText("Reset Sobriety");
//        }

        startSobriety.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!started){
                    startTimer();
                    startSobriety.setText("Reset Sobriety");
                    started =true;
                    setButtonStatus();
                }
                else if(started){
                    started =false;
                    timeInMilliSeconds=0L;
                    timerTask.cancel();
                    setButtonStatus();
                    yearCounter.setText(formatTime(0l));
                    dayCounter.setText(formatTime(0l));
                    hourCounter.setText(formatTime(0l));
                    minCounter.setText(formatTime(0l));
                    secCounter.setText(formatTime(0l));
                    //startTimer();
                    startSobriety.setText("Start Sobriety");
                }

            }
        });
        return view;
    }

    private void setButtonStatus() {
        DocumentReference userReference = db.collection("userAccount")
                .document(firebaseUser.getUid());
        if(started){
            userReference
                    .update("buttonPressed", true);
        }
        else{
            userReference
                    .update("buttonPressed", false);
        }


    }

    private void getButtonStatus() {

        db.collection("userAccount")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user =document.toObject(User.class);
                                if(user.getButtonPressed()){
                                    started = true;
                                    Log.d("THURSDAY", "BUTTON IS PRESSED FROM DB");
                                    startSobriety.setText("Reset Sobriety");
                                    startTimer();
                                }
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void getTotalTimeElapsed() {

        db.collection("userAccount")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user =document.toObject(User.class);
                                if(user.getTotalTimeSober()==0){
                                    timeInMilliSeconds =0L;
                                }
                                else{
                                    timeInMilliSeconds = user.getTotalTimeSober() + (System.currentTimeMillis() - user.getLastEndTime());
                                    Long year = 31540000000l;
                                    if(timeInMilliSeconds>=year){
                                        yearsCounterLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeInMilliSeconds += 1000;
                convertSeconds(timeInMilliSeconds);
                yearCounter.setText(formatTime(years));
                dayCounter.setText(formatTime(days));
                hourCounter.setText(formatTime(hours));
                minCounter.setText(formatTime(mins));
                secCounter.setText(formatTime(secs));
            }
        };
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                           timeInSeconds++;
//                           convertSeconds(timeInSeconds);
//                           dayCounter.setText(formatTime(days));
//                           hourCounter.setText(formatTime(hours));
//                           minCounter.setText(formatTime(mins));
//                           secCounter.setText(formatTime(secs));
//
//                    }
//                }
//
//                );
//
//            }
//        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
    private void convertSeconds(Long milliseconds){

        days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        if(days>=365){
            years = days/365;
            days = TimeUnit.MILLISECONDS.toDays(milliseconds)%365;
        }
        else{
            years = 0l;
        }
        hours = TimeUnit.MILLISECONDS.toHours(milliseconds) %24;
        mins = TimeUnit.MILLISECONDS.toMinutes(milliseconds)%60;
        secs = TimeUnit.MILLISECONDS.toSeconds(milliseconds)%60;
//        days = seconds/(24*3600);
//        seconds = seconds %(24*3600);
//        hours = seconds/3600;
//        seconds%=3600;
//        mins = seconds/60;
//        seconds %=60;
//        secs = seconds;
    }
    private String formatTime(long time){
        if(time>99){
            return String.format("%03d", time);
        }
        return String.format("%02d", time);
    }

    @Override
    public void onDestroy() {
        Log.d("Checking", "Destroy");
        DocumentReference userReference = db.collection("userAccount")
                .document(firebaseUser.getUid());
        Long tmpTime = timeInMilliSeconds;
        userReference
                .update("totalTimeSober", tmpTime);
        Long lastEndtime = System.currentTimeMillis();
        userReference
                .update("lastEndTime", lastEndtime);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d("Checking", "Stop");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d("Checking", "Pause");
        super.onPause();
    }
}
