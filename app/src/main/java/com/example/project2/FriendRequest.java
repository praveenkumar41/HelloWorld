package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project2.Notification.Token;
import com.example.project2.Notification1.Token1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FriendRequest extends AppCompatActivity {

    RecyclerView recyclerView;



    List<Friends_request> mfriendrequest;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference1, reference2;
    CircleImageView imageenter;
    DatabaseReference mref;
    List<Details> muser;
    String userid;
    int count=0;

    List<String> archieved=new ArrayList<>();

    FriendRequest_Adapter friendRequest_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        recyclerView = findViewById(R.id.recyclerview);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference1 = FirebaseDatabase.getInstance().getReference("users");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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

                        final Friends_request friends_request=mfriendrequest.get(position);

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(mfriendrequest.get(position).getSender());

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archieved.add(username);
                                mfriendrequest.remove(mfriendrequest.get(position));
                                friendRequest_adapter.notifyItemRemoved(position);


//


                                Snackbar snackbar=Snackbar.make(recyclerView,username+" "+"Has been"+" "+"Hidden.",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setDuration(6000)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archieved.remove(archieved.lastIndexOf(username));
                                                mfriendrequest.add(friends_request);
                                                friendRequest_adapter.notifyItemInserted(position);
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

                        final Friends_request friends_request1=mfriendrequest.get(position);

                       reference=FirebaseDatabase.getInstance().getReference("users").child(mfriendrequest.get(position).getSender());

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archieved.add(username);
                                mfriendrequest.remove(mfriendrequest.get(position));
                                friendRequest_adapter.notifyItemRemoved(position);


//


                                Snackbar snackbar=Snackbar.make(recyclerView,username+" "+"Has been"+" "+"Hidden.",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setDuration(6000)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archieved.remove(archieved.lastIndexOf(username));
                                                mfriendrequest.add(friends_request1);
                                                friendRequest_adapter.notifyItemInserted(position);
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
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getApplicationContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark))
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark))
                        .addSwipeLeftActionIcon(R.drawable.ic_visibility_off_black_24dp)
                        .addSwipeRightActionIcon(R.drawable.ic_visibility_off_black_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);




        muser=new ArrayList<>();
        mfriendrequest = new ArrayList<Friends_request>();
        friendrequestdisplay();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }


    private void friendrequestdisplay()
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Friend_Request").child(firebaseUser.getUid());
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfriendrequest.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Friends_request friends_request = Snapshot.getValue(Friends_request.class);
                    Friends friends = Snapshot.getValue(Friends.class);

                    userid=friends.getId();
                    String ss=Snapshot.child("requested_type").getValue(String.class);

                    if (Snapshot.exists() && Snapshot.hasChild("requested_type"))
                    {
                        if("Recieved".equals(ss))
                        {
                            mfriendrequest.add(friends_request);

                        }

                    }
                }

                friendRequest_adapter = new FriendRequest_Adapter(getApplicationContext(), mfriendrequest,userid,count);
                recyclerView.setAdapter(friendRequest_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void updateToken(String token)
    {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token1 token1=new Token1(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }


}
