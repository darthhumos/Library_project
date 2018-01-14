package com.tmd.library;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class addbooks extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);

        String Name = ((TextView)findViewById(R.id.addbooks_label_name)).getText().toString();;
        String Author = ((TextView)findViewById(R.id.addbooks_label_author)).getText().toString();;
        ImageView iv =(ImageView)findViewById(R.id.addbooks_image);
        Bitmap pic = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        int Availability = 3;
        String ganersArray [] = ((TextView)findViewById(R.id.addbooks_label_ganere)).getText().toString().split(",");
        ArrayList<String> Geners = new ArrayList<>(Arrays.asList(ganersArray));
        String description = ((TextView)findViewById(R.id.addbooks_label_desciption)).getText().toString();;
        int Id = Browse.getBooksArray().size()+1;

        Book newBook = new Book(Name,Author,pic,Geners,description,Id,Availability);


        //adding book to the firabase


    }
}
