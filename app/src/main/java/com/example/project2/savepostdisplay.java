package com.example.project2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2.videostream.ImagedisplayAdapter;
import com.example.project2.videostream.MediaObject;
import com.example.project2.videostream.MediaObject1;
import com.example.project2.videostream.VerticalSpacingItemDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2.videostream.ImagedisplayAdapter;
import com.example.project2.videostream.MediaObject1;
import com.example.project2.videostream.VerticalSpacingItemDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class savepostdisplay extends Fragment {

    RecyclerView savepostdisplayrv;
    ArrayList<MediaObject1> msaves;
    List<String> mysaves;
    List<String> userids11=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_savepostdisplay, container, false);

        savepostdisplayrv=view.findViewById(R.id.savepostdisplayrv);

        initRecyclerView();
        return view;
    }

    private void initRecyclerView()
    {

        savepostdisplayrv.setLayoutManager( new LinearLayoutManager(getContext()));
        savepostdisplayrv.setHasFixedSize(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        savepostdisplayrv.addItemDecoration(itemDecorator);


        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid());
        reference11.keepSynced(true);

        reference11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mysaves=new ArrayList<>();
                mysaves.clear();

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                   mysaves.add(Snapshot.getKey());
                }

                readsaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readsaves()
    {

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("posts");
        mref.keepSynced(true);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msaves=new ArrayList<>();
                msaves.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MediaObject1 mediaObject1=snapshot.getValue(MediaObject1.class);

                    for(String id:mysaves)
                    {
                        if(mediaObject1.getPushid().equals(id))
                        {
                            msaves.add(mediaObject1);
                        }
                    }
                }

                Collections.reverse(msaves);
                Save_Post_display_Adapter adapter= new Save_Post_display_Adapter(getContext(), msaves);
                adapter.notifyDataSetChanged();
                savepostdisplayrv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}