package com.tmd.library;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Tomer on 29-Dec-17.
 */

public class User {
    private String Name;
    private  String Password;
    private String Last_Name;
    private String Email;
    private String City;
    private String Street;
    private String Pic;
    private String Gender;
    private String Phone;
    private ArrayList<Integer> Books;
    public User()
    {

    }

    /**
     *
     * @param _name name
     * @param _last last name
     * @param _pass password
     * @param _Email email
     * @param _City city
     * @param _Street street
     * @param _bit Picture
     * @param _Gender gender
     * @param _Phone phone
     */
    public User(String _name, String _last, String _pass, String _Email, String _City, String _Street,
                Bitmap _bit, String _Gender, String _Phone)
    {
        Name = _name;
        Last_Name = _last;
        Password = _pass;
        this.Pic = BitMapToString(_bit);
        this.City = _City;
        this.Gender = _Gender;
        this.Phone = _Phone;
        this.Street = _Street;
        this.Email = _Email;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public ArrayList<Integer> getBooks() {
        return Books;
    }

    public void setBooks(ArrayList<Integer> books) {
        Books = books;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
