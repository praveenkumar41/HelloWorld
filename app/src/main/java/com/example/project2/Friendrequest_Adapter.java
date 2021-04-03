
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.Notification.Client;
import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification1.Data1;
import com.example.project2.Notification1.Sender1;
import com.example.project2.Notification1.Token1;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

class FriendRequest_Adapter extends RecyclerView.Adapter<FriendRequest_Adapter.ViewHolder>
{


    private Context mcontext;

    private List<Friends_request> mfriendrequest;

    DatabaseReference reference;

    String userid;
    DatabaseReference mref,mref1;
    DatabaseReference mref2,myrefry,ref;
    FirebaseUser firebaseUser;

    String username;
    String current;

    String currentstate;
    int count;

    APIService2 api2;
    boolean notify=false;


    public FriendRequest_Adapter(Context mcontext, List<Friends_request> mfriendrequest, String userid1, int count1)
    {
        this.mcontext=mcontext;
        this.mfriendrequest= mfriendrequest;
       // this.userid=userid1;
        this.count=count1;
    }


    @NonNull
    @Override
    public FriendRequest_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.request_cardlayout, parent, false);

        return new FriendRequest_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        mref = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mref1 = FirebaseDatabase.getInstance().getReference().child("Friends");

        mref.keepSynced(true);
        mref1.keepSynced(true);



        reference= FirebaseDatabase.getInstance().getReference("users").child(mfriendrequest.get(position).getSender());
        reference.keepSynced(true);
        assert userid != null;

        ref= FirebaseDatabase.getInstance().getReference().child("Notifications");




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id1 = mfriendrequest.get(position).getSender();

        final String user_id = mfriendrequest.get(position).getId();

        final Friends_request friends_request = mfriendrequest.get(position);
        mref2 = FirebaseDatabase.getInstance().getReference("users");//.child(user_id);
        mref2.child(user_id1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String image = dataSnapshot.child("IMAGEURL").getValue(String.class);


                holder.un.setText(name);
                holder.setUserimage(image, mcontext);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        api2= Client.getClient("https://fcm.googleapis.com/").create(APIService2.class);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, Alluserprofile.class);
                intent.putExtra("userid", user_id1);
                mcontext.startActivity(intent);
            }
        });

        String countq = Integer.toString(count);
        DatabaseReference countref = FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
        countref.child("count").setValue(countq).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        String userid=friends_request.getId();

        DatabaseReference mm = FirebaseDatabase.getInstance().getReference("users").child(user_id1);
        mm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("USERNAME").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mref.child(friends_request.getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(friends_request.getReciever())) {
                    String reqtype = dataSnapshot.child(friends_request.getReciever()).child("requested_type").getValue(String.class);

                    if ("Recieved".equals(reqtype))
                    {
                        currentstate = "reqrecieved";
                        current=currentstate;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        currentstate="reqrecieved";

        holder.addrequestedfriend.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {

                                                             notify=true;

                                                             if ("reqrecieved".equals(currentstate)) {
                                                                 holder.bar.setVisibility(View.VISIBLE);

                                                                 Calendar calendar = Calendar.getInstance();
                                                                 final SimpleDateFormat currentdate1 = new SimpleDateFormat("MMM dd, YYYY");
                                                                 final String currentdate = currentdate1.format(calendar.getTime());

                                                                 final String sender = friends_request.getSender();
                                                                 final String reciever = friends_request.getId();


                                                                 mref1.child(sender).child(reciever).child("firstrequested").setValue(sender).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                     @Override
                                                                     public void onSuccess(Void aVoid) {

                                                                         mref1.child(sender).child(reciever).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                             @Override
                                                                             public void onSuccess(Void aVoid) {

                                                                                 mref1.child(sender).child(reciever).child("accepted").setValue(reciever).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                     @Override
                                                                                     public void onSuccess(Void aVoid) {

                                                                                         mref1.child(sender).child(reciever).child("id").setValue(reciever).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                             @Override
                                                                                             public void onSuccess(Void aVoid) {

                                                                                                 mref1.child(sender).child(reciever).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                     @Override
                                                                                                     public void onSuccess(Void aVoid) {

                                                                                                         mref1.child(sender).child(reciever).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                             @Override
                                                                                                             public void onSuccess(Void aVoid) {


                                                                                                                 mref1.child(reciever).child(sender).child("firstrequested").setValue(sender).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                     @Override
                                                                                                                     public void onSuccess(Void aVoid) {


                                                                                                                         mref1.child(reciever).child(sender).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                             @Override
                                                                                                                             public void onSuccess(Void aVoid) {

                                                                                                                                 mref1.child(reciever).child(sender).child("accepted").setValue(reciever).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                     @Override
                                                                                                                                     public void onSuccess(Void aVoid) {

                                                                                                                                         mref1.child(reciever).child(sender).child("id").setValue(sender).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                             @Override
                                                                                                                                             public void onSuccess(Void aVoid) {

                                                                                                                                                 mref1.child(reciever).child(sender).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                     @Override
                                                                                                                                                     public void onSuccess(Void aVoid) {

                                                                                                                                                         mref1.child(reciever).child(sender).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                             @Override
                                                                                                                                                             public void onSuccess(Void aVoid) {


                                                                                                                                                                 mref.child(sender).child(reciever).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                     @Override
                                                                                                                                                                     public void onSuccess(Void aVoid) {

                                                                                                                                                                         mref.child(reciever).child(sender).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                             @Override
                                                                                                                                                                             public void onSuccess(Void aVoid) {

                                                                                                                                                                                 DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(sender).child(reciever);

                                                                                                                                                                                 final HashMap typingstatus = new HashMap();
                                                                                                                                                                                 typingstatus.put("usertyping", "false");
                                                                                                                                                                                 mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                                     @Override
                                                                                                                                                                                     public void onSuccess(Void aVoid) {
                                                                                                                                                                                     }
                                                                                                                                                                                 });

                                                                                                                                                                                 mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(reciever).child(sender);

                                                                                                                                                                                 final HashMap typingstatus1 = new HashMap();
                                                                                                                                                                                 typingstatus.put("usertyping", "false");
                                                                                                                                                                                 mref6.setValue(typingstatus1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                                     @Override
                                                                                                                                                                                     public void onSuccess(Void aVoid) {
                                                                                                                                                                                     }
                                                                                                                                                                                 });


                                                                                                                                                                                 DatabaseReference countref = FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
                                                                                                                                                                                 countref.child("count").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                     @Override
                                                                                                                                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                                                     }
                                                                                                                                                                                 });


                                                                                                                                                                                 Toast.makeText(mcontext, "Accepted", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                 currentstate = "friends";
                                                                                                                                                                                 holder.addrequestedfriend.setText("Added");

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


                                                                 final String msg = "Accepted your Friend Request";
                                                                 DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                                                 reference.addValueEventListener(new ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                         Details details = dataSnapshot.getValue(Details.class);
                                                                         if (notify) {

                                                                             sendNotification1(sender, details.getUSERNAME(), msg, friends_request);

                                                                         }
                                                                         notify = false;
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                     }
                                                                 });

                                                                 holder.bar.setVisibility(View.GONE);

                                                             }
                                                         }
                                                     });

/*
                if(currentstate.equals("reqsend"))
                {

                    mref.child(firebaseUser.getUid()).child(userid).child("requested_type").removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mref.child(userid).child(firebaseUser.getUid()).child("requested_type").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mcontext, "Request canceled...", Toast.LENGTH_SHORT).show();

                                    currentstate ="Notfriends";
                                   // addfriend.setText("Add Friend");

                                }
                            });

                        }


                    });
                }

            }

        });


            }

            });
*/

    }


            private void sendNotification1(final String reciever, final String username, final String message, Friends_request friends_request) {
                final String sender = friends_request.getSender();
                userid = friends_request.getId();

                DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
                Query query = tokens.orderByKey().equalTo(reciever);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Token2 token = snapshot.getValue(Token2.class);
                            Data2 data = new Data2(firebaseUser.getUid(), R.mipmap.ic_launcher, username + " " + message, "Friend Request", reciever);

                            Sender2 sender2 = new Sender2(data, token.getToken());

                            api2.sendNotification(sender2)
                                    .enqueue(new retrofit2.Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().success != 1) {
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

            @Override
            public int getItemCount() {
                return mfriendrequest.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {

                public TextView un;
                public CircleImageView imageenter1;
                Button addrequestedfriend;
                ProgressBar bar;


                public ViewHolder(View itemView) {

                    super(itemView);
                    un = itemView.findViewById(R.id.un);
                    imageenter1 = itemView.findViewById(R.id.imageenter1);
                    addrequestedfriend = itemView.findViewById(R.id.addrequestedfriend);

                    bar=itemView.findViewById(R.id.bar);
                }

                public void setUserimage(String image, Context mcontext) {
                    imageenter1 = itemView.findViewById(R.id.imageenter1);
                    Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
                }
            }

        }
