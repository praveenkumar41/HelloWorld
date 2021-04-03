package com.example.project2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.Adapter.Story_View_Adapter;
import com.example.project2.Notification.Token;
import com.example.project2.videostream.MediaObject;
import com.example.project2.videostream.MediaObject1;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;
import static android.widget.LinearLayout.VERTICAL;
import static androidx.recyclerview.widget.LinearLayoutManager.*;
import static androidx.recyclerview.widget.RecyclerView.*;

public class PersonFragment extends Fragment implements View.OnKeyListener  {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PARTITION_CODE = 101;
    RecyclerView recyclerView;
    MediaPlayer mp;

    FriendsAdapter friendsAdapter;

    List<Friends> mfriends;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference1;
    DatabaseReference reference2;
    CircleImageView imageenter, addstory,settings;
    SearchView friendssearch;
    FloatingActionButton add_message;
    TextView roundimagename;
    Bitmap bit;
    static final int PICK_IMAGE = 11;
    public static Bundle bundle = new Bundle();

    private Story_View_Adapter story_view_adapter;
    private List<Stories> storyList;
    private List<String> archivechat;

    private List<String> mfollowing;
    private List<String> mfollowers;

    DatabaseReference mref,reference12;

    String userid12;
    Activity activity;

    public PersonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.person_fragment, container, false);

        add_message=view.findViewById(R.id.add_message);

        recyclerView = view.findViewById(R.id.recyclerview);
     //   settings=view.findViewById(R.id.settings);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference1 = FirebaseDatabase.getInstance().getReference("users");

        mp=new MediaPlayer();
        if(mp.isPlaying())
        {
            mp.stop();
        }

        add_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });



/*
        reference1.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String image= dataSnapshot.child("IMAGEURL").getValue().toString();

                Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageenter, new Callback() {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onError()
                    {
                        Picasso.with(getContext()).load(image).placeholder(R.drawable.pro).into(imageenter);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {

                final int position=viewHolder.getAdapterPosition();

                switch (direction)
                {
                    case ItemTouchHelper.LEFT:

                        final Friends friends =mfriends.get(position);

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(mfriends.get(position).getId());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archivechat.add(username);

                                mfriends.remove(mfriends.get(position));
                                friendsAdapter.notifyItemRemoved(position);

                                String username1= username.substring(0, 1).toUpperCase() + username.substring(1);

                                Snackbar snackbar=Snackbar.make(recyclerView,username1+" "+"chat"+" "+"Archieved",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setDuration(6000).setActionTextColor(Color.WHITE).setAction("undo", new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        archivechat.remove(archivechat.lastIndexOf(username));
                                        mfriends.add(friends);
                                        friendsAdapter.notifyItemInserted(position);

                                    }
                                });

                                View snackbarview=snackbar.getView();
                                snackbarview.setBackgroundColor(Color.parseColor("#0080FF"));
                                snackbar.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;

                    case ItemTouchHelper.RIGHT:

                        final Friends friends1=mfriends.get(position);

                        reference=FirebaseDatabase.getInstance().getReference("users").child(mfriends.get(position).getId());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archivechat.add(username);

                                mfriends.remove(mfriends.get(position));
                                friendsAdapter.notifyItemRemoved(position);

                                String username1= username.substring(0, 1).toUpperCase() + username.substring(1);


                                Snackbar snackbar=Snackbar.make(recyclerView,username1+" "+"chat"+" "+"Archieved",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setDuration(6000).setActionTextColor(Color.WHITE).setAction("undo", new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        archivechat.remove(archivechat.lastIndexOf(username));
                                        mfriends.add(friends1);
                                        friendsAdapter.notifyItemInserted(position);

                                    }
                                });

                                View snackbarview=snackbar.getView();
                                snackbarview.setBackgroundColor(Color.parseColor("#0080FF"));
                                snackbar.show();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark))
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark))
                        .addSwipeLeftActionIcon(R.drawable.ic_archive_black_24dp)
                        .addSwipeRightActionIcon(R.drawable.ic_archive_black_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        archivechat=new ArrayList<String>();


//        usersdetailsdisplay();
        checkfollowing();
       // updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }


/*

    private void getviews() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");


        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(firebaseUser.getUid()).child(getArguments().getString("storyid")).child("views");


        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfriends.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    Friends friends = Snapshot.getValue(Friends.class);
                            mfriends.add(friends);
                }

                friendsAdapter = new FriendsAdapter(getContext(), mfriends);
                recyclerView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

*/


    private void checkfollowing()
    {
        mfollowing = new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("follow_count").child(firebaseUser.getUid()).child("following");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mfollowing.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mfollowing.add(snapshot.getKey());
                }

                usersdetailsdisplay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



   /* private void checkfollowingforstories()
    {

        mfollowing = new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("follow_count").child(firebaseUser.getUid()).child("following");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mfollowing.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mfollowing.add(snapshot.getKey());
                }
                readStory();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/


    private void usersdetailsdisplay()
    {
        mfriends=new ArrayList<>();
        final int[] count = {0};

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid());
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfriends.clear();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    Friends friends=Snapshot.getValue(Friends.class);

                    mfriends.add(friends);
                }
                friendsAdapter = new FriendsAdapter(getContext(), mfriends,userid12);
                friendsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*
    private void readStory()
    {
       DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent=System.currentTimeMillis();

                storyList.clear();
                storyList.add(new Stories("",0,0,"",FirebaseAuth.getInstance().getCurrentUser().getUid()));


                for(String id:mfollowing)
                {
                    int countstory=0;
                    Stories stories=null;

                    for(DataSnapshot snapshot:dataSnapshot.child(id).getChildren())
                    {

                        stories=snapshot.getValue(Stories.class);
                        assert stories != null;
                        if(timecurrent>stories.getTimestart() && timecurrent<stories.getTimeend())
                        {
                            countstory++;
                        }

                    }

                    if(countstory>0)
                    {
                        storyList.add(stories);
                    }
                }

                story_view_adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

*/

    private void updateToken(String token)
    {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
            if (i == KeyEvent.KEYCODE_BACK) {

                Intent intent=new Intent(getContext(),Startapp.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        }
        return false;
    }
}


