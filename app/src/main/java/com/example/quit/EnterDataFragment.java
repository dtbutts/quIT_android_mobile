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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EnterDataFragment extends Fragment {
    private Button btnSubmit;
    private EditText username,age, height, weight, moneySpent,addictedYear, addictedMonth, addictedDay,
    soberYear, soberMonth, soberDay;
    private LinearLayout lengthOfSobriety;
    private RadioButton radioYes;
    private CheckBox checkBox;
    FirebaseFirestore db;
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
        lengthOfSobriety = (LinearLayout) view.findViewById(R.id.lengthOfSobriety);
        radioYes = view.findViewById(R.id.radio_yes);
        lengthOfSobriety.setVisibility(View.GONE);
        username = view.findViewById(R.id.userName);
        age = view.findViewById(R.id.age);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        moneySpent = view.findViewById(R.id.moneySpent);
        addictedYear= view.findViewById(R.id.yearOfAddiction);
        addictedMonth = view.findViewById(R.id.monthOfAddiction);
        addictedDay = view.findViewById(R.id.dayOfAddiction);
        soberYear = view.findViewById(R.id.yearOfSobriety);
        soberMonth = view.findViewById(R.id.monthOfSobriety);
        soberDay = view.findViewById(R.id.dayOfSobriety);
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
                String Username = username.getText().toString();
                if(Username==null){
                    //makeToast (required)
                    //return;
                }
                String Age = age.getText().toString();
                String Height = height.getText().toString();
                String Weight = weight.getText().toString();
                String Money = moneySpent.getText().toString();
                String AddictedYear = addictedYear.getText().toString();
                String AddictedMonth = addictedMonth.getText().toString();
                String AddictedDay = addictedDay.getText().toString();
                int addictedDayInt=0, addictedMonthInt=0, addictedYearInt=0;
                try{
                    addictedYearInt = Integer.parseInt(AddictedYear);
                }catch(NumberFormatException ex){

                }
                try{
                    addictedMonthInt = Integer.parseInt(AddictedMonth);
                }catch(NumberFormatException ex){

                }
                try{
                    addictedDayInt = Integer.parseInt(AddictedDay);
                }catch(NumberFormatException ex){

                }

                String TotalAddicted = String.valueOf(addictedDayInt+addictedMonthInt+addictedYearInt);

                String SoberYear = soberYear.getText().toString();
                String SoberMonth = soberMonth.getText().toString();
                String SoberDay = soberDay.getText().toString();
                int soberYearInt=0, soberMonthInt=0,soberDayInt=0;
                try{
                    soberYearInt = Integer.parseInt(SoberYear);
                }catch(NumberFormatException ex){

                }
                try{
                    soberMonthInt = Integer.parseInt(SoberMonth);
                }catch(NumberFormatException ex){

                }
                try{
                    soberDayInt = Integer.parseInt(SoberDay);
                }catch(NumberFormatException ex) {

                }
                int totalSober = (soberYearInt*31536000)+(soberDayInt*86400);
                String TotalSober = String.valueOf(soberDayInt+soberMonthInt+soberYearInt);
                Map<String, Object> userAccount = new HashMap<>();
                userAccount.put("Username", Username);
                userAccount.put("Age", Age);
                userAccount.put("Height", Height);
                userAccount.put("Weight", Weight);
                userAccount.put("Money Spent", Money);
                userAccount.put("Total Time Addicted", TotalAddicted);
                userAccount.put("Initial Total Time Sober", TotalSober);

                int existsVal=checkIfAccountAlreadyExists(Username, db,userAccount);
                Log.d("EXISTSVAL", String.valueOf(existsVal));
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
    private int checkIfAccountAlreadyExists(final String usernameToCompare, FirebaseFirestore db,Map<String, Object> userAccount){
        final Query mQuery = db.collection("userAccount").whereEqualTo("Username", usernameToCompare);
        final int[] returnVal = {-1};
        mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("UsernameCheck", "checkingIfusernameExist: checking if username exists");

                if (task.isSuccessful()){
                    for (DocumentSnapshot ds: task.getResult()){
                        String userNames = ds.getString("Username");
                        if (userNames.equals(usernameToCompare)) {
                            Log.d("UsernameCheck", "checkingIfusernameExist: FOUND A MATCH -username already exists");
                            returnVal[0] =1;
                            Toast.makeText(mActivity, "username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //checking if task contains any payload. if no, then update
                if (task.getResult().size() == 0){
                    try{

                        Log.d("UsernameCheck", "onComplete: MATCH NOT FOUND - username is available");
                        returnVal[0] =0;
                        Toast.makeText(mActivity, "username changed", Toast.LENGTH_SHORT).show();
                        db.collection("userAccount")
                                .add(userAccount)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(mActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mActivity, "Account Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                        MainActivity.bottomNav.setVisibility(View.VISIBLE);

                        //uncomment below code to allow create account to only show up for first time
                        //SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        //SharedPreferences.Editor editor = prefs.edit();
                        //editor.putBoolean("firstStart", false);
                        //editor.apply();

                    }catch (NullPointerException e){
                        Log.e("UsernameCheck", "NullPointerException: " + e.getMessage() );
                    }
                }
            }
        });
        return returnVal[0];
    }
}
