package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.util.Freezable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project2.videostream.MediaObject;
import com.example.project2.videostream.MediaObject1;
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

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>
{

    String lastmessages;
    String time;
    Boolean seentexts;
    String pushid;
    boolean seentexts1;
    private Context mcontext;

    private List<Friends> mfriends;
    private List<String> mfriends1;

    private List<Friends_request> mfriendrequest;

    DatabaseReference unreadref,ref,typingref,unread11;
    String snapshotref;
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    String userid112;
    final Boolean[] mm11 = new Boolean[1];


    public FriendsAdapter(Context mcontext, List<Friends> mfriends, String userid112)
    {
        this.mcontext=mcontext;
        this.mfriends= mfriends;
        this.userid112=userid112;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cardlayout, parent, false);

        return new FriendsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {

        final String user_id=mfriends.get(position).getId();

        final DatabaseReference mref2= FirebaseDatabase.getInstance().getReference("users");
        mref2.keepSynced(true);
        mref2.child(mfriends.get(position).getFollowing()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                          String name=dataSnapshot.child("USERNAME").getValue(String.class);

                          holder.un.setText(name);

                          String image= dataSnapshot.child("IMAGEURL").getValue(String.class);
                          holder.setUserimage(image,mcontext);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        ref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
        ref.keepSynced(true);


        typingref=FirebaseDatabase.getInstance().getReference().child("Typingstatus");
        typingref.keepSynced(true);


        typingref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String typingstatus=dataSnapshot.child(user_id).child(firebaseUser.getUid()).child("usertyping").getValue(String.class);

                if("true".equals(typingstatus))
                {
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
                            String switchonoff = dataSnapshot.child("TOGGLE").getValue(String.class);

                            if (switchonoff != null) {
                                if("ON".equals(switchonoff)){
                                    thelastmessages11(mfriends.get(position).getId(),holder.lastmessagetext,holder.timetext);
                                }
                                else{
                                    holder.lastmessagetext.setTypeface(Typeface.SANS_SERIF);
                                   // String id=mfriends.get(position).getId();
                                    thelastmessages(mfriends.get(position).getId(),holder.lastmessagetext,holder.timetext);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

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
                intent.putExtra("userid",mfriends.get(position).getId());
                mcontext.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext,FriendsChatpad.class);
                intent.putExtra("userid",mfriends.get(position).getId());
                mcontext.startActivity(intent);

            }
        });
    }

    private void thelastmessages11(final String id, final TextView lastmessagetext, final TextView timetext)
    {

        final Boolean[] mm = new Boolean[1];
        final String[] type = new String[1];

        lastmessages = "default";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        unreadref = FirebaseDatabase.getInstance().getReference().child("Chats");
        unreadref.keepSynced(true);

        unreadref.addValueEventListener(new ValueEventListener()
        {
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

                        type[0] = chats.getType();
                    }
                }

                switch (lastmessages) {
                    case "default":
                        lastmessagetext.setText("Start a Conversation");
                        break;

                    default:

                        if (mm[0] != null)
                        {
                            if (mm[0])
                            {
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
                            }
                            else {
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

        unreadref = FirebaseDatabase.getInstance().getReference().child("Chats");
        unreadref.keepSynced(true);

        unreadref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
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



    @Override
    public int getItemCount() {
        return mfriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        public TextView un,lastmessagetext,timetext;
        public CircleImageView imageenter1;

        public ViewHolder(View itemView)
        {

            super(itemView);
            un=itemView.findViewById(R.id.un);
            imageenter1=itemView.findViewById(R.id.imageenter1);
            lastmessagetext=itemView.findViewById(R.id.lastmessagetext);
            timetext=itemView.findViewById(R.id.timetext);
        }

        public void setUserimage(String image, Context mcontext) {
     //       imageenter1=itemView.findViewById(R.id.imageenter1);
            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
        }
    }
}


