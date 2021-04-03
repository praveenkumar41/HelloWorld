
package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class suggestedAdapter extends RecyclerView.Adapter<suggestedAdapter.ViewHolder>
{

    private Context mcontext;

    private List<Friends> mfriends;

    DatabaseReference reference;
    DatabaseReference mref10;
    DatabaseReference mref;
    DatabaseReference mref2,myrefry;
    FirebaseUser firebaseUser;
    String suserid;//=FirebaseAuth.getInstance().getCurrentUser();


    public suggestedAdapter(Context mcontext, List<Friends> mfriends, String userid)
    {
        this.mcontext=mcontext;
        this.mfriends= mfriends;
        this.suserid=userid;
    }



    @NonNull
    @Override
    public suggestedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.suggestedcardlayout, parent, false);
        return new suggestedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String user_id = mfriends.get(position).getId();

      //  final Friends friends = mfriends.get(position);
/*        mref2 = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

  //      String userid = mfriends.get(position).getId();
        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String image = dataSnapshot.child("IMAGEURL").getValue(String.class);

                assert name != null;
                String name1=name.substring(0,1).toUpperCase()+name.substring(1);

                holder.sname.setText(name1);
                holder.setUserimage(image, mcontext);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
    }


    @Override
    public int getItemCount() {
        return mfriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView sname;
        public CircleImageView simage;
        Button saddfriend;

        public ViewHolder(View itemView)
        {

            super(itemView);

            simage=itemView.findViewById(R.id.simage);
            sname=itemView.findViewById(R.id.sname);
            saddfriend=itemView.findViewById(R.id.saddfriend);

        }

        public void setUserimage(String image, Context mcontext) {
            //simage=itemView.findViewById(R.id.simage);
            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(simage);
        }

    }

}


