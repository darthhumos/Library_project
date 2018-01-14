package com.tmd.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class admindetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindetails);
        Button b = (Button)findViewById(R.id.admindetails_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)findViewById(R.id.Admindetails_email)).getText().toString();
                String Password = ((EditText)findViewById(R.id.Admindetails_password)).getText().toString();
                MainActivity.setAdmin(email,Password);
                finish();
            }
        });
        Button back = (Button)findViewById(R.id.admindetails_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
