package com.example.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Story_seen_List_Adapter extends RecyclerView.Adapter<Story_seen_List_Adapter.ViewHolder>{


    private Context mcontext;
    private List<String> mviews;
    private List<Friends> mfriend;
    private DatabaseReference mref2,mref1,mref21;
    FirebaseUser firebaseUser;
    String userid;

    private Story_seen_List_Adapter context=this;

    public Story_seen_List_Adapter(Context mcontext, List<String> mviews, String userid)
    {
        this.mcontext=mcontext;
        this.mviews=mviews;
        this.userid=userid;

    }

    public Story_seen_List_Adapter(String userids)
    {
        userid=userids;
    }


    @NonNull
    @Override
    public Story_seen_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.story_seen_list_items, parent, false);
        return new Story_seen_List_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       // final String user_id=mviews.get(position).getId();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

       // mref2= FirebaseDatabase.getInstance().getReference().child("Friends").child(user_id);
        mref21= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref2= FirebaseDatabase.getInstance().getReference("users").child("Friends");

        mref21.keepSynced(true);
        mref2.keepSynced(true);

              final DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("users");

/*
                reference1.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name= dataSnapshot.child("USERNAME").getValue().toString();
                        String image= dataSnapshot.child("IMAGEURL").getValue().toString();

                        holder.un.setText(name);
                        holder.setUserimage(image,mcontext);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

*/


                reference1.keepSynced(true);
                reference1.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("USERNAME").getValue().toString();
                        String imageurl=dataSnapshot.child("IMAGEURL").getValue().toString();
                        holder.un.setText(name);
                        holder.setUserimage(imageurl,mcontext);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


    @Override
    public int getItemCount() {
        return mviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView un;
        public CircleImageView imageenter1;


        public ViewHolder(View itemView)
        {
            super(itemView);

            un=itemView.findViewById(R.id.un);
            imageenter1=itemView.findViewById(R.id.imageenter1);
        }


        public void setUserimage(String image, Context mcontext) {

            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
        }

    }

}
