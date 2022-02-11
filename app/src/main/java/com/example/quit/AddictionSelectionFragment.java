package com.example.quit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class AddictionSelectionFragment extends Fragment {

    //variables here
    //private Button recalculate;


    //required empty public constructor
    public AddictionSelectionFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.addiction_selection_frag, container, false);

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


