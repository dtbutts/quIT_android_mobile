package com.example.quit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//THIS IS NO LONGER USED!!!!!!!!!!!!!
public class EnterDataFragment extends Fragment {
    private Button btnSubmit;
    private EditText email, username,password,age, height, weight, moneySpent,dayOfAddiction, dayOfSobriety;
    private RelativeLayout lengthOfSobriety;
    private RadioButton radioYes;
    private CheckBox checkBox;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

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
        View view = inflater.inflate(R.layout.enter_data_frag,container, false);
        btnSubmit = view.findViewById(R.id.submit_btn);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        lengthOfSobriety = (RelativeLayout) view.findViewById(R.id.lengthOfSobriety);
        radioYes = view.findViewById(R.id.radio_yes);
        lengthOfSobriety.setVisibility(View.GONE);
        email = view.findViewById(R.id.email);
        username = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.password);
        age = view.findViewById(R.id.age);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        moneySpent = view.findViewById(R.id.moneySpent);
        dayOfAddiction = view.findViewById(R.id.daysOfAddiction);
        dayOfSobriety = view.findViewById(R.id.daysOfSobriety);
        checkBox = view.findViewById(R.id.termsConditions);

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
                    Toast.makeText(mActivity, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
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
                if(Username.isEmpty() || Email.isEmpty() ||Password.isEmpty() ||
                        Age.isEmpty() ||Height.isEmpty() ||Weight.isEmpty() ||
                        Money.isEmpty() ||DaysOfAddicted.isEmpty() ){
                    //makeToast (required)
                    Toast.makeText(mActivity, "Make sure to fill out all required (*) fields", Toast.LENGTH_LONG).show();
                    return;
                    //return;
                }
                Long TotalTimeSober = 0L;
                Boolean ButtonPressed = false;
                Long LastEndTime = 0L;
                if(!DaysOfSobriety.equals("")) {
                    try {
                        Integer tmp = Integer.parseInt(DaysOfSobriety);
                        if(tmp>0){
                            TotalTimeSober = TimeUnit.DAYS.toMillis(Long.parseLong(DaysOfSobriety));
                            ButtonPressed = true;
                            LastEndTime = System.currentTimeMillis();
                        }
                    }
                    catch (Exception e){

                    }
                }
                Long Timestamp = System.currentTimeMillis();
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
                userAccount.put("imageUri", "");

                //userAccount.put("timeSober", DaysOfSobriety);

                checkIfAccountAlreadyExists(Username, db,userAccount);

//                if(existsVal!=-1){
//                        MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
//                        MainActivity.bottomNav.setVisibility(View.VISIBLE);
//
//                        //uncomment below code to allow create account to only show up for first time
//                        //SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                        //SharedPreferences.Editor editor = prefs.edit();
//                        //editor.putBoolean("firstStart", false);
//                        //editor.apply();
//
//                }
//                else {
//                    return;
//                }
//                if(existsVal==1){
//
//                }
//                else{
//                    db.collection("userAccount")
//                            .add(userAccount)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(mActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getActivity(), "Account Error", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }

//                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
//                MainActivity.bottomNav.setVisibility(View.VISIBLE);
//                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean("firstStart", false);
//                editor.apply();
            }
        });
        return view;
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
                            Toast.makeText(mActivity, "username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //checking if task contains any payload. if no, then update
                if (task.getResult().size() == 0){
                    try{

                        Log.d("UsernameCheck", "onComplete: MATCH NOT FOUND - username is available");
                        Toast.makeText(mActivity, "username changed", Toast.LENGTH_SHORT).show();

                        //create authorization of user for retrieving current user later on
                        mAuth.createUserWithEmailAndPassword((String)userAccount.get("email"), (String) userAccount.get("password"))
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(mActivity, "auth worked", Toast.LENGTH_LONG).show();
                                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                            String userid = firebaseUser.getUid();

                                            db.collection("userAccount")
                                                    .document(userid)
                                                    .set(userAccount)
                                                    .addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(mActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                                                            MainActivity.bottomNav.setVisibility(View.VISIBLE);
                                                            //uncomment below code to allow create account to only show up for first time
                                                            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = prefs.edit();
                                                            editor.putBoolean("firstStart", false);
                                                            editor.apply();
                                                        }

//                                                        @Override
//                                                        public void onSuccess(DocumentReference documentReference) {
//
//                                                            Toast.makeText(mActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }).addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(mActivity, "Account Error", Toast.LENGTH_SHORT).show();
//                                                        }
                                            });

                                        }
                                        else{
                                           // Toast.makeText(mActivity, "Incorrect email or password", Toast.LENGTH_LONG).show();
                                            Toast.makeText(mActivity, "Incorrect email or password", Toast.LENGTH_LONG).show();
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
}
