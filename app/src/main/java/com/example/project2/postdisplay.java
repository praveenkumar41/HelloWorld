package com.example.project2;

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


public class postdisplay extends Fragment {

    RecyclerView postdisplayrv;
    ArrayList<MediaObject1> mediaObject1s;
    List<String> userids11=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_postdisplay, container, false);

        postdisplayrv=view.findViewById(R.id.postdisplayrv);

        initRecyclerView();
        return view;
    }


    private void initRecyclerView()
    {

        final String [] mm=new String[20];

        postdisplayrv.setLayoutManager( new LinearLayoutManager(getContext()));
        postdisplayrv.setHasFixedSize(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        postdisplayrv.addItemDecoration(itemDecorator);


        DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference().child("posts");
        reference11.keepSynced(true);
        reference11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                mediaObject1s=new ArrayList<>();
                mediaObject1s.clear();

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    MediaObject1 mediaObject = Snapshot.getValue(MediaObject1.class);

                        if((firebaseUser.getUid().equals(mediaObject.getPublisher())&& "text".equals(mediaObject.getType())))
                        {
                            mediaObject1s.add(mediaObject);
                        }

                }

                Collections.reverse(mediaObject1s);
                post_display_Adapter adapter = new post_display_Adapter(getContext(), mediaObject1s);
                adapter.notifyDataSetChanged();
                postdisplayrv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
