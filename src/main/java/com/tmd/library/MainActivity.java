package com.tmd.library;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{

    private Button Login;
    private TextView UpperText;
    private TextView Forgot;
    private TextView AdminLabel;
    private EditText Password;
    private EditText UserName;
    Intent I;
    Bundle bun;
    private static User user;
    private static String user_key;
    private boolean Authenticated;
    private FirebaseAuth mAuth;
    private TextView admin;
    private static boolean isAdmin;
    private static String adminPass;
    private static String adminEmail;
    FirebaseAnalytics analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpperText = (TextView) findViewById(R.id.Login_Text);
        Login = (Button) findViewById(R.id.Login_Button);
        Login.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View V) {
                        LogIn();
                    }
                }
        );
        Button SignUp = (Button)findViewById(R.id.SignUp);
        SignUp.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View V) {
                        Signup();
                    }
                }
        );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Forgot = (TextView)findViewById(R.id.LogIn_forgot);
        Forgot.setText(R.string.forgot_label);
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotMyPassword();
            }
        });



    }
    private void forgotMyPassword() {
        try {
            String msg = "We're sorry, this option is not available now. please try on another time.";
            final CharSequence[] options = {msg,"Cancel"};
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Not Available");
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
    protected void onRestart() {
        super.onRestart();

    }

    private void LogIn()
    {
       /* if(bun == null)
        {
            bun =new Bundle();
            bun.putString("Name","User");
        }
        I.putExtras(bun);*/
         UserName = (EditText)findViewById(R.id.Login_User);
        Password = (EditText)findViewById(R.id.Password_Text);
        ProgressBar spinner = (ProgressBar)findViewById(R.id.Main_ProgressBar);
        spinner.setVisibility(View.VISIBLE);
        try {
            CheckUser();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void Signup()
    {
        Bundle signup = new Bundle();
        analytics = FirebaseAnalytics.getInstance(MainActivity.this);
        signup.putString(FirebaseAnalytics.Param.ITEM_NAME,"signup");
        signup.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"Button");
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,signup);
        Intent I = new Intent(MainActivity.this,signup.class);
        startActivity(I);
    }
    private void CheckUser() throws InterruptedException {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();
        I = new Intent(MainActivity.this,Browse.class);
        String Name = UserName.getText().toString();
        String Pass = Password.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        //database.setPersistenceEnabled(true);
        DatabaseReference ref = database.getInstance().getReference();
        ref = ref.child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Toast.makeText(getApplicationContext(),dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User s = ds.getValue(User.class);

                    try {
                        String name = UserName.getText().toString();
                        String o_name = s.getName().toString();

                        if (name.equals(o_name)) {
                            String pass1 = Password.getText().toString();
                            String pass2 = s.getPassword().toString();
                            if (pass1.equals(pass2)) {
                                Authenticated = true;
                                user = s;
                                user_key = ds.getKey();
                                Bundle login = new Bundle();
                                analytics = FirebaseAnalytics.getInstance(MainActivity.this);
                                login.putString("User",user.getName().toString());
                                login.putString(FirebaseAnalytics.Param.ITEM_ID,user_key);
                                analytics.logEvent(FirebaseAnalytics.Event.LOGIN,login);
                                Toast.makeText(getApplicationContext(), "User Name Match!", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), user_key, Toast.LENGTH_SHORT).show();
                                ProgressBar spinner = (ProgressBar)findViewById(R.id.Main_ProgressBar);
                                spinner.setVisibility(View.INVISIBLE);
                                I.putExtra("Name",UserName.getText());
                                I.putExtra("Password",Password.getText().toString());
                                startActivity(I);
                            }
                        }
                    } catch (Exception ex) {
                        //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                if (!Authenticated) {
                   
                    Toast.makeText(getApplicationContext(), "The user is not in the databse", Toast.LENGTH_SHORT).show();
                    ProgressBar spinner = (ProgressBar)findViewById(R.id.Main_ProgressBar);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static User getuser()
    {
        return user;
    }

    public static String getUser_key() {
        return user_key;
    }
    public static void setAdmin(String email, String password)
    {
        isAdmin = true;
        adminEmail = email;
        adminPass = password;

    }
    //region Gestures Override
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    //endregion
}
