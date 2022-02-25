package com.example.quit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class AddictionSelectionFragment extends Fragment {

    //variables here
    private Button nicotine;
    private Button alcohol;
    private Button caffiene;

    //database variables
    FirebaseFirestore db;
    FirebaseAuth mAuth;



    //required empty public constructor
    public AddictionSelectionFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.addiction_selection_frag, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //instantiate fields from xml
        nicotine = theView.findViewById(R.id.nicotineButton);
        alcohol = theView.findViewById(R.id.alcoholButton);
        caffiene = theView.findViewById(R.id.caffeineButton);

        //get path to addictionType variable for this user
        DocumentReference dRef = db.collection("userAccount").document(firebaseUser.getUid());



        // LISTEN FOR A CLICK!!!!!!!!!!!!!
        nicotine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update chosen addiction in database
                dRef.update("addictionType", "n");

                //update the variable in the MainActivity class
                ((MainActivity)getActivity()).updateAddictionVariable();

                //switch to that page
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HealthFragNicotine()).commit();
            }
        });

        //all following buttons to the same things for each addiction type
        alcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dRef.update("addictionType", "a");

                ((MainActivity)getActivity()).updateAddictionVariable();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HealthFragAlcohol()).commit();
            }
        });

        caffiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dRef.update("addictionType", "c");

                ((MainActivity)getActivity()).updateAddictionVariable();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HealthFragCaffiene()).commit();
            }
        });


        return theView;

    }


}


