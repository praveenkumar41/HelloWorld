package com.example.project2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.AddStoryActivity;
import com.example.project2.Alltype_Story;
import com.example.project2.Details;
import com.example.project2.Post_Timeago;
import com.example.project2.R;
import com.example.project2.Stories;
import com.example.project2.StoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.media2.MediaUtils.TAG;

public class Story_View_Adapter extends RecyclerView.Adapter<Story_View_Adapter.ViewHolder>
{
    private Context mcontext;
    private List<Stories> mstory;
    DatabaseReference mref,mref1;

    public Story_View_Adapter(Context mcontext,List<Stories> mstory)
    {
        this.mcontext=mcontext;
        this.mstory=mstory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType==0){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.add_story_item,parent,false);
            return new Story_View_Adapter.ViewHolder(view);
        }else {
            View view=LayoutInflater.from(mcontext).inflate(R.layout.roundcardlayout,parent,false);
            return new Story_View_Adapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Stories stories=mstory.get(position);

        userinformation(holder,stories.getUserid(),position);

        if(holder.getAdapterPosition()!=0)
        {
            seenStory(holder,stories.getUserid());
        }
        if(holder.getAdapterPosition()==0)
        {
            myStory(holder.addstory_text,holder.story_plus,false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.getAdapterPosition()==0)
                {
                    myStory(holder.addstory_text,holder.story_plus,true);
                }else
                {
                   Intent intent=new Intent(mcontext, StoryActivity.class);
                   intent.putExtra("userid",stories.getUserid());
                   intent.putExtra("storyid",stories.getStoryid());
                   long timestart=stories.getTimestart();
                    Post_Timeago timeago = new Post_Timeago();
                   String Lastseentime =timeago.Post_Timeago(timestart, mcontext);
                   intent.putExtra("timestart",Lastseentime);
                   mcontext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mstory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CircleImageView story_photo,story_plus,story_photo_seen;
        public TextView story_username,addstory_text;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            story_photo=itemView.findViewById(R.id.story_photo);
            story_plus=itemView.findViewById(R.id.story_plus);
            story_photo_seen=itemView.findViewById(R.id.story_photo_seen);
            story_username=itemView.findViewById(R.id.story_username);
            addstory_text=itemView.findViewById(R.id.addstory_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return  0;
        }
        return 1;
    }

    private void userinformation(final ViewHolder viewHolder,final String userid,final int pos)
    {
        mref= FirebaseDatabase.getInstance().getReference("users").child(userid);
        mref.keepSynced(true);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Details details=dataSnapshot.getValue(Details.class);
                Picasso.with(mcontext).load(details.getIMAGEURL()).into(viewHolder.story_photo);
                Log.d(TAG, "blue color changes");

                if(pos!=0)
                {
                    Picasso.with(mcontext).load(details.getIMAGEURL()).into(viewHolder.story_photo_seen);
                    Log.d(TAG, "grey color changes");
                    viewHolder.story_username.setText(details.getUSERNAME());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void myStory(final TextView textView, final ImageView imageView, final boolean click)
    {

        mref1= FirebaseDatabase.getInstance().getReference().child("story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mref1.keepSynced(true);
        mref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                int count=0;
                long timecurrent=System.currentTimeMillis();

                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Stories stories=snapshot.getValue(Stories.class);
                    if(timecurrent > stories.getTimestart() && timecurrent < stories.getTimeend())
                    {
                        count++;
                    }
                }

                if(click)
                {
                    if(count > 0)
                    {
                        AlertDialog alertDialog=new AlertDialog.Builder(mcontext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent=new Intent(mcontext, StoryActivity.class);
                                intent.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mcontext.startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(mcontext, Alltype_Story.class);
                                mcontext.startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });

                        alertDialog.show();

                    }else {
                        Intent intent=new Intent(mcontext, Alltype_Story.class);
                        mcontext.startActivity(intent);
                    }
                }else{
                    if(count>0)
                    {
                        textView.setText("My Story");
                        imageView.setVisibility(View.GONE);
                    }
                    else
                    {
                        textView.setText("Add Story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenStory(final ViewHolder viewHolder, String userid)
    {
        mref1= FirebaseDatabase.getInstance().getReference().child("story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mref1.keepSynced(true);
        mref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                int i=0;

                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    if(!snapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() && System.currentTimeMillis() < snapshot.getValue(Stories.class).getTimeend())
                    {
                        i++;
                    }
                }

                if(i>0)
                {
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.story_photo_seen.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.story_photo.setVisibility(View.GONE);
                    viewHolder.story_photo_seen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}