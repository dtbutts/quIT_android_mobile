package com.example.quit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import Model.User;

public class HomeFragment extends Fragment {
    Button startSobriety, signOut;
    Timer timer;
    Activity activity;
    TimerTask timerTask;
    TextView yearCounter, dayCounter, hourCounter, minCounter, secCounter, soberSince;
    RelativeLayout yearsCounterLayout;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    Long timeInMilliSeconds = 0L;
    long years, days, hours, mins, secs;
    boolean started = false;
    ConstraintLayout kahuna;

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
        signOut = view.findViewById(R.id.signOut);
        yearCounter = view.findViewById(R.id.yearCounter);
        dayCounter= view.findViewById(R.id.dayCounter);
        hourCounter= view.findViewById(R.id.hourCounter);
        minCounter= view.findViewById(R.id.minCounter);
        secCounter= view.findViewById(R.id.secCounter);
        soberSince = view.findViewById(R.id.soberSince);
        yearsCounterLayout = (RelativeLayout) view.findViewById(R.id.yearsCounterLayout);
        yearsCounterLayout.setVisibility(View.GONE);
        kahuna = view.findViewById(R.id.kahuna);
        kahuna.setVisibility(View.GONE);
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

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, StartActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        startSobriety.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!started){
                    startTimer();
                    startSobriety.setText("Reset Sobriety");
                    started =true;
                    setButtonStatus();
//                    Log.d("SANITY", "DEARGOD");
//                    //Intent alarm = new Intent(getContext(), BackgroundService.class);
//                    //PendingIntent pendingIntent = PendingIntent.getService(this,0,intent,0);
////                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(
////                            new BroadcastReceiver() {
////                                @Override
////                                public void onReceive(Context context, Intent intent) {
////                                    double latitude = intent.getDoubleExtra(LocationBroadcastService.EXTRA_LATITUDE, 0);
////                                    double longitude = intent.getDoubleExtra(LocationBroadcastService.EXTRA_LONGITUDE, 0);
////                                    textView.setText("Lat: " + latitude + ", Lng: " + longitude);
////                                }
////                            }, new IntentFilter(LocationBroadcastService.ACTION_LOCATION_BROADCAST)
////                    );
//                    Intent alarm = new Intent(getContext(), AlarmReceiver.class);
////                    boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
////                    if(alarmRunning == false) {
//                        Log.d("SERVICELOG", "OG");
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
//                        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                        AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
//                        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 300000, pendingIntent);

                    //}
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

            Date date = new Date(System.currentTimeMillis());
            userReference.update("soberSince", date);
            DateFormat simple = new SimpleDateFormat("MMM dd, yyyy");
            soberSince.setText("Sober since "+simple.format(date));
        }
        else{
            userReference
                    .update("buttonPressed", false);
            userReference.update("soberSince", null);
            soberSince.setText("");

            //userReference.update("totalTimeSober", 0);
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
                                if(user.getSoberSince()!=null)
                                {
                                    DateFormat simple = new SimpleDateFormat("MMM dd, yyyy");
                                    soberSince.setText("Sober since "+simple.format(user.getSoberSince()));
                                }
                                else{
                                    soberSince.setText("");
                                }
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
                        kahuna.setVisibility(View.VISIBLE);
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
        super.onStop();
        Log.d("Checking", "Destroy");
        DocumentReference userReference = db.collection("userAccount")
                .document(firebaseUser.getUid());
        Long tmpTime = timeInMilliSeconds;
        userReference
                .update("totalTimeSober", tmpTime);
        Long lastEndtime = System.currentTimeMillis();
        userReference
                .update("lastEndTime", lastEndtime);

    }

    @Override
    public void onPause() {

        super.onPause();
        Log.d("Checking", "Destroy");
        DocumentReference userReference = db.collection("userAccount")
                .document(firebaseUser.getUid());
        Long tmpTime = timeInMilliSeconds;
        userReference
                .update("totalTimeSober", tmpTime);
        Long lastEndtime = System.currentTimeMillis();
        userReference
                .update("lastEndTime", lastEndtime);
    }
}
