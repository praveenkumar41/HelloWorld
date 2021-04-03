package com.example.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Adapter.Story_View_Adapter;
import com.example.project2.GroupChat.GroupAdapter;
import com.example.project2.Notification.Token;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class GroupFragment extends Fragment implements View.OnKeyListener{

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PARTITION_CODE = 101;
    RecyclerView recyclerView;
    MediaPlayer mp;

    GroupAdapter groupAdapter;

    List<Group> mgroup;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference1;
    FloatingActionButton add_message;
    TextView roundimagename;
    Bitmap bit;
    static final int PICK_IMAGE = 11;
    public static Bundle bundle = new Bundle();

    private List<String> archivechat;

    private List<String> mfollowing;

    String userid12;

    public GroupFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.group_fragment, container, false);

        archivechat=new ArrayList<>();

        add_message=view.findViewById(R.id.add_message);

        recyclerView = view.findViewById(R.id.recyclerview);
        //   settings=view.findViewById(R.id.settings);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        add_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

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

                        final Group group =mgroup.get(position);

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Groups").child(mgroup.get(position).getGroupid());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String username=dataSnapshot.child("grouptitle").getValue(String.class);

                                archivechat.add(username);

                                mgroup.remove(mgroup.get(position));
                                groupAdapter.notifyItemRemoved(position);

                              //  String username1= username.substring(0, 1).toUpperCase() + username.substring(1);

                                Snackbar snackbar=Snackbar.make(recyclerView,username+" "+"Group"+" "+"Archieved",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setDuration(6000).setActionTextColor(Color.WHITE).setAction("undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        archivechat.remove(archivechat.lastIndexOf(username));
                                        mgroup.add(group);
                                        groupAdapter.notifyItemInserted(position);

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

                        final Group group1=mgroup.get(position);

                        reference=FirebaseDatabase.getInstance().getReference().child("Groups").child(mgroup.get(position).getGroupid());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username=dataSnapshot.child("grouptitle").getValue(String.class);

                                archivechat.add(username);

                                mgroup.remove(mgroup.get(position));
                                groupAdapter.notifyItemRemoved(position);

                                //String username1= username.substring(0, 1).toUpperCase() + username.substring(1);
                                Snackbar snackbar=Snackbar.make(recyclerView,username+" "+"Group"+" "+"Archieved",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setDuration(6000).setActionTextColor(Color.WHITE).setAction("undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        archivechat.remove(archivechat.lastIndexOf(username));
                                        mgroup.add(group1);
                                        groupAdapter.notifyItemInserted(position);

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

        usersdetailsdisplay();

        return view;
    }

    private void usersdetailsdisplay()
    {
        mgroup=new ArrayList<>();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Groups");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mgroup.clear();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                {
                    Group group=Snapshot.getValue(Group.class);

                    if(Snapshot.child("createdby").getValue(String.class).equals(firebaseUser.getUid()) || Snapshot.child("participants").child(firebaseUser.getUid()).exists())
                    {
                        mgroup.add(group);
                    }
                }
                groupAdapter = new GroupAdapter(getContext(), mgroup);
                groupAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(groupAdapter);
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

                Intent intent=new Intent(getContext(),Startapp.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        }
        return false;
    }
}