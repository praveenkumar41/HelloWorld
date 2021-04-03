package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {
    String lastmessages;
    String time;
    Boolean seentexts;
    String pushid;
    boolean seentexts1;
    private Context mcontext;
    DatabaseReference reference;
    DatabaseReference mref10;
    DatabaseReference mref;
    DatabaseReference mref2,myrefry;
    DatabaseReference datadetails;
    DatabaseReference datadetails1;
    DatabaseReference unreadref,ref,typingref;
    String snapshotref;
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


    ArrayList<Friends> data = new ArrayList<>();


    public VerticalAdapter(ArrayList<Friends>data) {
        this.data=data;
    }

    @Override
    public VerticalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VerticalAdapter.MyViewHolder holder, final int position)
    {

/*
        final Friends friends= data.get(position);
        mref2= FirebaseDatabase.getInstance().getReference("users");

        String userid= data.get(position).getId();
        mref2.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name= dataSnapshot.child("USERNAME").getValue().toString();
                String image= dataSnapshot.child("IMAGEURL").getValue().toString();

                String name1=name.substring(0,1).toUpperCase()+name.substring(1);

                holder.un.setText(name1);
                holder.setUserimage(image,mcontext);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
        ref.keepSynced(true);


        typingref=FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(friends.getId()).child(firebaseUser.getUid());
        typingref.keepSynced(true);

        typingref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String typingstatus=dataSnapshot.child("usertyping").getValue().toString();

                if("true".equals(typingstatus)){

                    Typeface ff=holder.lastmessagetext.getTypeface();
                    holder.lastmessagetext.setText("Typing...");
                    holder.lastmessagetext.setTypeface(holder.lastmessagetext.getTypeface(), Typeface.BOLD);
                    holder.timetext.setText("");

                }
                else{
                    holder.lastmessagetext.setTypeface(Typeface.SANS_SERIF);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String switchonoff = dataSnapshot.child("TOGGLE").getValue().toString();

                            if (switchonoff != null) {
                                if("ON".equals(switchonoff)){
                                    thelastmessages11(((Friends) data.get(position)).getId(),holder.lastmessagetext,holder.timetext);
                                }
                                else{
                                    holder.lastmessagetext.setTypeface(Typeface.SANS_SERIF);
                                    thelastmessages(((Friends) data.get(position)).getId(),holder.lastmessagetext,holder.timetext);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.imageenter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext,Otheruserswithmusicprofile.class);
                intent.putExtra("userid",friends.getId());

                mcontext.startActivity(intent);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext,FriendsChatpad.class);
                mref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String toggleonoffswitch=dataSnapshot.child("TOGGLE").getValue().toString();

                        if("ON".equals(toggleonoffswitch)) {

                            mref10 = FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid()).child(friends.getId());
                            mref10.child("toggle").setValue("ON").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                intent.putExtra("userid",friends.getId());
                mcontext.startActivity(intent);
            }
        });
*/
    }

    private void thelastmessages11(final String id, final TextView lastmessagetext, final TextView timetext) {

        final Boolean[] mm = new Boolean[1];
        final String[] type = new String[1];

        lastmessages = "default";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        unreadref = FirebaseDatabase.getInstance().getReference().child("Chats").child(firebaseUser.getUid()).child(id);
        unreadref.keepSynced(true);


        unreadref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chats chats = snapshot.getValue(Chats.class);
                    assert chats != null;
                    if (firebaseUser.getUid().equals(chats.getReceiverid()) && (id.equals(chats.getSender()))) {

                        lastmessages = chats.getMessage();
                        time = chats.getTime();
                        mm[0] = chats.isUnreadtoggleon();
                        type[0] = chats.getType();
                    }
                }




                switch (lastmessages) {
                    case "default":
                        lastmessagetext.setText("Start a Conversation");
                        break;

                    default:

                        if (mm[0] != null) {
                            if (mm[0]) {
                                if ("text".equals(type[0]) || "link".equals(type[0]) || "url".equals(type[0])) {

                                    lastmessagetext.setText(lastmessages);
                                    timetext.setText(time);
                                    break;

                                } else if ("image".equals(type[0])) {
                                    lastmessagetext.setText("photo");
                                    timetext.setText(time);
                                    break;

                                } else {
                                    lastmessagetext.setText("Audio");
                                    timetext.setText(time);
                                    break;

                                }

                            } else {
                                if ("text".equals(type[0]) || "link".equals(type[0]) || "url".equals(type[0])) {
                                    lastmessagetext.setText(lastmessages);
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setText(time);
                                    break;

                                } else if ("image".equals(type[0])) {
                                    lastmessagetext.setText("photo");
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setText(time);
                                    break;

                                } else {
                                    lastmessagetext.setText("Audio");
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    timetext.setText(time);
                                    break;

                                }


                            }

                        }
                        lastmessages = "default";
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void thelastmessages(final String id, final TextView timetext, final TextView lastmessagetext)
    {

        final Boolean[] mm1 = new Boolean[1];
        final String[] type = new String[1];

        lastmessages="default";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        unreadref = FirebaseDatabase.getInstance().getReference().child("Chatsclone").child(firebaseUser.getUid()).child(id);


        unreadref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    assert chats != null;
                    if (firebaseUser.getUid().equals(chats.getReceiverid()) && (id.equals(chats.getSender())))
                    {

                        lastmessages = chats.getMessage();
                        time = chats.getTime();

                        type[0]=chats.getType();

                    }
                }

                switch (lastmessages)
                {
                    case "default":
                        timetext.setText("Start a Conversation");
                        break;

                    default:

                        if(mm1[0] !=null) {
                            if (mm1[0])
                            {
                                if("text".equals(type[0])||"link".equals(type[0])||"url".equals(type[0])){

                                    timetext.setText(lastmessages);
                                    lastmessagetext.setText(time);
                                    break;

                                }
                                else if("image".equals(type[0])){
                                    timetext.setText("Photo");
                                    lastmessagetext.setText(time);
                                    break;
                                }
                                else{
                                    timetext.setText("Audio");
                                    lastmessagetext.setText(time);
                                    break;
                                }

                            } else {

                                if("text".equals(type[0])||"link".equals(type[0])||"url".equals(type[0])){

                                    timetext.setText(lastmessages);
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setText(time);
                                    break;

                                }
                                else if("image".equals(type[0])){

                                    timetext.setText("Photo");
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setText(time);
                                    break;

                                }
                                else{
                                    timetext.setText("Audio");
                                    timetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setTypeface(timetext.getTypeface(), Typeface.BOLD);
                                    lastmessagetext.setText(time);
                                    break;

                                }

                            }
                        }

                }
                lastmessages="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    ;

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView un,lastmessagetext,timetext;
        public CircleImageView imageenter1;

        public MyViewHolder(View itemView) {
            super(itemView);
            un=itemView.findViewById(R.id.un);
            imageenter1=itemView.findViewById(R.id.imageenter1);
            lastmessagetext=itemView.findViewById(R.id.lastmessagetext);
            timetext=itemView.findViewById(R.id.timetext);

        }

  /*      public void setUserimage(String image, Context mcontext)
        {
            imageenter1=itemView.findViewById(R.id.imageenter1);
            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
        }
        */
    }

}