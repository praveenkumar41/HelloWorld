package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.GroupChat.GroupChatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SearchActivity extends AppCompatActivity implements usermessage_Adapter.RecyclerItemSelectedListener,usermessage_Adapter.gettinguserstochat{

    CircleImageView backactivity;
    SearchView userssearch;
    RecyclerView rv;
    usermessage_Adapter usermessage_Adapter;
    List<Details> muser;
    ChipGroup chip;
    Chip chip12;
    TextView chatit;
    boolean sd=false;

    private List<String> archivechat=new ArrayList<>();
    private List<String> noofchips=new ArrayList<>();

    private List<String> noofchats=new ArrayList<>();
    private List<String> musers=new ArrayList<>();

    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backactivity=findViewById(R.id.backactivity);
        userssearch=findViewById(R.id.userssearch);
        rv=findViewById(R.id.rv);
        chatit=findViewById(R.id.chatit);
        chip=findViewById(R.id.chip);
        userssearch.setBackgroundResource(android.R.color.transparent);

        backactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(muser.get(position).getID());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("USERNAME").getValue(String.class);

                                archivechat.add(username);
                                muser.remove(muser.get(position));
                                usermessage_Adapter.notifyItemRemoved(position);

                                Snackbar.make(rv,username+",Hidden.",Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archivechat.remove(archivechat.lastIndexOf(username));
                                                muser.add(details);
                                                usermessage_Adapter.notifyItemInserted(position);
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
                                usermessage_Adapter.notifyItemRemoved(position);


                                Snackbar.make(rv,username+":"+"Hidden.",Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                archivechat.remove(archivechat.lastIndexOf(username));
                                                muser.add(details1);
                                                usermessage_Adapter.notifyItemInserted(position);
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



        userssearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                usermessage_Adapter.getFilter().filter(newText);
                return false;
            }
        });


        chatit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(noofchats.size()==0)
                {
                    Toast.makeText(SearchActivity.this, "select user", Toast.LENGTH_SHORT).show();
                    chatit.setVisibility(View.GONE);
                }
                else if(noofchats.get(0)!=null && noofchats.size()==1)
                {
                    chatit.setVisibility(View.VISIBLE);
                    final String firstuser=noofchats.get(0);
                    final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid());
                    mref.keepSynced(true);
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                Friends friends=snapshot.getValue(Friends.class);

                                if(firstuser.equals(friends.getId()))
                                {
                                    flag = 1;
                                }

                            }

                            if(flag==1)
                            {
                                Intent intent=new Intent(getApplicationContext(),FriendsChatpad.class);
                                intent.putExtra("userid",firstuser);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent=new Intent(getApplicationContext(),Dummy_chatpad.class);
                                intent.putExtra("userid",firstuser);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(noofchats.get(0)!=null && noofchats.get(1)!=null && noofchats.size()>1)
                {
                    chatit.setVisibility(View.VISIBLE);
                    Intent intent=new Intent(getApplicationContext(), GroupChatActivity.class);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) noofchats);
                    startActivity(intent);
                    finish();
                }
            }
        });

        muser=new ArrayList<>();
        usersdetailsdisplay();
    }

    private void usersdetailsdisplay()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users");

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
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
                    if (!firebaseUser.getUid().equals(details.getID()))
                    {
                        muser.add(details);
                    }
                }

                usermessage_Adapter=new usermessage_Adapter(getApplicationContext(),muser,SearchActivity.this);
                usermessage_Adapter.notifyDataSetChanged();
                rv.setAdapter(usermessage_Adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemSelected(Details details, boolean d, String id) {

        if(d)
        {
            chip12=new Chip(this);
            chip12.setText(details.getUSERNAME());
            chip12.setChipIcon(getDrawable(details.getIMAGEURL()));
            chip12.setCheckable(false);
            chip12.setClickable(false);
            //       chip12.setCloseIconVisible(true);
            //     chip12.setOnCloseIconClickListener(this);
            chip.addView(chip12);
            chip.setVisibility(View.VISIBLE);
            //   muser.remove(details);
            d=false;
        }
        else
        {
            chip.removeView(chip12);
            //     muser.add(details);
            d=true;
        }

    }

    public Drawable getDrawable(String bitmapUrl)
    {
        try {
            URL url = new URL(bitmapUrl);
            Drawable d =new BitmapDrawable(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
            return d;
        }
        catch(Exception ex) {return null;}
    }

    @Override
    public void applycodes(ArrayList<String> codes,ArrayList<String> muser112) {
        noofchats=codes;
        musers=muser112;
    }

    /*
    @Override
    public void onClick(View view) {

     //   Chip chip13= (Chip) view;
       // chip.removeView(chip13);
     //   muser.add(details);

    }
    */
}



