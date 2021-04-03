package com.example.project2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Searchusers_friends extends AppCompatActivity implements View.OnKeyListener{

    ImageView back;
    TextView contacts;
    SearchView userssearch;
    RecyclerView rv;


    UserAdapter userAdapter;
    List<Details> muser;

    FriendsAdapter friendsAdapter;
    List<Friends> mfriends;
    List<String> mfollowing;

    private List<String> archivechat=new ArrayList<>();
    ImageView addchat,friendrequestprofile,searchit;
    List<String> allusers;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchusers_friends);

        back=findViewById(R.id.back);
        contacts=findViewById(R.id.contacts);
        userssearch=findViewById(R.id.userssearch);
        rv=findViewById(R.id.rv);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),AllContacts_list.class);
                startActivity(intent);
            }
        });



        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position=viewHolder.getAdapterPosition();

                switch (direction)
                {
                    case ItemTouchHelper.LEFT:

                        final Details details=muser.get(position);

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(muser.get(position).getID());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archivechat.add(username);
                                muser.remove(muser.get(position));
                                userAdapter.notifyItemRemoved(position);


//


                                Snackbar.make(rv,username+",Hidden.",Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archivechat.remove(archivechat.lastIndexOf(username));
                                                muser.add(details);
                                                userAdapter.notifyItemInserted(position);
                                            }
                                        }).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;

                    case ItemTouchHelper.RIGHT:

                        final Details details1=muser.get(position);

                        reference=FirebaseDatabase.getInstance().getReference("users").child(muser.get(position).getID());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archivechat.add(username);

                                muser.remove(muser.get(position));
                                userAdapter.notifyItemRemoved(position);


                                Snackbar.make(rv,username+":"+"Hidden.",Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archivechat.remove(archivechat.lastIndexOf(username));
                                                muser.add(details1);
                                                userAdapter.notifyItemInserted(position);
                                            }
                                        }).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getApplicationContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.selected_filter))
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.selected_filter))
                        .addSwipeLeftActionIcon(R.drawable.ic_visibility_off_black_24dp)
                        .addSwipeRightActionIcon(R.drawable.ic_visibility_off_black_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);



        muser=new ArrayList<>();

        userssearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });

        usersdetailsdisplay();
    }


    private void usersdetailsdisplay()
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("usersdisplay");


        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                muser.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    Details details = Snapshot.getValue(Details.class);

                    assert details != null;
                    if (!details.getID().equals(firebaseUser.getUid()))
                    {
                        muser.add(details);
                    }
                }

                userAdapter=new UserAdapter(getApplicationContext(),muser);
                userAdapter.notifyDataSetChanged();
                rv.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
            if (i == KeyEvent.KEYCODE_BACK) {

                finish();
                return true;
            }
        }
        return false;
    }
}