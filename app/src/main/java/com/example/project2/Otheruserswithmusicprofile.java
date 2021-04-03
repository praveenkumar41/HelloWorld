package com.example.project2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media2.MediaPlayer;
import androidx.viewpager.widget.ViewPager;
import com.gigamole.library.PulseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;


public class Otheruserswithmusicprofile extends AppCompatActivity {

     CircleImageView musicimage;
     TextView musicname;
     ImageView threedots,backbutton;

     DatabaseReference datadetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruserswithmusicprofile);

        final String userid=getIntent().getStringExtra("userid");

        threedots=findViewById(R.id.threedots);
        backbutton=findViewById(R.id.backbutton);

        musicimage=findViewById(R.id.musicimage);
        musicname=findViewById(R.id.musicname);


        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("USERID",userid);
                Othersusersbottomsheets bottomsheet=new Othersusersbottomsheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(getSupportFragmentManager(),"Othersusersbottomsheets");
            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        datadetails = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        datadetails.keepSynced(true);



        datadetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("IMAGEURL").getValue().toString();
                String name = dataSnapshot.child("USERNAME").getValue().toString();
                musicname.setText(name);

                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.pro).into(musicimage);

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
