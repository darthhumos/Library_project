package com.tmd.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Loanedbooks extends AppCompatActivity {
    User s;
    ArrayList<Integer> list;
    ArrayList<String> adapter_books;
    HashMap<Integer,Book> books;
    ProgressBar prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaned_books);
        s = MainActivity.getuser();
        prog = (ProgressBar)findViewById(R.id.Loans_progressBar);
        prog.setVisibility(View.VISIBLE);
        TextView tx = (TextView)findViewById(R.id.Loaned_Hello);
        tx.setText(tx.getText()+" "+s.getName());
         list = s.getBooks();
        adapter_books = new ArrayList<>();
        books = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        DatabaseReference ref = database.getReference("Books");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                getBooks(data);
                ListView View = (ListView)findViewById(R.id.Loans_BooksList);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Loanedbooks.this,android.R.layout.simple_list_item_1, adapter_books);
                View.setAdapter(adapter);
                prog.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getBooks(DataSnapshot data) {
        for(DataSnapshot ds : data.getChildren())
        {
            Book b = ds.getValue(Book.class);
            books.put(b.getId(),b);
        }
        for(int i :list)
        {
            adapter_books.add(books.get(i).getName());
        }
    }
}
