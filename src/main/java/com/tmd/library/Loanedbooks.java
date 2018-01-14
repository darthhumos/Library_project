package com.tmd.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        //FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        //DatabaseReference ref = database.getReference("Books");
        /*
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
        });*/
        try {
            getBooks();
        }catch(Exception ex)
        {
            Intent i = new Intent(Loanedbooks.this,Browse.class);
            startActivity(i);
            finish();
        }
        ListView View = (ListView)findViewById(R.id.Loans_BooksList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Loanedbooks.this,android.R.layout.simple_list_item_1, adapter_books);
        View.setAdapter(adapter);
        prog.setVisibility(View.INVISIBLE);
        View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String bookname  = String.valueOf(adapterView.getItemAtPosition(position));
                Book b = Browse.getBook(bookname);
                Intent bookview = new Intent(Loanedbooks.this,BookView.class);
                bookview.putExtra("index", b.getId());
                try{
                    startActivity(bookview);
                }
                catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getBooks() {
        /*for(DataSnapshot ds : data.getChildren())
        {
            Book b = ds.getValue(Book.class);
            books.put(b.getId(),b);
        }*/
        try {
            ArrayList<Book> Allbooks = Browse.getBooksArray();
            for(Book b : Allbooks)
            {
                books.put(b.getId(),b);
            }
            for(int i :list)
            {
                adapter_books.add(books.get(i).getName());

            }
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(),"could not retrive all books please wait " +
                    "untill the operation completes",Toast.LENGTH_LONG).show();

        }


    }
}
