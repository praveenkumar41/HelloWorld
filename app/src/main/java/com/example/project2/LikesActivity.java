package com.example.project2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesActivity extends AppCompatActivity {

    ImageView backintent;
    RecyclerView rv;

    List<String> mlikes;
    String pushid,publisherid;
    LikesAdapter likesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        backintent=findViewById(R.id.backintent);
        rv=findViewById(R.id.rv);
        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pushid=getIntent().getStringExtra("pushid");
        publisherid=getIntent().getStringExtra("publisherid");


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        readlikes();
    }


    private void readlikes()
    {
        mlikes=new ArrayList<>();
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Likes").child(pushid);
        mref.keepSynced(true);
        final FirebaseUser f=FirebaseAuth.getInstance().getCurrentUser();
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mlikes.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                     mlikes.add(snapshot.getKey());
                }

                Collections.reverse(mlikes);
                likesAdapter=new LikesAdapter(getApplicationContext(),mlikes);
                rv.setAdapter(likesAdapter);
                likesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
