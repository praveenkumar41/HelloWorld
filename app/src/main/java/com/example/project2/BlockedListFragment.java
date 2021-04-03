package com.example.project2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedListFragment extends AppCompatActivity {

    RecyclerView recyclerView;
    BlockedlistAdapter blockedlistAdapter;
    List<Blockedlist> mblock;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference1;
    CircleImageView imageenter;
    SearchView userssearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blocked_list);

        recyclerView = findViewById(R.id.recyclerview);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        imageenter = findViewById(R.id.imageenter);

        reference1= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());


  /*      reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                final String image= dataSnapshot.child("IMAGEURL").getValue().toString();


                Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageenter, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getContext()).load(image).placeholder(R.drawable.pro).into(imageenter);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


   */


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mblock=new ArrayList<>();
        usersdetailsdisplay();
        return;

    }


    private void usersdetailsdisplay()
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mblock.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Blockedlist blockedlist = Snapshot.getValue(Blockedlist.class);

                    if (Snapshot.exists() && Snapshot.hasChild("accepted")&& Snapshot.hasChild("firstrequested")&&Snapshot.hasChild("friendssince"))
                    {
                        if(Snapshot.hasChild("blockeduser")){
                            mblock.add(blockedlist);
                        }

                    }
                }
                blockedlistAdapter = new BlockedlistAdapter(getApplicationContext(), mblock);
                recyclerView.setAdapter(blockedlistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    }


