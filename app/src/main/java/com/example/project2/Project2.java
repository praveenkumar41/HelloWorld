package com.example.project2;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


public class Project2 extends Application
{
    private DatabaseReference datadetails,datadetails1;
    private FirebaseUser firebaseUser;

    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();



        if(firebaseUser!=null) {
            datadetails = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            datadetails.keepSynced(true);
            datadetails.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null)
                    {

                        datadetails.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                        datadetails.child("online").setValue("true");
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }




    }
}
