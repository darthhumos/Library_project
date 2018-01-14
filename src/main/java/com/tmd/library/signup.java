package com.tmd.library;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.bitmap;


public class signup extends AppCompatActivity {
    Button b;
    ImageView im;
    final int PICK_IAMGE = 1;
    final int REQUEST_CAMERA = 1337;
    Bundle bun;
    private FirebaseDatabase database;
    String Gender = "";
    FirebaseAnalytics analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        im = (ImageView)findViewById(R.id.signup_image);
        b = (Button)findViewById(R.id.SignUp_SignUp);
        RadioGroup rg = (RadioGroup)findViewById(R.id.SignUp_Gender);
        TextView tx = new TextView(getApplicationContext());
        tx.setText("Gender:");
        rg.addView(tx);
        RadioButton rb = new RadioButton(getApplicationContext());
        rb.setText("Male");
        RadioButton rb2 = new RadioButton(getApplicationContext());
        rb2.setText("female");
        rg.addView(rb);
        rg.addView(rb2);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case 1:
                      Gender = "Male";
                    case 2:
                        Gender = "Female";
                }
            }
        });

        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                /*bun = new Bundle();
                bun.putString("Name",((EditText)findViewById(R.id.Signup_Name)).getText().toString());
                bun.putString("LastName",((EditText)findViewById(R.id.Signup_LastName)).getText().toString());
                bun.putString("Email",((EditText)findViewById(R.id.Signup_Email)).getText().toString());
                bun.putString("Street",((EditText)findViewById(R.id.Signup_street)).getText().toString());
                bun.putString("City",((EditText)findViewById(R.id.Signup_City)).getText().toString());
                i.putExtras(bun);*/
                DatabaseReference myRef = database.getReference();
                String Id = myRef.child("Users").push().getKey();
                myRef = myRef.child("Users").child(Id);
                String Name = ((EditText)findViewById(R.id.Signup_Name)).getText().toString();
                String Last = ((EditText)findViewById(R.id.Signup_LastName)).getText().toString();
                String Password = ((EditText)findViewById(R.id.Signup_Password)).getText().toString();
                String City = ((EditText)findViewById(R.id.Signup_City)).getText().toString();
                String Street = ((EditText)findViewById(R.id.Signup_street)).getText().toString();
                String Phone = ((EditText)findViewById(R.id.SignUp_Phone)).getText().toString();
                String Email = ((EditText)findViewById(R.id.Signup_Email)).getText().toString();
                User s = new User(Name,Last,Password,Email,City,Street,((BitmapDrawable)im.getDrawable()).getBitmap(),Gender,Phone);
                //String b = s.getPic();
                myRef.setValue(s);
                Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_SHORT).show();
                Bundle sign_up = new Bundle();
                analytics = FirebaseAnalytics.getInstance(signup.this);
                sign_up.putString(FirebaseAnalytics.Param.ITEM_NAME,"signup");
                sign_up.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"signing up");
                sign_up.putString("Name",Name.toString());
                sign_up.putString("Email",Email.toString());
                analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,sign_up);
                Intent i = new Intent(signup.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IAMGE);*/
               selectImage();

            }
        });

    }

    private void selectImage() {
        try {

                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(signup.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                           // String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
                            startActivityForResult(intent,REQUEST_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");
                            i.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IAMGE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IAMGE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(data!=null)
                {
                    Uri uri = data.getData();
                    InputStream inputStream = null;
                    try
                    {
                        inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bit  = BitmapFactory.decodeStream(inputStream);
                        im.setImageBitmap(bit);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

        }
        else if(requestCode == REQUEST_CAMERA)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

            Log.e("Activity", "Pick from Camera::>>> ");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File destination = new File(Environment.getExternalStorageDirectory() + "/" +
                    getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String imgPath = destination.getAbsolutePath();
            im.setImageBitmap(bitmap);
        }
    }
}





