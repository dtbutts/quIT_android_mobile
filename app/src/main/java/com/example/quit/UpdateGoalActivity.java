package com.example.quit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Model.Goal;

public class UpdateGoalActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private TextView theGoal, deadline, percentComplete, currentUpdate;
    private ImageView arrowUp, arrowDown;
    private int progress;
    private ProgressBar progressBar;
    private String goalUid, measurement;
    private Button updateButton;
    private Integer current, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goal);

        theGoal = findViewById(R.id.theGoal);
        deadline = findViewById(R.id.deadline);
        percentComplete = findViewById(R.id.percentComplete);
        currentUpdate = findViewById(R.id.currentUpdate);
        arrowUp = findViewById(R.id.arrowUp);
        arrowDown = findViewById(R.id.arrowDown);
        progressBar = findViewById(R.id.progressBar1);
        updateButton = findViewById(R.id.updateButton);
        progress = 0;
        current = 0;
        total = 0;

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Intent intent = getIntent();
        goalUid = intent.getStringExtra("goalUid");
        measurement = intent.getStringExtra("measurement");
        getSpecificGoal(goalUid);

        arrowUp.setClickable(true);
        arrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress++;
                currentUpdate.setText(progress + " " +measurement);
                current++;
                progressBar.setProgress(current);
                percentComplete.setText(""+current+"/"+total +" "+measurement);
            }
        });

        arrowDown.setClickable(true);
        arrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress--;
                currentUpdate.setText(progress + " " +measurement);
                current--;
                progressBar.setProgress(current);
                percentComplete.setText(""+current+"/"+total +" "+measurement);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("COUNTs", "Current: "+current +" Total: "+total);
                if(current>=total){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateGoalActivity.this);
                    builder.setMessage("Congratulations!! You have completed your goal! Would you like" +
                            " to save or remove this goal?")
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DocumentReference documentReference = db.collection("Goals")
                                            .document(firebaseUser.getUid())
                                            .collection("Sub")
                                            .document(goalUid);
                                    Calendar cal = Calendar.getInstance();
                                    documentReference.update("deadline",cal.getTime());
                                    documentReference.update("saved", true);
                                    updateDB(goalUid);
                                    UpdateGoalActivity.this.finish();
                                }
                            })
                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.collection("Goals")
                                            .document(firebaseUser.getUid())
                                            .collection("Sub")
                                            .document(goalUid)
                                            .delete();
                                    updateDB(goalUid);
                                    UpdateGoalActivity.this.finish();
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create().show();
                }
                else{
                    updateDB(goalUid);
                    finish();
                }
            }
        });



    }

    private void updateDB(String goalUid) {
        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .document(goalUid)
                .update("current", current);
    }

    private void getSpecificGoal(String goalUid) {
        db.collection("Goals")
                .document(firebaseUser.getUid())
                .collection("Sub")
                .document(goalUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            Goal goal = document.toObject(Goal.class);
                            theGoal.setText(goal.getTheGoal());

                            current = goal.getCurrent();
                            total = goal.getTotalNeeded();

                            String myFormat="MM/dd/yy";
                            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                            if(goal.getDeadline()!=null){
                                deadline.setText("Deadline: "+dateFormat.format(goal.getDeadline()));
                            }
                            else{
                                deadline.setVisibility(View.GONE);
                            }
                            percentComplete.setText(""+current+"/"+total +" "+goal.getMeasurement());
                            progressBar.setProgress(current);
                            progressBar.setMax(total);

                        }
                    }
                });
    }

}
