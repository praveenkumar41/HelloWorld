package com.example.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Notification.Client;
import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification1.Data1;
import com.example.project2.Notification1.Sender1;
import com.example.project2.Notification1.Token1;
import com.example.project2.Notification2.APIService2;
import com.example.project2.Notification2.Data2;
import com.example.project2.Notification2.Sender2;
import com.example.project2.Notification2.Token2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    private Context mcontext;
    private List<String> mlike;

    public LikesAdapter(Context mcontext, List<String> muser)
    {
        this.mcontext=mcontext;
        this.mlike= muser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cardlayoutusers, parent, false);

        return new LikesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child(mlike.get(position));
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String image=dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name=dataSnapshot.child("USERNAME").getValue(String.class);
                String nameid=dataSnapshot.child("USERNAME_ID").getValue(String.class);

                holder.un.setText(name);
                holder.un1.setText("@"+nameid);
                holder.setuserimage(mcontext,image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref11=FirebaseDatabase.getInstance().getReference("users").child(mlike.get(position));
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("follow_count").child(firebaseUser.getUid()).child("following");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if(!ds.getValue(String.class).equals(firebaseUser.getUid()))
                            {
                                if(ds.getValue(String.class).equals(mlike.get(position)))
                                {
                                    holder.addit.setText("FOLLOW");
                                }
                                else
                                {
                                    holder.addit.setText("UNFOLLOW");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        // checking_if_liker_is_already_a_follower(holder.addit);


    }

    @Override
    public int getItemCount() {
        return mlike.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView un,un1;
        public CircleImageView imageenter1;
        Button addit;

        public ViewHolder(View itemView)
        {
            super(itemView);
            un=itemView.findViewById(R.id.un);
            un1=itemView.findViewById(R.id.un1);
            imageenter1=itemView.findViewById(R.id.imageenter1);
            addit=itemView.findViewById(R.id.addit);
        }
        public void setuserimage(Context mcontext, String imageurl) {

            Picasso.with(mcontext).load(imageurl).placeholder(R.drawable.pro).into(imageenter1);
        }
    }
    /*
    public void checking_if_liker_is_already_a_follower(final Button addit)
    {

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        for(int i=0;i<mlike.size();i++)
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("follow_count").child(firebaseUser.getUid()).child("following");

            final int finalI = i;
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class).equals(mlike.get(finalI)))
                    {
                        addit.setText("FOLLOW");
                    }
                    else
                    {
                        addit.setText("UNFOLLOW");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    */

}
