package com.example.project2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Story_seen_List extends AppCompatActivity {

    RecyclerView recyclerView;
    Story_seen_List_Adapter story_seen_list_adapter;
    List<String> mviews;
    List<Friends> mfriend;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference1;
    CircleImageView imageenter;
    SearchView userssearch;
    String userid;
    TextView count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_seen__list);

        recyclerView = findViewById(R.id.recyclerview);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        count=findViewById(R.id.count);
        imageenter = findViewById(R.id.imageenter);

        reference1= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        reference1.keepSynced(true);


        String userids=getIntent().getStringExtra("id");
        String storyid=getIntent().getStringExtra("storyid");
        String title=getIntent().getStringExtra("title");


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

        mviews=new ArrayList<String>();
        mfriend= new ArrayList<>();
        getviews();
        seenNumber(userids,storyid);

    }

    private void seenNumber(String userids, String storyid) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("story").child(userids).child(storyid).child("views");
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount()==0)
                {
                    count.setText(""+dataSnapshot.getChildrenCount());
                }
                else{
                    int count11= (int) (dataSnapshot.getChildrenCount()-1);
                    if(count11>0 && count11<=9)
                    {
                        count.setText(""+"0"+count11);
                    }
                    else
                    {
                        count.setText(""+count11);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getviews()
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");
        reference.keepSynced(true);

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(firebaseUser.getUid()).child(getIntent().getStringExtra("storyid")).child("views");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mviews.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    if(!firebaseUser.getUid().equals(Snapshot.getKey()))
                    {
                        mviews.add(Snapshot.getKey());
                        userid=Snapshot.getKey();
                    }
                }

                story_seen_list_adapter = new Story_seen_List_Adapter(getApplicationContext(),mviews,userid);
                recyclerView.setAdapter(story_seen_list_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}


