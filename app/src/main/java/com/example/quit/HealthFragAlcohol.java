package com.example.quit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class HealthFragAlcohol extends Fragment{

    //variables here
    //private Button recalculate;


    //required empty public constructor
    public HealthFragAlcohol(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.health_frag_alcohol, container, false);

        //instantiate fields from xml
        // recalculate = theView.findViewById(R.id.recalculate);

        // LISTEN FOR A CLICK!!!!!!!!!!!!!
//        recalculate.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(recalculateButtonListener != null){
//                    recalculateButtonListener.onRecalculateButtonListener();
//                }
//            }
//        });


        return theView;

    }

}


