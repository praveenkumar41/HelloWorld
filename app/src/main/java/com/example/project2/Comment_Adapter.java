package com.example.project2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder>
{


    private Context mcontext;

    private List<Comments> mcomments;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public Comment_Adapter(Context mcontext, List<Comments> mcomments)
    {
        this.mcontext=mcontext;
        this.mcomments=mcomments;
    }

    @NonNull
    @Override
    public Comment_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.comment_item, parent, false);

        return new Comment_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Comments comments=mcomments.get(position);

        holder.text.setText(comments.getComment());
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String name=dataSnapshot.child("USERNAME_ID").getValue(String.class);
                String image=dataSnapshot.child("IMAGEURL").getValue(String.class);

                holder.name.setText(name);
                Picasso.with(mcontext).load(image).into(holder.image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return mcomments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text,name;
        public CircleImageView image;


        public ViewHolder(View itemView) {

            super(itemView);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }

    }

}
