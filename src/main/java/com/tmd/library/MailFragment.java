package com.tmd.library;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MailFragment extends Fragment {
    ImageView im;
    FirebaseDatabase database;
    DatabaseReference ref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mail_fragment, container, false);
        im = (ImageView)view.findViewById(R.id.Mail_Image);
        database = FirebaseDatabase.getInstance("https://library-b0594.firebaseio.com/");
        Query q = database.getReference().child("Messages");
        q.limitToLast(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Message m = ds.getValue(Message.class);
                            User s = MainActivity.getuser();
                            String name = s.getName()+" "+s.getLast_Name();
                            if (m.getTo().toString().equals(name)) {
                                if(!m.isRead()) {
                                    im.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.newmail));
                                    Toast.makeText(getContext(), "you've got mail", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getActivity().getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Messages.class);
                im.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mail2));
                startActivity(i);

            }
        });

        return view;
    }
}
