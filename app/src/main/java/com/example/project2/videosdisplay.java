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
import java.util.Collections;
import java.util.List;


public class videosdisplay extends Fragment {

    RecyclerView videodisplayrv;
    ArrayList<MediaObject1> medias;
    List<String> userids11=new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_videosdisplay, container, false);

        videodisplayrv=view.findViewById(R.id.videodisplayrv);

        initRecyclerView();
        return view;
    }


    private void initRecyclerView()
    {

        final String [] mm=new String[20];

        videodisplayrv.setLayoutManager( new LinearLayoutManager(getContext()));
        videodisplayrv.setHasFixedSize(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        videodisplayrv.addItemDecoration(itemDecorator);




        DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference().child("posts");
        reference11.keepSynced(true);
        reference11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                medias=new ArrayList<>();
                medias.clear();
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    MediaObject1 mediaObject = Snapshot.getValue(MediaObject1.class);

                        if((firebaseUser.getUid().equals(mediaObject.getPublisher()) && "video".equals(mediaObject.getType())))
                        {
                            medias.add(mediaObject);
                        }
                }

                Collections.reverse(medias);
                Video_display_Adapter adapter1 = new  Video_display_Adapter(getContext(), medias);
                adapter1.notifyDataSetChanged();
                videodisplayrv.setAdapter(adapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
