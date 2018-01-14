package com.tmd.library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Browse extends AppCompatActivity {

    private ListView list;
    int index = 0;
    private ArrayList<String> Books2;
    private  Book b = new Book();
    FirebaseDatabase database;
    DatabaseReference myRef;
    static Bundle bun;
    boolean Authenticated = false;
    private FirebaseAuth mAuth;
    private FirebaseUser u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_browse);
        list = (ListView)findViewById(R.id.Book_List);
        //region InitBooks
        bun = getIntent().getExtras();
        User s = MainActivity.getuser();
        TextView text = (TextView) findViewById(R.id.Hello_TextView);
        text.setText(text.getText().toString() + " " + s.getName());
        //region FireBase Handling
        mAuth = FirebaseAuth.getInstance();
        Books2 =  new ArrayList<>();
        mAuth.signInWithEmailAndPassword("tptomerpeled@gmail.com","123456").
                addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("enter", "signInWithCustomToken:success");
                     u= mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),u.getUid(),Toast.LENGTH_SHORT).show();
                     getBooks();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("enter", "signInWithCustomToken:failure", task.getException());
                }
            }
        });

        //database.goOnline();
        //database.setPersistenceEnabled(true);

        //init_books();
        //endregion


    }

    private void getBooks() {
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        myRef = database.getReference("Books");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                Log.d("enter","firebase connected");
                AddBooks(data);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                Log.d("enter","failed");
            }
        });
    }

    private void AddBooks(DataSnapshot data) {
        for(DataSnapshot ds: data.getChildren())
        {
            Book b = ds.getValue(Book.class);
            Books2.add(b.getName());
           // Toast.makeText(getApplicationContext(),Books2.get(index),Toast.LENGTH_SHORT).show();
            index++;
        }
        int s = Books2.size();

        //Toast.makeText(getApplicationContext(), String.valueOf(s), Toast.LENGTH_SHORT).show();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Books2);
        list.setAdapter(adapter);

    }


    public static Bundle getBun()
    {
        return bun;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        myRef = null;
        database.goOffline();
        Log.d("enter","firebase disconnected");
        finish();
        super.onBackPressed();
    }
    @Deprecated
    public void init_books() {
        TextView text = (TextView)findViewById(R.id.Hello_TextView);
        text.setText(text.getText().toString()+" User");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Books2);

        list.setAdapter(adapter);
        int j = 0;
    }
    @Deprecated
    public void RetriveBooks(Map<String,Object> _Books) {
        Log.d("enter","entered retrive");

        for (Map.Entry<String, Object> entry : _Books.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            Books2.add((String) singleUser.get("Name"));
            Toast.makeText(getApplicationContext(),(String) singleUser.get("Name"),Toast.LENGTH_SHORT).show();
            index++;
        }
        init_books();
    }

}
