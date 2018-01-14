package com.tmd.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookView extends AppCompatActivity {

    private Book book;
    private String ganer="";
    private TextView tv;
    private ImageView iv;
    private FirebaseDatabase database;
    private FirebaseAnalytics analytics;
    Button btn2;
    User user;
    public class ValContainer<T> {
        private T val;

        public ValContainer() {
        }

        public ValContainer(T v) {
            this.val = v;
        }

        public T getVal() {
            return val;
        }

        public void setVal(T val) {
            this.val = val;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        Intent intent = getIntent();
        int index = intent.getIntExtra("index",0);
        book = Browse.getBook(index);
        tv = (TextView) findViewById(R.id.Bookview_name);
        tv.setText(book.getName());
        tv = (TextView) findViewById(R.id.Bookview_author);
        tv.setText(book.getAuthor());
        tv = (TextView) findViewById(R.id.Bookview_genre);
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        analytics = FirebaseAnalytics.getInstance(this);
        for (String str : book.getGeners()){
            ganer += str;
        }
        tv.setText(ganer);

        //region FireBase Handling
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("Book_Pictures");
        myRef = myRef.child(String.valueOf(book.getId()));
        iv = (ImageView)findViewById(R.id.Bookview_image);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Book_Picture pic = dataSnapshot.getValue(Book_Picture.class);
                iv.setImageBitmap(StringToBitMap(pic.getPic()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
        //endregion



        Button btn1 = (Button) findViewById(R.id.Bookview_btn_bookDescription);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionDialog(book.getDescription());
            }
        });
        user = MainActivity.getuser();
        int isAvailable = book.isAvailability();
         btn2 = (Button) findViewById(R.id.Bookview_btn_loan);
        if ((isAvailable==0)||user.getBooks().contains(book.getId())) btn2.setVisibility(View.GONE);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = database.getReference();

                try{

                    int temp = (book.isAvailability()-1);
                    try {
                        if(user.getBooks().contains(book.getId())||book.isAvailability()==0)
                        {
                            Toast.makeText(getApplicationContext(), "the book is already loaned" +
                                    " to you", Toast.LENGTH_SHORT).show();
                            btn2.setVisibility(View.GONE);
                        }
                        else {
                            myRef.child("Books").child("Book" + book.getId()).child("availability").setValue(temp);
                            ArrayList<Integer> user_books = user.getBooks();
                            user.getBooks().add(book.getId());
                            String key = MainActivity.getUser_key();
                           myRef.child("Users").child(key).child("Books").setValue(user_books);
                            Toast.makeText(getApplicationContext(), "the book "+book.getName()
                                    +"is waiting for you in the library", Toast.LENGTH_LONG).show();
                        }

                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Bundle loaned = new Bundle();
                    loaned.putString("User",user.getName().toString());
                    loaned.putString(FirebaseAnalytics.Param.ITEM_NAME,book.getName());
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,loaned);

                 //   myRef.child("User").child(user.getId())

                }
                catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private void descriptionDialog(String desc) {
        try {

            final CharSequence[] options = {desc,"Cancel"};
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BookView.this);
            builder.setTitle("Description");
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Browse.class);
        startActivity(i);
    }
}
