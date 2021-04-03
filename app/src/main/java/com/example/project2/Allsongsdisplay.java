package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.service.JcPlayerService;
import com.example.jean.jcplayer.service.JcPlayerServiceListener;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Allsongsdisplay extends AppCompatActivity {


    RecyclerView recyclerView;
    Allsongsdisplay_adapter allsongsdisplay_adapter;
    List<Displaysongs> msongs;
    private FirebaseAuth mAuth;

 //   ArrayAdapter<String> adapter;
    List<String> songnames=new ArrayList<>();
    List<String> songurls=new ArrayList<>();

    FirebaseUser firebaseUser;
    DatabaseReference reference,reference1;
    String userid;
    ImageView uploadsong;


    JcPlayerView jcplayer;
    ArrayList<JcAudio> jcAudios=new ArrayList<>();


    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsongsdisplay);



        uploadsong=findViewById(R.id.uploadsong);


        final String user_id = getIntent().getStringExtra("userid");
        userid=user_id;


               recyclerView = findViewById(R.id.recyclerview);
   //               listview = findViewById(R.id.listview);
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
               // reference1= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());



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
                msongs=new ArrayList<>();
                usersdetailsdisplay();
/*
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        jcplayer.playAudio(jcAudios.get(i));
                        jcplayer.setVisibility(View.VISIBLE);
                        jcplayer.createNotification(R.drawable.ic_album_black_24dp);
                    }
                });
*/


                return;

            }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.pause();
    }

    private void usersdetailsdisplay()
            {
               // final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                reference = FirebaseDatabase.getInstance().getReference().child("Music_upload").child(firebaseUser.getUid()).child(userid);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        msongs.clear();
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            Displaysongs displaysongs= Snapshot.getValue(Displaysongs.class);

                            if (Snapshot.exists() && Snapshot.hasChild("songname")&& Snapshot.hasChild("songurl"))
                            {
                                    msongs.add(displaysongs);

                            }
       //                     jcAudios.add(JcAudio.createFromURL(displaysongs.getSongname(),displaysongs.getSongurl()));
                        }
            //            jcplayer.initPlaylist(jcAudios,null);
                     //   jcplayer.initPlaylist(jcAudios,null);
                        allsongsdisplay_adapter = new Allsongsdisplay_adapter(getApplicationContext(), msongs,userid);
                        recyclerView.setAdapter(allsongsdisplay_adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
/*
                private void usersdetailsdisplay()
                {
                    // final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    reference = FirebaseDatabase.getInstance().getReference().child("Music_upload").child(firebaseUser.getUid());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                                Displaysongs displaysongs= Snapshot.getValue(Displaysongs.class);

                                songnames.add(displaysongs.getSongname());
                                songurls.add(displaysongs.getSongurl());
                                jcAudios.add(JcAudio.createFromURL(displaysongs.getSongname(),displaysongs.getSongurl()));

                            }

                            adapter = new ArrayAdapter<String>(Allsongsdisplay.this,R.layout.song_cardlayout,R.id.un,songnames)

                            {

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {

                                    View view=super.getView(position,convertView,parent);

                                    TextView un=view.findViewById(R.id.un);

                                    un.setSingleLine(true);
                                    un.setMarqueeRepeatLimit(50);
                                    un.setMaxLines(1);

                                    return view;

                                }
                            };

                            jcplayer.initPlaylist(jcAudios,null);
                            listview.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

*/


               /* reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists() &&  dataSnapshot.hasChild("songname") && dataSnapshot.hasChild("songurl")){
                            Displaysongs displaysongs=dataSnapshot.getValue(Displaysongs.class);
                            msongs.add(displaysongs);

                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                */

            }
}





