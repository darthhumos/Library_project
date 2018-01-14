package com.tmd.library;

/**
 * Created by Tomer on 22-Nov-17.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Activity;
import android.widget.Spinner;

import java.util.ArrayList;

public class MenuFrag extends Fragment{
    Button b;
    Spinner spin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,container,false);
        spin = (Spinner)view.findViewById(R.id.spinner);
        ArrayList<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Menu");
        spinnerArray.add("Browse Book");
        spinnerArray.add("Loaned Book");
        spinnerArray.add("Profile");
        spinnerArray.add("Settings");
        spinnerArray.add("About");
        spinnerArray.add("Send messages");
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 5)
                {
                   Intent i = new Intent(getActivity(),About.class);

                    startActivity(i);
                }
                if(position == 3)
                {
                    Intent i = new Intent(getActivity(),Profile.class);

                    startActivity(i);
                }
                if(position == 4)
                {
                    Intent i = new Intent(getActivity(),Settings.class);

                    startActivity(i);
                }
                if(position == 1)
                {
                    Intent i = new Intent(getActivity(),Browse.class);
                    startActivity(i);
                }
                if(position == 2)
                {
                    Intent i = new Intent(getActivity(),Loanedbooks.class);

                    startActivity(i);
                }
                if(position == 6)
                {
                    Intent i = new Intent(getActivity(),SendNewMessage.class);
                    startActivity(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        return view;


    }
}
