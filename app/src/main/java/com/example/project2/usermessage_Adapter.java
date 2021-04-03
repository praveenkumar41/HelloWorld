package com.example.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class usermessage_Adapter extends RecyclerView.Adapter<usermessage_Adapter.ViewHolder> implements Filterable{

    public Context mcontext;
    private List<Details> muser;
    FirebaseUser firebaseUser;
    ArrayList<Details> muser1;

    private gettinguserstochat chatlistener;

    int flag=0;
    ArrayList<String> forgroupchats = null;
    ArrayList<String> alluserids=null;
    boolean s=false;
    private RecyclerItemSelectedListener itemSelectedListener;

    public usermessage_Adapter(Context mcontext11, List<Details> muser, SearchActivity searchActivity)
    {
        this.mcontext=mcontext11;
        this.muser= muser;
        this.muser1=new ArrayList<>(muser);
        this.itemSelectedListener=(SearchActivity)searchActivity;
        this.chatlistener=(SearchActivity) searchActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cardlayout_usermessage, parent, false);
        return new usermessage_Adapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final Details details=muser.get(position);

        holder.un.setText(details.getUSERNAME());
        holder.un1.setText("@"+details.getUSERNAME_ID());
        holder.setuserimage(mcontext,details.getIMAGEURL());

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid());
                mref.keepSynced(true);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Friends friends=snapshot.getValue(Friends.class);

                            if(muser.get(position).getID().equals(friends.getId()))
                            {
                                flag = 1;
                            }

                        }

                        if(flag==1)
                        {
                            Intent intent=new Intent(mcontext,FriendsChatpad.class);
                            intent.putExtra("userid",muser.get(position).getID());
                            mcontext.startActivity(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(mcontext,Dummy_chatpad.class);
                            intent.putExtra("userid",muser.get(position).getID());
                            mcontext.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
       });
*/
        alluserids();
        forgroupchats=new ArrayList<>();

        if(forgroupchats.contains(muser.get(position).getID()))
        {
            holder.checkbox.setChecked(true);
        }else {
            //checkBox.setChecked(false); //has animation
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {

                if(b)
                {
                    itemSelectedListener.onItemSelected(muser.get(position),true,muser.get(position).getUSERNAME());
                    forgroupchats.add(muser.get(position).getID());
                    Toast.makeText(mcontext, muser.get(position).getID()+"-"+"Added", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    itemSelectedListener.onItemSelected(muser.get(position),false,muser.get(position).getUSERNAME());
                    forgroupchats.remove(muser.get(position).getID());
                    Toast.makeText(mcontext, muser.get(position).getID()+"-"+"Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatlistener.applycodes(forgroupchats,alluserids);

    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Details> filteredlist=new ArrayList<>();

            if(charSequence.toString().isEmpty())
            {
                filteredlist.addAll(muser1);
            }
            else
            {
                String names=charSequence.toString().toLowerCase().trim();
                for (Details id:muser1)
                {
                    if(id.getUSERNAME().toLowerCase().contains(names))
                    {
                        filteredlist.add(id);
                    }
                }
            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            muser.clear();
            muser.addAll((Collection<? extends Details>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView un,un1;
        public CircleImageView imageenter1;
        RelativeLayout rootview;
        CheckBox checkbox;

        public ViewHolder(View itemView)
        {

            super(itemView);
            un=itemView.findViewById(R.id.un);
            un1=itemView.findViewById(R.id.un1);
            imageenter1=itemView.findViewById(R.id.imageenter1);
            checkbox=itemView.findViewById(R.id.checkbox);
            rootview=itemView.findViewById(R.id.rootview);
            rootview.setOnClickListener(this);

            //     final Details item = muser.get(position);
            // text.setText(muser.get(getAdapterPosition()).getID());


        }


        public void setuserimage(Context mcontext, String imageurl) {

            Picasso.with(mcontext).load(imageurl).placeholder(R.drawable.pro).into(imageenter1);
        }

        @Override
        public void onClick(View view) {


            //    int pos=muser.indexOf(muser.get(getAdapterPosition()));
            //      itemSelectedListener.onItemSelected(muser.get(getAdapterPosition()),true,muser.get(getAdapterPosition()).getUSERNAME(),pos);
            //forgroupchats=new ArrayList<>();
            // itemSelectedListener.onItemSelected(muser.get(getAdapterPosition()),false);
            //forgroupchats.add(muser.get(getAdapterPosition()).getID());

            //Toast.makeText(mcontext, muser.get(getAdapterPosition()).getID(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface RecyclerItemSelectedListener
    {
        void onItemSelected(Details details, boolean des, String id);
    }

    public void alluserids()
    {
        alluserids=new ArrayList<>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alluserids.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Details details=snapshot.getValue(Details.class);
                    alluserids.add(details.getUSERNAME());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface gettinguserstochat
    {
        void applycodes(ArrayList<String> codes,ArrayList<String> alluserids);
    }

}



