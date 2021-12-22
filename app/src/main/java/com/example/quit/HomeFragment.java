package com.example.quit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    Button startSobriety;
    Timer timer;
    TimerTask timerTask;
    TextView dayCounter, hourCounter, minCounter, secCounter;
    int timeInSeconds = 0;
    int days, hours, mins, secs;
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
        View view =inflater.inflate(R.layout.home_frag,container, false);
        startSobriety = view.findViewById(R.id.startSobriety);
        dayCounter= view.findViewById(R.id.dayCounter);
        hourCounter= view.findViewById(R.id.hourCounter);
        minCounter= view.findViewById(R.id.minCounter);
        secCounter= view.findViewById(R.id.secCounter);
        timer = new Timer();
//        getActivity().registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        }, new IntentFilter("bcNewMessage"));

        //set started to whatever db.collection.current user button is
        //set time variable to db.collection.currentuser total time

        startSobriety.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!started){
                    startTimer();
                    startSobriety.setText("Reset Sobriety");
                    started =true;
                }
                else if(started){
                    started =false;
                    timeInSeconds=0;
                    timerTask.cancel();
                    startTimer();
                    startSobriety.setText("Reset Sobriety");
                }

            }
        });
        return view;
    }

    private void startTimer() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                           timeInSeconds++;
                           convertSeconds(timeInSeconds);
                           dayCounter.setText(formatTime(days));
                           hourCounter.setText(formatTime(hours));
                           minCounter.setText(formatTime(mins));
                           secCounter.setText(formatTime(secs));

                    }
                }

                );

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
    private void convertSeconds(int seconds){
        days = seconds/(24*3600);
        seconds = seconds %(24*3600);
        hours = seconds/3600;
        seconds%=3600;
        mins = seconds/60;
        seconds %=60;
        secs = seconds;
    }
    private String formatTime(int time){
        return String.format("%02d", time);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
