package com.example.quit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.User;

public class StartActivity extends AppCompatActivity {
    EditText email, password;
    TextView newUser;
    Button login;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //redirect to home frag if not null
        if(firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        newUser = findViewById(R.id.newUser);
        login = findViewById(R.id.loginBtn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        newUser.setClickable(true);
        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if(Email.isEmpty() || Password.isEmpty()){
                    //makeToast (required)
                    Toast.makeText(getApplicationContext(), "Make sure to fill out all required (*) fields", Toast.LENGTH_LONG).show();
                    return;
                    //return;
                }
                else{
                    mAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        db.collection("userAccount")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            // Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
