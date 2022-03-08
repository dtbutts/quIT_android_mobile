package com.example.quit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Currency;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Model.Money;
import me.abhinay.input.CurrencyEditText;

public class MoneyFragment extends Fragment {
    CurrencyEditText avgMoneySpentWeekly;

    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    TextView total, expectedMonthly, expectedYearly, startTime, monthVal, weekVal,
            weekTitle, monthTitle;
    TimerTask timerTask;
    Timer timer;
    Button pause, resetMoney;
    LinearLayout wholeShabang;
    Activity activity;
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
        wholeShabang = view.findViewById(R.id.wholeShabang);
        wholeShabang.setVisibility(View.GONE);
        resetMoney = view.findViewById(R.id.resetMoney);


        //toolbar set up
        Toolbar toolbar = view.findViewById(R.id.moneyToolbar);
        activity = getActivity();
        if(activity!=null){
            ((AppCompatActivity)activity).setSupportActionBar(toolbar);

        }

//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
//            }
//        });

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        resetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                resetMoney();
                                getMoneyInfo();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to reset money saver?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        avgMoneySpentWeekly.setCurrency("$");
        avgMoneySpentWeekly.setDelimiter(false);
        avgMoneySpentWeekly.setSpacing(false);
        avgMoneySpentWeekly.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        avgMoneySpentWeekly.setSeparator(".");
        avgMoneySpentWeekly.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                        if ((i == EditorInfo.IME_ACTION_DONE)) {
                            // the user is done typing.
                            Double tmp = 0.0;
                            try{
                                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                                Number number = format.parse(avgMoneySpentWeekly.getText().toString().substring(1));
                                Double tmpD = number.doubleValue();
//                                Double tmpD = Double.parseDouble(number);
                                //Double tmpMonthly = (tmpD/7) * 4.34524;
                                DocumentReference dRef = db.collection("money")
                                        .document(firebaseUser.getUid());
                                dRef.update("avgWeekly",tmpD);
                                Double tmpMonthly = tmpD * 4.34524;
                                expectedMonthly.setText("$ "+(df.format(tmpMonthly)));
                                tmp = tmpD * 52.1429;
                                expectedYearly.setText("$ "+(df.format(tmp)));

                                getMoneyInfo();
                                InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                            }catch (Exception e){
                                Toast.makeText(getContext(), "enter number value only", Toast.LENGTH_SHORT).show();
                            }

                            return true; // consume.
                        }
                        else if(i == KeyEvent.KEYCODE_BACK){
                            return false;
                        }
                        return false;
                    }

                }
        );

        avgMoneySpentWeekly.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            String current = "";
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                  if(!charSequence.toString().equals(current)){
//       avgMoneySpentWeekly.removeTextChangedListener(this);
//
//       String cleanString = charSequence.toString().replaceAll("[$,.]", "");
//
//       double parsed = Double.parseDouble(cleanString);
//       String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
//
//       current = formatted;
//       avgMoneySpentWeekly.setText(formatted);
//       avgMoneySpentWeekly.setSelection(formatted.length());
//
//       avgMoneySpentWeekly.addTextChangedListener(this);
//    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Double tmp = 0.0;
//                try {
//
//                    Double tmpD = Double.parseDouble(avgMoneySpentWeekly.getText().toString().substring(1));
//                    //Double tmpMonthly = (tmpD/7) * 4.34524;
//                    DocumentReference dRef = db.collection("money")
//                            .document(firebaseUser.getUid());
//                    dRef.update("avgWeekly", tmpD);
//                    Double tmpMonthly = tmpD * 4.34524;
//                    expectedMonthly.setText("$ " + (df.format(tmpMonthly)));
//                    tmp = tmpD * 52.1429;
//                    expectedYearly.setText("$ " + (df.format(tmp)));
//
//                    //getMoneyInfo();
//
//                } catch (Exception e) {
//
//                }
            }
            });
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
           // }
       // });
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
                activity = getActivity();
                if(activity!=null){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
                }
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
                                    Log.d("NOT WORKING", "endOfWeekDate.getTime() "+money.getEndOfWeekDate().getTime());
                                    Log.d("NOT WORKING", "endOfWeekDate "+money.getEndOfWeekDate());
                                    Log.d("NOT WORKING", "long "+tmp);
                                    Date date = new Date(tmp);
                                    Log.d("NOT WORKING", "date "+date);
                                    money.setStartOfWeekDate(date);

                                    //compute weekVal and display weekTitle and weekVal
                                    extraDays = (now - money.getStartOfWeekDate().getTime()) / day;
                                    Double totalWeek = (extraDays * (money.getAvgWeekly()/7));
                                    weekTitle.setText(""+simple.format(money.getStartOfWeekDate())+" - "+
                                            simple.format(money.getEndOfWeekDate()));
                                    weekVal.setText("$ "+(df.format(totalWeek)));

                                    //get month info
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(money.getEndOfMonthDate());
                                    //get end of month date
                                    while(now>money.getEndOfMonthDate().getTime()){

//                                        calendar.setTime(money.getEndOfMonthDate());

                                        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                                            calendar.set(Calendar.MONTH, Calendar.JANUARY);
                                            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                                        } else {
                                            calendar.roll(Calendar.MONTH, true);
                                        }
                                        money.setEndOfMonthDate(calendar.getTime());
                                    }

                                    //get start of month
                                    if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                                        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                                        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
                                    } else {
                                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1);
                                    }
                                    money.setStartOfMonthDate(calendar.getTime());

                                    extraDays = (now - money.getStartOfMonthDate().getTime()) /day;
                                    Double totalMonth = (extraDays * (money.getAvgWeekly()/7));
                                    monthTitle.setText(""+simple.format(money.getStartOfMonthDate())+" - "+
                                            simple.format(money.getEndOfMonthDate()));
                                    monthVal.setText("$ "+(df.format(totalMonth)));
                                }
                                else{
                                    //set total savings
                                    //total.setText("$ "+(df.format(money.getTotal())));
                                    Double tmpTotal = ((now - money.getStartDate())/day) * (money.getAvgWeekly()/7);
                                    total.setText("$ "+(df.format(tmpTotal)));
                                    money.setTotal(tmpTotal);

                                    //set weekly stats
                                    extraDays = (now - money.getStartOfWeekDate().getTime()) /day;
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
                                //avgMoneySpentWeekly.setText(money.getAvgWeekly().toString());
                                System.out.println();
                                if(updateDB){
                                    updateDB(money);
                                }
                                wholeShabang.setVisibility(View.VISIBLE);
                            }
                        }
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

    private void resetMoney() {

        Map<String, Object> money = new HashMap<>();
        Double tmp = 0.0;

        Date date = new Date();
        Double blank = 0.0;
        Long start = System.currentTimeMillis();
        money.put("avgWeekly", tmp);
        money.put("total", blank);
        money.put("startDate", start);
        money.put("currentDate", start);
        //money.put("weekVal", blank);
        money.put("startOfWeekDate", date);
        money.put("endOfWeekDate", getNextWeek(start));
        //money.put("monthVal", blank);
        money.put("startOfMonthDate", date);
        money.put("endOfMonthDate", getNextMonth(date));

        db.collection("money")
                .document(firebaseUser.getUid())
                .set(money)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //do nothing extra
                    }
                });
    }

    public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        } else {
        calendar.roll(Calendar.MONTH, true);
        }

        return calendar.getTime();
    }
    public static Date getNextWeek(Long nowMS) {
        nowMS = (86400000 *7)+ nowMS;
        Date date = new Date(nowMS);
        return date;
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