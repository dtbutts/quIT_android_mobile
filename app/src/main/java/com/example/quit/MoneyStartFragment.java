package com.example.quit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoneyStartFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    EditText edit;
    Button start;
    TextView message;
    RelativeLayout editLayout;
    LinearLayout wholeShambo;

    @Override
    public void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
//        start.setVisibility(View.INVISIBLE);
//        editLayout.setVisibility(View.INVISIBLE);
//        message.setVisibility(View.INVISIBLE);
        wholeShambo.setVisibility(View.INVISIBLE);



        db.collection("money")
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists())
                    {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
                    }
                    else{
                        wholeShambo.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.money_start_frag,container, false);

        edit = view.findViewById(R.id.editStart);
        start = view.findViewById(R.id.startBtn);
        message = view.findViewById(R.id.message);
        editLayout = view.findViewById(R.id.editLayout);
        wholeShambo = view.findViewById(R.id.wholeShambo);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Edit = edit.getText().toString();
                if(Edit.isEmpty()){
                    Toast.makeText(getActivity(), "Choose an average value of money spent each week", Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> money = new HashMap<>();
                Double tmp = 0.0;
                Edit = Edit.substring(1);
                try {
                    tmp = Double.valueOf(Edit);
                }catch (Exception e){

                }
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
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MoneyFragment()).commit();
                            }
                        });
            }
        });

        return view;
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
