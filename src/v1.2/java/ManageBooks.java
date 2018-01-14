package com.tmd.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManageBooks extends AppCompatActivity {

    private User s;
    final String NAME="Book";
    FirebaseDatabase database;
    DatabaseReference ref;
    ProgressBar prog;
    ArrayList<String> adapter_books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);
        boolean isAdmin = MainActivity.isAdmin();
        if(!isAdmin)
        {
            Toast.makeText(getApplicationContext(),"only authenticated users can view this page",Toast.LENGTH_LONG).show();
            finish();
        }
        s = MainActivity.getuser();
        TextView Hello = (TextView)findViewById(R.id.managebooks_hello);
        Hello.setText(Hello.getText()+ " "+s.getName());
        Button b = (Button)findViewById(R.id.manage_addbooks);
        adapter_books = new ArrayList<>();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageBooks.this,addbooks.class);
                startActivity(i);
                i.putExtra("Id",adapter_books.size());
            }
        });
        prog = (ProgressBar)findViewById(R.id.Manage_progress);
        prog.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"getting all books",Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        ref = database.getReference("Books");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                getBooks(data);
                ListView View = (ListView)findViewById(R.id.Manage_BooksList);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageBooks.this,android.R.layout.simple_list_item_1, adapter_books);
                View.setAdapter(adapter);
                prog.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"problem connecting to firebase"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBooks(DataSnapshot data) {
        for(DataSnapshot ds : data.getChildren())
        {
            Book b = ds.getValue(Book.class);
            adapter_books.add(b.getName());
        }


    }
    class compare implements Comparator<String>
    {
        @Override
        public int compare(String o1, String o2) {
            return 0;
        }
    }
}
