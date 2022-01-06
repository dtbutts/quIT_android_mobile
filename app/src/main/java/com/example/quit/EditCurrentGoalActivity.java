package com.example.quit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Model.Goal;

public class EditCurrentGoalActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText current, total, theGoal;
    private String measurement, goalUid;
    private TextView deadline;
    private Button addGoal;
    boolean choseDeadline;
    private ArrayAdapter<CharSequence> arrayAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    FirebaseFirestore db;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        total = findViewById(R.id.totalEdit);
        current = findViewById(R.id.currentEdit);
        deadline = findViewById(R.id.deadlineEdit);
        addGoal = findViewById(R.id.submitGoal);
        theGoal = findViewById(R.id.theGoal);
        measurement = "Hours";
        choseDeadline = false;

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        spinner = (Spinner) findViewById(R.id.measurements_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.measurements_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.getItemAtPosition(i);
                measurement = (String) adapterView.getItemAtPosition(i);
                total.setHint(""+adapterView.getItemAtPosition(i));
                current.setHint(""+ adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();
        goalUid = intent.getStringExtra("goaluid");
        getCurrentInfo(goalUid);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        deadline.setClickable(true);
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditCurrentGoalActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitGoalInfo(goalUid);
            }
        });

    }

    private void getCurrentInfo(String goalUid) {
        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .document(goalUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Goal goal = task.getResult().toObject(Goal.class);
                            theGoal.setText(goal.getTheGoal());
                            total.setText(goal.getTotalNeeded().toString());
                            current.setText(goal.getCurrent().toString());
                            if(goal.getDeadline()==null)
                            {
                                deadline.setText("");
                            }
                            else{
                                String myFormat="MM/dd/yy";
                                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                                deadline.setText(dateFormat.format(goal.getDeadline()));
                            }
                            int pos =findSpinnerPosition(goal.getMeasurement());
                            if(pos!=-1){
                                spinner.setSelection(pos);
                            }
                        }
                    }
                });
    }

    private int findSpinnerPosition(String measurement) {
        if(measurement.equals("Hours")){
            return 0;
        }
        else if(measurement.equals("Days"))
        {
            return 1;
        }
        else if(measurement.equals("Weeks"))
        {
            return 2;
        }
        else if(measurement.equals("Times"))
        {
            return 3;
        }
        else if(measurement.equals("Lbs"))
        {
            return 4;
        }
        else if(measurement.equals("Miles"))
        {
            return 5;
        }

        return -1;
    }

    private void submitGoalInfo(String goalUid) {
        String TheGoal = theGoal.getText().toString();
        String Total = total.getText().toString();
        String Current = current.getText().toString();
        if(measurement.isEmpty()|| Total.isEmpty()||
                TheGoal.isEmpty()||Current.isEmpty()){
            Toast.makeText(getApplicationContext(), "Make sure to fill out all required (*) fields", Toast.LENGTH_LONG).show();
            return;
        }
        Integer totalInt =0;
        Integer currentInt = 0;
        try{
            totalInt = Integer.parseInt(Total);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "You did not choose an appropriate number for total!", Toast.LENGTH_LONG).show();
            return;
        }
        try{
            currentInt = Integer.parseInt(Current);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "You did not choose an appropriate number for current!", Toast.LENGTH_LONG).show();
            return;
        }
        Date date = new Date();
        if(!choseDeadline){
            date = null;
        }
        else{
            date = myCalendar.getTime();
        }
        Long Timestamp = System.currentTimeMillis();
        Map<String, Object> goal = new HashMap<>();
        goal.put("theGoal", TheGoal);
        goal.put("measurement", measurement);
        goal.put("totalNeeded", totalInt);
        goal.put("current", currentInt);
        goal.put("deadline", date);
        goal.put("timestamp", Timestamp);
        goal.put("goalUid", goalUid);
        goal.put("saved", false);

        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .document(goalUid)
                .set(goal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
        EditCurrentGoalActivity.this.finish();
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        deadline.setText(dateFormat.format(myCalendar.getTime()));
        choseDeadline=true;
    }
}