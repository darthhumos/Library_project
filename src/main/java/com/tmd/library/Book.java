package com.tmd.library;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Tomer on 23-Nov-17.
 */

public class Book {
    private String Name;
    private String Author;
   // private String pic;
    private int Availability;
    private ArrayList<String> Geners;
    private String description;
    private int Id;
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }
/*
    public String getPic() {
        return pic;
    }

    public void setPic(String photo_URL) {
        pic = photo_URL;
    }
*/
    public int isAvailability() {
        return Availability;
    }

    public void setAvailability(int availability) {
        Availability = availability;
    }

    public ArrayList<String> getGeners() {
        return Geners;
    }

    public void setGeners(ArrayList<String> geners) {
        this.Geners = geners;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAuthor(String author) {
        Author = author;
    }


    public String getName() {
        return Name;
    }

    public String getAuthor() {
        return Author;
    }

    public Book()
    {}
    public Book(String _name, String _author, Bitmap _pic, ArrayList<String> _ganer, String _description,
                int _id, int _availability)
    {
        this.Author = _author;
        this.Name = _name;
        //this.pic = BitMapToString(_pic);
        this.Availability = _availability;
        this.Geners = _ganer;
        this.description = _description;
        this.Id = _id;
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
