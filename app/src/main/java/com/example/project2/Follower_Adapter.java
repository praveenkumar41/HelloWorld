package com.example.project2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Notification.Client;
import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification2.APIService2;
import com.example.project2.Notification2.Data2;
import com.example.project2.Notification2.Sender2;
import com.example.project2.Notification2.Token2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

class Follower_Adapter extends RecyclerView.Adapter<Follower_Adapter.ViewHolder>
{

    private Context mcontext;

    private List<Details> musers;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    boolean notify=false;

    APIService1 apiService;
    APIService2 api2;


    public Follower_Adapter(Context mcontext, List<Details> musers)
    {
        this.mcontext=mcontext;
        this.musers=musers;
    }


    @NonNull
    @Override
    public Follower_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.follower_item, parent, false);

        return new Follower_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService1.class);
        api2 = Client.getClient("https://fcm.googleapis.com/").create(APIService2.class);


        final Details details=musers.get(position);

        holder.name.setText(details.getUSERNAME());
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Picasso.with(mcontext).load(details.getIMAGEURL()).into(holder.image);

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("follow_count").child(firebaseUser.getUid()).child("following");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    if(snapshot.exists())
                    {
                        holder.btn.setText("UNFOLLOW");
                    }
                    else
                    {
                        holder.btn.setText("FOLLOW");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.btn.getText().toString().equals("FOLLOW"))
                {
                    notify = true;
                    final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("follow");

                    mref.keepSynced(true);

                    mref.child(firebaseUser.getUid()).child(details.getID()).child("following").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mref.child(firebaseUser.getUid()).child(details.getID()).child("follower").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mref.child(firebaseUser.getUid()).child(details.getID()).child("id").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mref.child(firebaseUser.getUid()).child(details.getID()).child("toggle").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    mref.child(details.getID()).child(firebaseUser.getUid()).child("following").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            mref.child(details.getID()).child(firebaseUser.getUid()).child("follower").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    mref.child(details.getID()).child(firebaseUser.getUid()).child("id").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            mref.child(details.getID()).child(firebaseUser.getUid()).child("toggle").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    Toast.makeText(mcontext, "Following", Toast.LENGTH_SHORT).show();


                                                                                }
                                                                            });


                                                                        }
                                                                    });


                                                                }
                                                            });


                                                        }
                                                    });


                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });


                    DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(details.getID());

                    mref6.keepSynced(true);

                    final HashMap typingstatus = new HashMap();
                    typingstatus.put("usertyping", "false");
                    mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });

                    mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(details.getID()).child(firebaseUser.getUid());

                    final HashMap typingstatus1 = new HashMap();
                    typingstatus.put("usertyping", "false");
                    mref6.setValue(typingstatus1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });

                    DatabaseReference mref11=FirebaseDatabase.getInstance().getReference();

                    mref11.keepSynced(true);

                    mref11.child("follow_count").child(firebaseUser.getUid()).child("following").child(details.getID()).setValue("true");
                    mref11.child("follow_count").child(details.getID()).child("followers").child(firebaseUser.getUid()).setValue("true");


                    final String msg = "Started following you";
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                    reference.keepSynced(true);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Details details = dataSnapshot.getValue(Details.class);
                            if (notify) {
                                sendNotification2(details.getID(), details.getUSERNAME(), msg);
                            }
                            notify = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    holder.btn.setText("unfollow");

                } else {

                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                    mref.keepSynced(true);
                    mref.child("follow").child(firebaseUser.getUid()).child(details.getID()).removeValue();
                    mref.child("follow").child(details.getID()).child(firebaseUser.getUid()).removeValue();


                    mref.child("follow_count").child(firebaseUser.getUid()).child("following").child(details.getID()).removeValue();
                    mref.child("follow_count").child(details.getID()).child("followers").child(firebaseUser.getUid()).removeValue();


                }
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


    private void sendNotification2(final String userid, final String username, final String msg) {

        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=tokens.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Token2 token=snapshot.getValue(Token2.class);
                    Data2 data=new Data2(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+msg,"Friend Request",userid);

                    Sender2 sender2=new Sender2(data,token.getToken());

                    api2.sendNotification(sender2)
                            .enqueue(new retrofit2.Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(mcontext, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
