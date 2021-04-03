package com.example.project2;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

class Following_Adapter extends RecyclerView.Adapter<Following_Adapter.ViewHolder>
{

    private Context mcontext;

    private List<Details> musers;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public Following_Adapter(Context mcontext, List<Details> musers)
    {
        this.mcontext=mcontext;
        this.musers=musers;
    }


    @NonNull
    @Override
    public Following_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.following_item, parent, false);

        return new Following_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Details details=musers.get(position);

        holder.name.setText(details.getUSERNAME());
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Picasso.with(mcontext).load(details.getIMAGEURL()).into(holder.image);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser firebaseUser1=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                mref.keepSynced(true);
                mref.child("follow").child(firebaseUser1.getUid()).child(details.getID()).removeValue();
                mref.child("follow").child(details.getID()).child(firebaseUser1.getUid()).removeValue();

                mref.child("follow_count").child(firebaseUser1.getUid()).child("following").child(details.getID()).removeValue();
                mref.child("follow_count").child(details.getID()).child("followers").child(firebaseUser1.getUid()).removeValue();
            }
        });

    }


    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView atname,name;
        public CircleImageView image;
        public Button btn;


        public ViewHolder(View itemView) {

            super(itemView);
            atname = itemView.findViewById(R.id.atname);
            btn = itemView.findViewById(R.id.btn);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }

    }

}
