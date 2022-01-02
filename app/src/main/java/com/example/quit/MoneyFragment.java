package com.example.quit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Model.Money;

public class MoneyFragment extends Fragment {
    EditText avgMoneySpentWeekly;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    TextView total, expectedMonthly, expectedYearly, startTime, monthVal, weekVal,
            weekTitle, monthTitle;
    TimerTask timerTask;
    Timer timer;
    Button pause;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.money_frag,container, false);

        timer = new Timer();
        avgMoneySpentWeekly = view.findViewById(R.id.edit);
        total = view.findViewById(R.id.total);
        startTime = view.findViewById(R.id.startTime);
        expectedMonthly = view.findViewById(R.id.expectedMonthly);
        expectedYearly = view. findViewById(R.id.expectedYearly);
        monthVal = view.findViewById(R.id.monthVal);
        weekVal = view.findViewById(R.id.weekVal);
        weekTitle = view.findViewById(R.id.titleWeek);
        monthTitle = view.findViewById(R.id.titleMonth);

//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
//            }
//        });

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        avgMoneySpentWeekly.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double tmp = 0.0;
                try {
                    Double tmpD = Double.parseDouble(avgMoneySpentWeekly.getText().toString().substring(1));
                    Double tmpMonthly = (tmpD/7) * 4.34524;
                    expectedMonthly.setText("$ "+(df.format(tmpMonthly)));
                    tmp = tmpD * 52.1429;
                    expectedYearly.setText("$ "+(df.format(tmp)));
                    DocumentReference dRef = db.collection("money")
                            .document(firebaseUser.getUid());
                    dRef.update("avgWeekly",tmpD);

                }
                catch(Exception e){

                }
                //Double tmpD = Double.parseDouble(avgMoneySpentWeekly.getText().toString());
//                Double tmpMonthly = (tmp/7) * 4.34524;
//                expectedMonthly.setText(Double.toString(tmpMonthly));
//                tmp = tmp * 52.1429;
//                expectedYearly.setText(Double.toString(tmp));
//                DocumentReference moneyReference = db.collection("money").document(firebaseUser.getUid());
//                moneyReference.get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if(task.isSuccessful()){
//                                    DocumentSnapshot document = task.getResult();
//                                    Long total =0l;
//                                    Long weeklySpent = Long.parseLong(avgMoneySpentWeekly.getText().toString());
//                                    Long startTime = System.currentTimeMillis();
//                                    if (document.exists()) {
//                                        Money money = document.toObject(Money.class);
//                                        total = money.getTotal();
//                                        startTime = money.getStartTime();
//                                    }
//                                    setMoneyInfo(total, weeklySpent,startTime);
//                                }
//
//                            }
//                        });
            }
        });
        getMoneyInfo();
        return view;
    }

    //this is just to update the totals if exactly 24 hrs has gone by and you are currently
    //on the savings page so that it updates in realtime. Probably won't ever get called.
    private void setUpdateTimer(Long currentDate) {
        Long startTime = (currentDate + 86400001) - System.currentTimeMillis();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
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

        timer.schedule(timerTask, startTime);
    }

    private void getMoneyInfo() {
        db.collection("money")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                boolean updateDB = false;
                                Money money = document.toObject(Money.class);

                                //update/set currentDate and total if its been 24hrs+
                                Long now = System.currentTimeMillis();
                                Long diff = (now - money.getCurrentDate());
                                DateFormat simple = new SimpleDateFormat("MMM dd, yyyy");
                                Long extraDays= 0l;
                                final long day = 86400000;
                                if(diff>=day){
                                    updateDB = true;
                                    extraDays = diff / day;
                                    Double totalD = (extraDays * (money.getAvgWeekly()/7)) + money.getTotal();
                                    money.setTotal(totalD);
                                    total.setText("$ "+(df.format(totalD)));

                                    //also update currentDate
                                    Long current = money.getCurrentDate() + (day*extraDays);
                                    money.setCurrentDate(current);

                                    //check weeks and weekVals
                                    Log.d("CHECK", "end of week date "+money.getEndOfWeekDate());
                                    //set end of week date
                                    while(now>money.getEndOfWeekDate().getTime()){
                                        Long tmp = money.getEndOfWeekDate().getTime() + (day*7);
                                        Date date = new Date(tmp);
                                        money.setEndOfWeekDate(date);
                                    }
                                    //set start of week date
                                    Long tmp = money.getEndOfWeekDate().getTime() - (day*7);
                                    Date date = new Date(tmp);
                                    money.setStartOfWeekDate(date);

                                    //compute weekVal and display weekTitle and weekVal
                                    extraDays = (now - money.getStartOfWeekDate().getTime()) / day;
                                    Double totalWeek = (extraDays * (money.getAvgWeekly()/7));
                                    weekTitle.setText(""+simple.format(money.getStartOfWeekDate())+" - "+
                                            simple.format(money.getEndOfWeekDate()));
                                    weekVal.setText("$ "+(df.format(totalWeek)));

                                    //get month info
                                    Calendar calendar = Calendar.getInstance();
                                    //get end of month date
                                    while(now>money.getEndOfMonthDate().getTime()){

                                        calendar.setTime(money.getEndOfMonthDate());

                                        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                                            calendar.set(Calendar.MONTH, Calendar.JANUARY);
                                            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                                        } else {
                                            calendar.roll(Calendar.MONTH, true);
                                        }
                                        money.setEndOfWeekDate(calendar.getTime());
                                    }

                                    //get start of month
                                    if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                                        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                                        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
                                    } else {
                                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1);
                                    }
                                    money.setStartOfWeekDate(calendar.getTime());

                                    extraDays = (now - money.getStartOfMonthDate().getTime()) /day;
                                    Double totalMonth = (extraDays * (money.getAvgWeekly()/7));
                                    monthTitle.setText(""+simple.format(money.getStartOfMonthDate())+" - "+
                                            simple.format(money.getEndOfMonthDate()));
                                    monthVal.setText("$ "+(df.format(totalMonth)));
                                }
                                else{
                                    //set total savings
                                    total.setText("$ "+(df.format(money.getTotal())));

                                    Double totalWeek = (extraDays * (money.getAvgWeekly()/7));
                                    weekTitle.setText(""+simple.format(money.getStartOfWeekDate())+" - "+
                                            simple.format(money.getEndOfWeekDate()));
                                    weekVal.setText("$ "+(df.format(totalWeek)));

                                    //set monthly stats unchanged
                                    extraDays = (now - money.getStartOfMonthDate().getTime()) /day;
                                    Double totalMonth = (extraDays * (money.getAvgWeekly()/7));
                                    monthTitle.setText(""+simple.format(money.getStartOfMonthDate())+" - "+
                                            simple.format(money.getEndOfMonthDate()));
                                    monthVal.setText("$ "+(df.format(totalMonth)));
                                }
//
                                setUpdateTimer(money.getCurrentDate());

                                //set start time label with start date
                                Date result = new Date(money.getStartDate());
                                startTime.setText("Total Money Saved since " + simple.format(result));

                                //set expected monthly and yearly savings
                                Double tmpD = money.getAvgWeekly();
                                Double tmpMonthly = tmpD * 4.34524;
                                expectedMonthly.setText("$ "+(df.format(tmpMonthly)));
                                tmpD = tmpD * 52.1429;
                                expectedYearly.setText("$ "+(df.format(tmpD)));

                                //set edit text box to current avgWeekly
                                avgMoneySpentWeekly.setText("$ "+(df.format(money.getAvgWeekly())));

                                if(updateDB){
                                    updateDB(money);
                                }
                            }
                        }
                    }

                    private void updateMonthGUI() {

                    }
                });
    }

    private void updateDB(Money money) {
        Map<String, Object> moneyMap = new HashMap<>();
        moneyMap.put("avgWeekly", money.getAvgWeekly());
        moneyMap.put("total", money.getTotal());
        moneyMap.put("startDate", money.getStartDate());
        moneyMap.put("currentDate", money.getCurrentDate());
        //moneyMap.put("weekVal", blank);
        moneyMap.put("startOfWeekDate", money.getStartOfWeekDate());
        moneyMap.put("endOfWeekDate", money.getEndOfWeekDate());
        //moneyMap.put("monthVal", money);
        moneyMap.put("startOfMonthDate", money.getStartOfMonthDate());
        moneyMap.put("endOfMonthDate", money.getEndOfMonthDate());
        db.collection("money")
                .document(firebaseUser.getUid())
                .set(moneyMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }


}

//ISAIAH TRYING TO DO MATH BUT SUCKING AT IT
//check if week needs to change
//                                Date endOfWeek = money.getEndOfWeekDate();
//                                Date now = new Date();
////                                if(endOfWeek.before(now)){
////                                    updateDB = true;
//////                                    Date tmp =updateWeekGUI(money.getEndOfWeekDate(), now,);
//////                                    money.setStartOfWeekDate(money.getEndOfWeekDate());
//////                                    money.setEndOfWeekDate(tmp);
////
////                                }else{
////                                    long diffWeek = now.getTime() - money.getStartOfWeekDate().getTime();//may need to be currentDate() and add it to weekVal
////                                    long diffDays = diffWeek/(24*60*60*1000);
////                                    Double tmp = diffDays * (money.getAvgWeekly()/7);
////                                    weekVal.setText("$ "+df.format(tmp));
////                                    money.setWeekVal(tmp);
////                                }
//                                //check if month needs to change
//                                Date endOfMonth = money.getEndOfMonthDate();
////                                if(endOfMonth.before(now)){
////                                    updateDB = true;
//////                                    updateMonthGUI();
//////                                    Date tmp =updateMonthGUI(money.getEndOfWeekDate(), now,);
//////                                    money.setStartOfWeekDate(money.getEndOfWeekDate());
//////                                    money.setEndOfWeekDate(tmp);
////
////                                }else{
////                                    long diffMonth = now.getTime() - money.getStartOfMonthDate().getTime();
////                                    long diffDays = diffMonth/(24*60*60*1000);
////                                    Double tmp = diffDays * (money.getAvgWeekly()/7);
////                                    monthVal.setText("$ "+df.format(tmp));
////                                    money.setMonthVal(tmp);
////                                }