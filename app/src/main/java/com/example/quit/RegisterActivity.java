package com.example.quit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText email, username,password,age, height, weight, moneySpent,dayOfAddiction, dayOfSobriety;
    private RelativeLayout lengthOfSobriety;
    private TextView alreadyUser;
    private RadioButton radioYes;
    private CheckBox checkBox;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSubmit = findViewById(R.id.submit_btn);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        lengthOfSobriety = (RelativeLayout) findViewById(R.id.lengthOfSobriety);
        radioYes = findViewById(R.id.radio_yes);
        lengthOfSobriety.setVisibility(View.GONE);
        email = findViewById(R.id.email);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        moneySpent = findViewById(R.id.moneySpent);
        dayOfAddiction = findViewById(R.id.daysOfAddiction);
        dayOfSobriety = findViewById(R.id.daysOfSobriety);
        checkBox = findViewById(R.id.termsConditions);
        alreadyUser = findViewById(R.id.alreadyUser);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dayOfSobriety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        alreadyUser.setClickable(true);
        alreadyUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

        //if the person is already sober, then show the edit text boxes for length of sobriety
        radioYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lengthOfSobriety.setVisibility((View.VISIBLE));
            }
        });
        //after submit button is clicked, replace the frame with the bottom navigation and
        // Home fragment. Also sets the first Start to false so that create account doesn't
        //come up again.
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(), "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Email = email.getText().toString();
                String Username = username.getText().toString();

                String Password = password.getText().toString();
                String Age = age.getText().toString();
                String Height = height.getText().toString();
                String Weight = weight.getText().toString();
                String Money = moneySpent.getText().toString();
                String DaysOfAddicted = dayOfAddiction.getText().toString();
                String DaysOfSobriety = dayOfSobriety.getText().toString();
                Date date = null;

                if(Username.isEmpty() || Email.isEmpty() ||Password.isEmpty() ||
                        Age.isEmpty() ||Height.isEmpty() ||Weight.isEmpty() ||
                        Money.isEmpty() ||DaysOfAddicted.isEmpty() ){
                    //makeToast (required)
                    Toast.makeText(getApplicationContext(), "Make sure to fill out all required (*) fields", Toast.LENGTH_LONG).show();
                    return;
                    //return;
                }
                Long TotalTimeSober = 0L;
                Boolean ButtonPressed = false;
                Long LastEndTime = 0L;
                if(!DaysOfSobriety.equals("") && myCalendar!=null) {
                    ButtonPressed = true;
                    TotalTimeSober = System.currentTimeMillis() - myCalendar.getTime().getTime();
                    LastEndTime = System.currentTimeMillis();
                    date = myCalendar.getTime();
//                    try {
//                        Integer tmp = Integer.parseInt(DaysOfSobriety);
//                        if(tmp>0){
//                            try{
//                                TotalTimeSober = TimeUnit.DAYS.toMillis(Long.parseLong(DaysOfSobriety));
//
//                                ButtonPressed = true;
//                                LastEndTime = System.currentTimeMillis();
//                            }catch (Exception e){
//
//                            }
//                        }
//                    }
//                    catch (Exception e){
//
//                    }
                }




                Map<String, Object> userAccount = new HashMap<>();
                userAccount.put("email", Email);
                userAccount.put("password", Password);
                userAccount.put("username", Username);
                userAccount.put("age", Age);
                userAccount.put("height", Height);
                userAccount.put("weight", Weight);
                userAccount.put("moneySpent", Money);
                userAccount.put("timeAddicted", DaysOfAddicted);
                userAccount.put("totalTimeSober", TotalTimeSober);
                userAccount.put("lastEndTime", LastEndTime);
                userAccount.put("buttonPressed", ButtonPressed);
                userAccount.put("imageUri", "https://firebasestorage.googleapis.com/v0/b/quit-a645b.appspot.com/o/profile-icon-png-898.png?alt=media&token=a6f5dcb6-722a-4ac3-b89a-0a0823a0ed5e");
                userAccount.put("soberSince", date);
                //userAccount.put("timeSober", DaysOfSobriety);

                checkIfAccountAlreadyExists(Username, db,userAccount);

            }
        });
    }

    //checks if an account with the current username already exists. If it does not exist,
    //it adds the account to the database. Else return -1
    private void checkIfAccountAlreadyExists(final String usernameToCompare, FirebaseFirestore db,Map<String, Object> userAccount){
        final Query mQuery = db.collection("userAccount").whereEqualTo("username", usernameToCompare);
        mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("UsernameCheck", "checkingIfusernameExist: checking if username exists");

                if (task.isSuccessful()){
                    for (DocumentSnapshot ds: task.getResult()){
                        String userNames = ds.getString("username");
                        if (userNames.equals(usernameToCompare)) {
                            Log.d("UsernameCheck", "checkingIfusernameExist: FOUND A MATCH -username already exists");
                            Toast.makeText(getApplicationContext(), "username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //checking if task contains any payload. if no, then update
                if (task.getResult().size() == 0){
                    try{

                        Log.d("UsernameCheck", "onComplete: MATCH NOT FOUND - username is available");
                        Toast.makeText(getApplicationContext(), "username changed", Toast.LENGTH_SHORT).show();

                        //create authorization of user for retrieving current user later on
                        mAuth.createUserWithEmailAndPassword((String)userAccount.get("email"), (String) userAccount.get("password"))
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                               if(task.isSuccessful()){
                                                                   Toast.makeText(getApplicationContext(), "auth worked", Toast.LENGTH_LONG).show();
                                                                   FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                                   String userid = firebaseUser.getUid();

                                                                   db.collection("userAccount")
                                                                           .document(userid)
                                                                           .set(userAccount)
                                                                           .addOnSuccessListener(new OnSuccessListener() {
                                                                               @Override
                                                                               public void onSuccess(Object o) {
                                                                                   Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                                                   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                   startActivity(intent);
                                                                                   //finish();
//                                                                                   MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
//                                                                                   MainActivity.bottomNav.setVisibility(View.VISIBLE);
                                                                                   //uncomment below code to allow create account to only show up for first time
//                                                                                   SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                                                                                   SharedPreferences.Editor editor = prefs.edit();
//                                                                                   editor.putBoolean("firstStart", false);
//                                                                                   editor.apply();
                                                                               }

                                                                           });

                                                               }
                                                               else{
                                                                   // Toast.makeText(mActivity, "Incorrect email or password", Toast.LENGTH_LONG).show();
                                                                   Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_LONG).show();
                                                               }
                                                           }
                                                       }
                                );
                    }catch (NullPointerException e){
                        Log.e("UsernameCheck", "NullPointerException: " + e.getMessage() );
                    }
                }
            }
        });
        return;
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dayOfSobriety.setText(dateFormat.format(myCalendar.getTime()));
    }
}
