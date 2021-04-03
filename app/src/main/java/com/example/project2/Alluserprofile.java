package com.example.project2;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Notification.Client;
import com.example.project2.Notification.Data;
import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification.Sender;
import com.example.project2.Notification.Token;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class Alluserprofile extends AppCompatActivity
{
    CircleImageView otheruserimage;
    TextView otherusername;
    Button addfriend;
    DatabaseReference reference,reference5;
    FirebaseUser firebaseUser;
    DatabaseReference mref,mref1,mref2,ref,ref2;
    private String currentstate="Notfriends";
    String username;
    FirebaseAuth mAuth;

    TextView suggested;
    RecyclerView srecyclerview;

    FriendsAdapter friendsAdapter;
    suggestedAdapter suggestedadapter;

    ProgressBar bar;
    List<Friends> mfriends;


    APIService1 apiService;
    APIService2 api2;
    boolean notify=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alluserprofile);

        otheruserimage = findViewById(R.id.otheruserimage);
        otherusername = findViewById(R.id.otherusername);
        addfriend = findViewById(R.id.addfriend);

        suggested = findViewById(R.id.suggested);
        srecyclerview = findViewById(R.id.srecyclerview);

        bar = findViewById(R.id.bar);
        //  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        final String userid = getIntent().getStringExtra("userid");

        reference = FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference.keepSynced(true);
        assert userid != null;
        reference5 = FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference5.keepSynced(true);

        ref = FirebaseDatabase.getInstance().getReference().child("Notifications");
        ref.keepSynced(true);

        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("USERNAME").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        srecyclerview.setHasFixedSize(true);
        srecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService1.class);
        api2 = Client.getClient("https://fcm.googleapis.com/").create(APIService2.class);


        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String othername = dataSnapshot.child("USERNAME").getValue(String.class);
                                                final String otherimage = dataSnapshot.child("IMAGEURL").getValue(String.class);

                                                otherusername.setText(othername);
                                                // Picasso.with(getApplicationContext()).load(otherimage).into(otheruserimage);


                                                Picasso.with(getApplicationContext()).load(otherimage).networkPolicy(NetworkPolicy.OFFLINE).into(otheruserimage, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Picasso.with(getApplicationContext()).load(otherimage).placeholder(R.drawable.pro).into(otheruserimage);

                                                    }
                                                });
                                            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*

                mref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(userid))
                        {
                            String reqtype=dataSnapshot.child(userid).child("requested_type").getValue(String.class);

                            if("Recieved".equals(reqtype))
                            {
                                currentstate="reqrecieved";
                                addfriend.setText("Accept");
                            }
                            else if("Send".equals(reqtype))
                            {
                                currentstate ="reqsend";
                                addfriend.setText("Requested");
                            }
                        }
                        else
                        {
                            mref1.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
                            {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(userid))
                                    {
                                        currentstate="friends";
                                        addfriend.setText("unfriend");
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String request="Request Send";
        mref= FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mref1= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref2= FirebaseDatabase.getInstance().getReference().child("Friendsreference");


        mref.keepSynced(true);
        mref1.keepSynced(true);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        currentstate = "Notfriends";

        addfriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                            if (currentstate.equals("Notfriends"))
                            {
                                bar.setVisibility(View.VISIBLE);

                                notify=true;

                                mref.child(firebaseUser.getUid()).child(userid).child("requested_type").setValue("Send").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mref.child(firebaseUser.getUid()).child(userid).child("sender").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                mref.child(firebaseUser.getUid()).child(userid).child("reciever").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                        mref.child(firebaseUser.getUid()).child(userid).child("id").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                if (task.isSuccessful()) {
                                                                    mref.child(userid).child(firebaseUser.getUid()).child("requested_type").setValue("Recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            mref.child(userid).child(firebaseUser.getUid()).child("sender").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    mref.child(userid).child(firebaseUser.getUid()).child("reciever").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {


                                                                                            mref.child(userid).child(firebaseUser.getUid()).child("id").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {




                                                                                                    DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(userid);

                                                                                                    final HashMap typingstatus = new HashMap();
                                                                                                    typingstatus.put("usertyping", "false");
                                                                                                    mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                        }
                                                                                                    });


                                                                                                    mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(userid).child(firebaseUser.getUid());

                                                                                                    final HashMap typingstatus1 = new HashMap();
                                                                                                    typingstatus.put("usertyping", "false");
                                                                                                    mref6.setValue(typingstatus1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                        }
                                                                                                    });

                                                                               /*          countref=FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
                                                                                        countref.child("count").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                            }
                                                                                        });
*/
/*
                                                                                                    String senderuserid=firebaseUser.getUid();

                                                                                                    HashMap<String, String> notificationmap = new HashMap();
                                                                                                    notificationmap.put("from",senderuserid);
                                                                                                    notificationmap.put("type", "request");


                                                                                                    ref.child(userid).push().setValue(notificationmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {

                                                                                                                Toast.makeText(getApplicationContext(), "Request send...", Toast.LENGTH_SHORT).show();
                                                                                                                currentstate = "reqsend";
                                                                                                                addfriend.setText("Requested");

                                                                                                            }

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


                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Failed Sending Request", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                                final String msg="send a Friend Request";
                                reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Details details=dataSnapshot.getValue(Details.class);
                                        if(notify)
                                        {
                                            sendNotification1(userid,details.getUSERNAME(),msg);
                                        }
                                        notify=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                bar.setVisibility(View.GONE);
                            }

                            //cancel friend request

                            if(currentstate.equals("reqsend"))
                            {
                                bar.setVisibility(View.VISIBLE);

                                mref.child(firebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mref.child(userid).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                final DatabaseReference countref=FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
                                                countref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String count=dataSnapshot.child("count").getValue(String.class);

                                                        int c=Integer.parseInt(count);
                                                        if(c==0)
                                                        {

                                                        }
                                                        else
                                                        {
                                                            c--;
                                                            String countq=Integer.toString(c);

                                                            countref.child("count").setValue(countq).addOnCompleteListener(new OnCompleteListener<Void>() {
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



                                                Toast.makeText(getApplicationContext(), "Request canceled...", Toast.LENGTH_SHORT).show();
                                                currentstate ="Notfriends";
                                                addfriend.setText("ADD");

                                            }
                                        });

                                    }


                                });
                                bar.setVisibility(View.GONE);
                            }

                            if(currentstate.equals("reqrecieved"))
                            {
                                notify=true;
                                // final String currentdate = DateFormat.getDateTimeInstance().format(new Date());
                                bar.setVisibility(View.VISIBLE);

                                Calendar calendar=Calendar.getInstance();
                                final SimpleDateFormat currentdate1=new SimpleDateFormat("MMM dd, YYYY");
                                final String currentdate=currentdate1.format(calendar.getTime());



                                mref1.child(firebaseUser.getUid()).child(userid).child("firstrequested").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mref1.child(firebaseUser.getUid()).child(userid).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                mref1.child(firebaseUser.getUid()).child(userid).child("accepted").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        mref1.child(firebaseUser.getUid()).child(userid).child("id").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                mref1.child(firebaseUser.getUid()).child(userid).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        mref1.child(firebaseUser.getUid()).child(userid).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                                                                mref1.child(userid).child(firebaseUser.getUid()).child("firstrequested").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {


                                                                                        mref1.child(userid).child(firebaseUser.getUid()).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                mref1.child(userid).child(firebaseUser.getUid()).child("accepted").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                        mref1.child(userid).child(firebaseUser.getUid()).child("id").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                mref1.child(userid).child(firebaseUser.getUid()).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                                        mref1.child(userid).child(firebaseUser.getUid()).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void aVoid) {


                                                                                                                                mref.child(firebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                                                        mref.child(userid).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                                                DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(userid);

                                                                                                                                                final HashMap typingstatus = new HashMap();
                                                                                                                                                typingstatus.put("usertyping", "false");
                                                                                                                                                mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                                                    }
                                                                                                                                                });

                                                                                                                                                mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(userid).child(firebaseUser.getUid());

                                                                                                                                                final HashMap typingstatus1 = new HashMap();
                                                                                                                                                typingstatus.put("usertyping", "false");
                                                                                                                                                mref6.setValue(typingstatus1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                                                    }
                                                                                                                                                });

                                                                                                                                                Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
                                                                                                                                                currentstate = "friends";
                                                                                                                                                addfriend.setText("unfriend");

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



                                final String msg="Accepted your Friend Request";
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Details details=dataSnapshot.getValue(Details.class);
                                        if(notify)
                                        {
                                            sendNotification2(userid,details.getUSERNAME(),msg);
                                        }
                                        notify=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                bar.setVisibility(View.GONE);
                            }


                            if(currentstate.equals("friends"))
                            {
                                bar.setVisibility(View.VISIBLE);

                                mref1.child(firebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mref1.child(userid).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Unfriended", Toast.LENGTH_SHORT).show();
                                                currentstate="Notfriends";
                                                addfriend.setText("ADD");
                                            }
                                        });
                                    }
                                });
                                bar.setVisibility(View.GONE);
                            }


});
}
*/

        addfriend.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             if (addfriend.getText().toString().equals("FOLLOW"))
                                             {
                                                 notify = true;
                                                 final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                                 final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child(userid);

                                                 mref.child("following").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                         mref.child("follower").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                 mref.child("id").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                         mref.child("toggle").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                             @Override
                                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                                         Toast.makeText(Alluserprofile.this, "following", Toast.LENGTH_SHORT).show();

                                                                             }
                                                                         });

                                                                     }
                                                                 });
                                                             }
                                                         });
                                                     }
                                                 });


                                                 DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(userid);

                                                 final HashMap typingstatus = new HashMap();
                                                 typingstatus.put("usertyping", "false");
                                                 mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {
                                                     }
                                                 });

                                                 mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(userid).child(firebaseUser.getUid());

                                                 final HashMap typingstatus1 = new HashMap();
                                                 typingstatus.put("usertyping", "false");
                                                 mref6.setValue(typingstatus1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {
                                                     }
                                                 });

                                                 DatabaseReference mref11=FirebaseDatabase.getInstance().getReference();


                                                 mref11.child("follow_count").child(firebaseUser.getUid()).child("following").child(userid).setValue("true");
                                                 mref11.child("follow_count").child(userid).child("followers").child(firebaseUser.getUid()).setValue("true");


                                                 final String msg = "Started following you";
                                                 DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                                 reference.addValueEventListener(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         Details details = dataSnapshot.getValue(Details.class);
                                                         if (notify) {
                                                             sendNotification2(userid, details.getUSERNAME(), msg);
                                                         }
                                                         notify = false;
                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });

                                                 addfriend.setText("unfollow");


                                                 bar.setVisibility(View.GONE);

                                             } else {

                                                 DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                                                 mref.child("follow").child(firebaseUser.getUid()).child(userid).removeValue();

                                                 mref.child("follow_count").child(firebaseUser.getUid()).child("following").child(userid).removeValue();
                                                 mref.child("follow_count").child(userid).child("followers").child(firebaseUser.getUid()).removeValue();

                                             }

                                         }
                                     });


        mfriends = new ArrayList<>();
        usersdetailsdisplay(userid);
        suggestedadapter = new suggestedAdapter(getApplicationContext(), mfriends,userid);
        if(suggestedadapter.getItemCount()!=0)
        {
             suggested.setVisibility(View.VISIBLE);
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
                                            Toast.makeText(Alluserprofile.this, "Failed", Toast.LENGTH_SHORT).show();
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


    private void sendNotification1(final String reciever, final String username, final String message)
    {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(reciever);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Token1 token=snapshot.getValue(Token1.class);
                    Data1 data=new Data1(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New Friend Request",reciever);

                    Sender1 sender1=new Sender1(data,token.getToken());

                    apiService.sendNotification(sender1)
                            .enqueue(new retrofit2.Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(Alluserprofile.this, "Failed", Toast.LENGTH_SHORT).show();
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


    private void usersdetailsdisplay(final String userid) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(userid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfriends.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Friends friends = Snapshot.getValue(Friends.class);

                    if (Snapshot.exists() && Snapshot.hasChild("accepted") && Snapshot.hasChild("firstrequested") && Snapshot.hasChild("friendssince")) {
                        if (!Snapshot.hasChild("blockeduser"))
                        {
                            if(!firebaseUser.getUid().equals(friends.getId()))
                            {
                                mfriends.add(friends);
                            }

                        }

                    }
                }
                suggestedadapter = new suggestedAdapter(getApplicationContext(), mfriends,userid);
                srecyclerview.setAdapter(suggestedadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
