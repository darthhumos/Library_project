package com.tmd.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendNewMessage extends AppCompatActivity {

    private String from;
    private String to;
    private String subject;
    private String msg;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private TextView tv;
    private String id;
    private Message ms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_message);
        Bundle bun = getIntent().getExtras();
        if(bun!=null)
        {
            id = bun.get("ID").toString();
            ms = (Message)bun.get("Message");

        }
        Button btnSend = (Button) findViewById(R.id.SendNewMessage_btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = MainActivity.getuser();
                tv = (TextView) findViewById(R.id.SendNewMessage_label_to);
                from = user.getName()+" "+user.getLast_Name();
                tv.setText(from);

                Spinner mySpinner=(Spinner) findViewById(R.id.SendNewMessage_spinner);
                to = mySpinner.getSelectedItem().toString();

                EditText sub = (EditText)findViewById(R.id.SendNewMessage_subject);
                subject = sub.getText().toString();

                EditText _msg = (EditText) findViewById(R.id.SendNewMessage_msg);
                msg = "";
                msg =  _msg.getText().toString();
             if ((subject.length() < 3 )|| (msg.length() < 3)) {
                 Toast.makeText(getApplicationContext(), "The message/subject is too short!", Toast.LENGTH_SHORT).show();//add msg to DB
             }
             else{
                 //add
                 try {
                 ref = database.getInstance().getReference().child("Messages");

                 String MessageId = ref.push().getKey();
                     if(id!=null)
                     {
                         MessageId = id;
                         ref = ref.child(MessageId);
                         ref = ref.child("answer");
                         ref.setValue(msg);
                         ref = database.getInstance().getReference().child("Messages").child(MessageId).child("read");
                         ref.setValue(false);
                     }
                     else {
                         ref = ref.child(MessageId);
                         Message newMessage = new Message(from, to, subject, msg, MessageId);
                         ref.setValue(newMessage);
                     }
                     Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                 }
                 catch(Exception ex)
                 {
                     Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                 }
                 //clearAllfields();
                 Intent I = new Intent(SendNewMessage.this,Messages.class);
                 finish();
                 startActivity(I);
             }
            }
        });

    }
    @Deprecated
    private void clearAllfields(){
        /*
        et = (EditText) findViewById(R.id.SendNewMessage_subject);
        et.setText("");
        et = (EditText) findViewById(R.id.SendNewMessage_msg);
        et.setText("Please insert your message here");*/
    }
}
