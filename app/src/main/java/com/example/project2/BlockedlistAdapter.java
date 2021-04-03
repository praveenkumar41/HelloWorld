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

public class BlockedlistAdapter extends RecyclerView.Adapter<BlockedlistAdapter.ViewHolder>{

    private Context mcontext;
    private List<Blockedlist> mblock;
    private DatabaseReference mref2,mref1,mref21;
    FirebaseUser firebaseUser;
    private BlockedlistAdapter context=this;



    public BlockedlistAdapter(Context mcontext, List<Blockedlist> mblock)
    {
        this.mcontext=mcontext;
        this.mblock= mblock;
    }


    @NonNull
    @Override
    public BlockedlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cardlayoutblockeduser, parent, false);

        return new BlockedlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String user_id=mblock.get(position).getId();

       mref2= FirebaseDatabase.getInstance().getReference().child("Friends").child(user_id);
        mref21= FirebaseDatabase.getInstance().getReference().child("Friends");

        mref1= FirebaseDatabase.getInstance().getReference("users").child(user_id);

       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final Blockedlist blockedlist=mblock.get(position);


        mref1.addValueEventListener(new ValueEventListener() {
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



     holder.unblockoption.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             final AlertDialog.Builder alertdialog =new AlertDialog.Builder(view.getRootView().getContext());
             alertdialog.setTitle("Do you want to unblock this user");

             alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i)
                 {

                     mref2.child(firebaseUser.getUid()).child("blockeduser").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             mref21.child(firebaseUser.getUid()).child(user_id).child("blockeduser").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful())
                                     {
                                         Toast.makeText( mcontext,"User unBlocked", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });


                         }
                     });

                 }
             });

             alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.cancel();
                 }
             });
             alertdialog.create();
             alertdialog.show();
         }
     });

    }

    @Override
    public int getItemCount() {
        return mblock.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView un;
        public CircleImageView imageenter1;
        ImageView unblockoption;

        public ViewHolder(View itemView)
        {
            super(itemView);

            un=itemView.findViewById(R.id.un);
            imageenter1=itemView.findViewById(R.id.imageenter1);
            unblockoption=itemView.findViewById(R.id.unblockoption);

        }


        public void setUserimage(String image, Context mcontext) {

            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
        }
    }

}
