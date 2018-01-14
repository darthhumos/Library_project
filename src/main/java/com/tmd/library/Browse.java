package com.tmd.library;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Browse extends AppCompatActivity {

    private ListView list;
    int index = 0;
    private ArrayList<String> Books2;
    private static HashMap<String,Book> BooksArray;
    private  Book b = new Book();
    FirebaseDatabase database;
    DatabaseReference myRef;
    static Bundle bun;
    boolean Authenticated = false;
    private FirebaseAuth mAuth;
    private FirebaseUser u;
    private ProgressBar prog;
    ArrayAdapter<String> adapter;
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
        BooksArray = new HashMap<>();
        prog = (ProgressBar)findViewById(R.id.Browse_progress);
        u = mAuth.getCurrentUser();
        if(u!=null) {
            mAuth.signInWithEmailAndPassword("tptomerpeled@gmail.com", "123456").
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("enter", "signInWithCustomToken:success");
                                u = mAuth.getCurrentUser();
                                //Toast.makeText(getApplicationContext(), "retriving books from the server", Toast.LENGTH_SHORT).show();
                                prog.setVisibility(View.VISIBLE);
                                getBooks();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("enter", "signInWithCustomToken:failure", task.getException());
                            }
                        }
                    });
        }
        else
        {
            prog.setVisibility(View.VISIBLE);
            getBooks();
        }
        prog.setVisibility(View.INVISIBLE);
        //database.goOnline();
        //database.setPersistenceEnabled(true);

        //init_books();
        //endregion
   list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           String bookname  = String.valueOf(adapterView.getItemAtPosition(i));
           Book b = BooksArray.get(bookname);
           Intent bookview = new Intent(Browse.this,BookView.class);
           bookview.putExtra("index", b.getId());
           try{
               startActivity(bookview);
           }
           catch(Exception ex)
           {
              // Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
           }
       }
   });



    }

    private void getBooks() {
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        myRef = database.getReference("Books");
       // Toast.makeText(getApplicationContext(),"connecting to the server",Toast.LENGTH_SHORT).show();
        myRef.addValueEventListener(new ValueEventListener() {
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
        if(adapter!=null) {
            Books2.clear();
            adapter.notifyDataSetChanged();
            ArrayList<String> temp = new ArrayList<>();
            for (DataSnapshot ds : data.getChildren())
            {
                Book b = ds.getValue(Book.class);
                temp.add(b.getName());
                BooksArray.put(b.getName(),b);
            }
            Books2.addAll(temp);
            adapter.notifyDataSetChanged();
            for(int i = 0;i<list.getCount();i++)
            {
                View v  = (View)list.getChildAt(i);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                Book b = BooksArray.get(tv.getText().toString());
                if (b.isAvailability() != 0) {
                    tv.setTextColor(Color.rgb(50, 210, 20));
                }
                else tv.setTextColor(Color.rgb(200, 20, 10));
            }
        }
        else{
            for (DataSnapshot ds : data.getChildren())
            {
                Book b = ds.getValue(Book.class);
                Books2.add(b.getName());
                BooksArray.put(b.getName(),b);
            }
           setadapter(Books2);
        }
    }

    private void setadapter(ArrayList<String> books) {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,books) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                if (BooksArray.get(tv.getText().toString()).isAvailability() != 0) {
                    tv.setTextColor(Color.rgb(50, 210, 20));
                }
                else tv.setTextColor(Color.rgb(200, 20, 10));

                // Generate ListView Item using TextView
                return view;
            }
        };

        list.setAdapter(adapter);
        prog.setVisibility(View.INVISIBLE);
    }


    public static Bundle getBun()
    {
        return bun;
    }

    public static ArrayList<Book> getBooksArray() {
        ArrayList<Book> temp = new ArrayList<>();
        temp.addAll(BooksArray.values());
        return temp;
    }

    public static Book getBook(int index)
    {
       for(Book b :BooksArray.values())
       {
           if((b.getId())== index)
           {
               return b;
           }
       }
       return new Book();
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
        //finish();
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

    public static Book getBook(String bookname) {
        return BooksArray.get(bookname);
    }
}
