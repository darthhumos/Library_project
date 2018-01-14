package com.tmd.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Messages extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ArrayList<String> SubjectArray;
    private HashMap<String,Message> MessagesArray;
    private ArrayAdapter<String> adapter;
    String myUserName;
    ListView list;
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
         list = (ListView) findViewById(R.id.Messages_listview);
        User user = MainActivity.getuser();
        myUserName = user.getName()+" "+user.getLast_Name();
        SubjectArray = new ArrayList<>();
        MessagesArray = new HashMap<>();
        getMessages();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sub  = String.valueOf(adapterView.getItemAtPosition(i));
                Message m = MessagesArray.get(sub);
                myRef = database.getReference("Messages");
                myRef = myRef.child(m.getKey());
                if(m.getTo().equals(myUserName))
                m.setRead(true);
                myRef.setValue(m);
                descriptionDialog(m,sub,m.getAnswer(),m.getMessage());
            }
        });

        Button btnSend = (Button) findViewById(R.id.Message_btn_newMessage);
        btnSend.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View V) {
                        Intent I = new Intent(Messages.this,SendNewMessage.class);
                        startActivity(I);
                        finish();
                    }
                }
        );
    }


    private void getMessages() {
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        myRef = database.getReference("Messages");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                Log.d("enter","firebase connected");
                AddMassage(data);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                Log.d("enter","failed");
            }
        });
    }

    private void AddMassage(DataSnapshot data) {
        if(adapter!=null) {
            index = 0;
            MessagesArray.clear();
            adapter.notifyDataSetChanged();
            ArrayList<String> temp = new ArrayList<>();
            for (DataSnapshot ds : data.getChildren())
            {
                Message m = ds.getValue(Message.class);
                if(m.getFrom().equals(myUserName) || m.getTo().equals(myUserName)) {
                    m.setKey(ds.getKey());
                    temp.add(index+")"+m.getSubject());
                }
            }
            SubjectArray.addAll(temp);
            adapter.notifyDataSetChanged();
        }
        else{
            for (DataSnapshot ds : data.getChildren())
            {
                Message m = ds.getValue(Message.class);
                try {

                    if (m.getFrom().equals(myUserName) || m.getTo().equals(myUserName)) {
                        m.setKey(ds.getKey());
                        MessagesArray.put(index+")"+m.getSubject(),m);
                        SubjectArray.add(index+")"+m.getSubject());
                        index++;

                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                }

            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, SubjectArray);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Browse.class);
        startActivity(i);
        finish();

    }
    private void descriptionDialog(Message m,String sub,String ans,String msg) {
        try {

            String s = ("From: "+m.getFrom())+"\n"+"Message: "+msg+"\n"+"answer: "+ans;
            final CharSequence[] options = {s,"Cancel"};
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Messages.this);
            builder.setTitle(sub);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        } catch (Exception e) {
            Toast.makeText(this, "error with dialog", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
