package com.example.project2;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class repost_bottomsheet extends BottomSheetDialogFragment {

    private FirebaseUser firebaseUser;
    repost_bottomsheet context=this;
    LinearLayout repost,repostwithcomment,repostwithstory;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.repost_bottomsheet, container, false);

        final String postid=getArguments().getString("postid");
        final String reposttag=getArguments().getString("reposttag");


        repost=view.findViewById(R.id.repost);
        repostwithcomment=view.findViewById(R.id.repostwithcomment);
        repostwithstory=view.findViewById(R.id.repostwithstory);

        repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Repost").child(postid);

                if (reposttag.equals("repost")) {

                    mref.child(firebaseUser.getUid()).setValue(true);
                    addposttomylist(postid);

                } else {

                    mref.child(firebaseUser.getUid()).removeValue();
                }

                dismiss();
            }
        });

        repostwithcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        repostwithstory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),Alltype_Story.class);
                intent.putExtra("postid",postid);
                intent.putExtra("set","true");
                startActivity(intent);
                dismiss();
            }
        });

        return view;
    }

    private void addposttomylist(String postid) {

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String bitmap=dataSnapshot.child("bitmap").getValue(String.class);
                String description=dataSnapshot.child("description").getValue(String.class);
                String imageurl=dataSnapshot.child("imageurl").getValue(String.class);
                String publisher=dataSnapshot.child("publisher").getValue(String.class);
                String pushid=dataSnapshot.child("pushid").getValue(String.class);
                String type=dataSnapshot.child("type").getValue(String.class);

                final DatabaseReference reference11=FirebaseDatabase.getInstance().getReference().child("posts").push();
                reference11.keepSynced(true);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                final String savecurrentdate = currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                final String savecurrenttime = currenttime.format(calendar.getTime());


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("bitmap",bitmap);
                hashMap.put("date",savecurrentdate);
                hashMap.put("description",description);
                hashMap.put("imageurl",imageurl);
                hashMap.put("publisher",publisher);
                hashMap.put("pushid",reference11.getKey());
                hashMap.put("time",savecurrenttime);
                hashMap.put("type",type);
                hashMap.put("ago", ServerValue.TIMESTAMP);
                hashMap.put("type1","repost");
                hashMap.put("repost_publisher",firebaseUser.getUid());
                hashMap.put("repost_pushid",pushid);


                reference11.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}