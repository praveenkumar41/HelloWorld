package com.example.project2.GroupChat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project2.Chats;
import com.example.project2.Friends;
import com.example.project2.FriendsChatpad;
import com.example.project2.Friends_request;
import com.example.project2.Group;
import com.example.project2.Otheruserswithmusicprofile;
import com.example.project2.R;
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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    String lastmessages;
    String time;
    private Context mcontext;
    List<Group> mgroup;

    public GroupAdapter(Context mcontext, List<Group> mgroups) {
        this.mcontext = mcontext;
        this.mgroup = mgroups;
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.groupcardlayout, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {

            holder.un.setText(mgroup.get(position).getGrouptitle());
            holder.setUserimage(mgroup.get(position).getGroupimage(), mcontext);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mgroup.size()!=0)
                    {
                        Intent intent = new Intent(mcontext,GroupChatpad.class);
                        intent.putExtra("groupid",mgroup.get(position).getGroupid());
                        mcontext.startActivity(intent);
                    }
                }
            });
        }


    @Override
    public int getItemCount() {
        return mgroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView un, lastmessagetext, timetext;
        public CircleImageView imageenter1;

        public ViewHolder(View itemView) {

            super(itemView);
            un = itemView.findViewById(R.id.un);
            imageenter1 = itemView.findViewById(R.id.imageenter1);
            lastmessagetext = itemView.findViewById(R.id.lastmessagetext);
            timetext = itemView.findViewById(R.id.timetext);
        }

        public void setUserimage(String image, Context mcontext) {

            Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
        }
    }
}