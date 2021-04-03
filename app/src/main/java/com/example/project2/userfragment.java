package com.example.project2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.project2.videostream.ImagedisplayAdapter;
import com.example.project2.videostream.MediaObject1;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class userfragment extends Fragment implements View.OnKeyListener {


    ArrayList<MediaObject1> mediaObjects1,mediaObjects;

    private static final int NUMGRID=3;

   SearchView userssearch;
   ImageView addfriend;
   GridView gridview;
   Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_user, container, false);


        addfriend=v.findViewById(R.id.addfriend);

        gridview=v.findViewById(R.id.gridview);

        userssearch= v.findViewById(R.id.userssearch);
        userssearch.setBackgroundResource(android.R.color.transparent);

        userssearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b)
                {
                    Intent intent=new Intent(getContext(),Searchusers_friends.class);
                    startActivity(intent);
                    userssearch.clearFocus();
                }
            }
        });

        initRecyclerView();

        return v;
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

    private void initRecyclerView() {
        mediaObjects1 = new ArrayList<>();
        mediaObjects = new ArrayList<>();

        DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference().child("posts");
        reference11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mediaObjects1.clear();
                mediaObjects.clear();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    MediaObject1 mediaObject = Snapshot.getValue(MediaObject1.class);

                    if(!"text".equals(mediaObject.getType()))
                    {
                        mediaObjects1.add(mediaObject);
                    }

                }

                Collections.reverse(mediaObjects1);

                int gridwith=getResources().getDisplayMetrics().widthPixels;
                int imgwidth=gridwith/NUMGRID;


                gridview.setColumnWidth(imgwidth);


                UserFragmentAdapter adapter = new UserFragmentAdapter(getContext(), mediaObjects1);
                adapter.notifyDataSetChanged();


                gridview.setAdapter(adapter);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {

                        Intent intent=new Intent(getContext(), PostViewer_Activity.class);
                        intent.putExtra("bitmap",mediaObjects1.get(position).getBitmap());
                        intent.putExtra("description",mediaObjects1.get(position).getDescription());
                        intent.putExtra("imageurl",mediaObjects1.get(position).getImageurl());
                        intent.putExtra("postid",mediaObjects1.get(position).getPushid());
                        intent.putExtra("publisher",mediaObjects1.get(position).getPublisher());
                        intent.putExtra("cardtype",mediaObjects1.get(position).getType());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

/*

    private void searchusers(final String query)
    {
         firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("users");
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
                    assert firebaseUser != null;
                    if (!firebaseUser.getUid().equals(details.getID()))
                    {
                        if(details.getUSERNAME().toLowerCase().contains(query.toLowerCase())){
                            muser.add(details);
                        }

                    }
                }
                userAdapter=new UserAdapter(getContext(),muser);
                userAdapter.notifyDataSetChanged();
                recyclerview.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    private void usersdetailsdisplay()
    {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

       DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("usersdisplay");

      //  DatabaseReference mm=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("users");


        reference.keepSynced(true);
   //     final DatabaseReference mrefy=FirebaseDatabase.getInstance().getReference().child("Friends");

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

                userAdapter=new UserAdapter(getContext(),muser);
                userAdapter.notifyDataSetChanged();
                recyclerview.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
*/
}