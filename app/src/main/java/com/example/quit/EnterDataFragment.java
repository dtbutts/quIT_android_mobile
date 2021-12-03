package com.example.quit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EnterDataFragment extends Fragment {
    private Button btnSumbit;
    private LinearLayout lengthOfSobriety;
    private RadioButton radioYes;
    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.enter_data_frag,container, false);
        btnSumbit = view.findViewById(R.id.submit_btn);
        lengthOfSobriety = (LinearLayout) view.findViewById(R.id.lengthOfSobriety);
        radioYes = view.findViewById(R.id.radio_yes);
        lengthOfSobriety.setVisibility(View.GONE);
        radioYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lengthOfSobriety.setVisibility((View.VISIBLE));
            }
        });
        btnSumbit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                MainActivity.bottomNav.setVisibility(View.VISIBLE);
                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        });
        return view;
    }
}
