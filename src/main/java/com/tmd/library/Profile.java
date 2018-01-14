package com.tmd.library;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity implements OnMapReadyCallback {
    MapView _View;
    GoogleMap map;
    Bundle bun;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        _View  = (MapView)findViewById(R.id.mapView);
        _View.onCreate(savedInstanceState);
        _View.getMapAsync(this);
        bun = Browse.getBun();
        user  = MainActivity.getuser();
        TextView tx = (TextView)findViewById(R.id.profile_name);
        tx.setText(user.getName()+" "+user.getLast_Name());
        TextView Email = (TextView)findViewById(R.id.Profile_Email);
        TextView Street = (TextView)findViewById(R.id.Profile_Street);
        TextView City = (TextView)findViewById(R.id.Profile_City);
        TextView Books = (TextView)findViewById(R.id.Profile_loaned);
        Books.setText(String.valueOf(user.getBooks().size()));
        Email.setText(user.getEmail());
        Street.setText(user.getStreet());
        City.setText(user.getCity());
        ImageView iv = (ImageView)findViewById(R.id.Profile_ImageView);
        iv.setImageBitmap(StringToBitMap(user.getPic()));
        //Toast.makeText(this, bun.getString("Name"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng loc = new LatLng(32.109333,34.855499);
        map.addMarker(new MarkerOptions().position(loc).title("Tel Aviv"));
        map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        map.setMinZoomPreference(5.0f);
       // map.moveCamera(CameraUpdateFactory.zoomBy(5.0f));
        _View.onResume();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Browse.class);
        startActivity(i);
    }
}
