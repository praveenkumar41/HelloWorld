package com.example.project2;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.Filter;
 import android.widget.Filterable;
 import android.widget.ImageView;
 import android.widget.ProgressBar;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AlertDialog;
 import androidx.cardview.widget.CardView;
 import androidx.recyclerview.widget.RecyclerView;

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
 import com.squareup.picasso.Callback;
 import com.squareup.picasso.NetworkPolicy;
 import com.squareup.picasso.Picasso;

 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.List;

 import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
 import de.hdodenhof.circleimageview.CircleImageView;
 import retrofit2.Call;
 import retrofit2.Response;

 import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    private Context mcontext;
    private List<Details> muser;
    FirebaseUser firebaseUser;
    ArrayList<Details> muser1;
    DatabaseReference mref,mref1,ref,reference5;
    String currentstate;

    String username;
    userfragment Userfragment;

    APIService1 apiService;
    APIService2 api2;
    boolean notify=false;

    public UserAdapter(Context mcontext, List<Details> muser)
   {
       this.mcontext=mcontext;
       this.muser= muser;
       this.muser1=new ArrayList<>(muser);
   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cardlayoutusers, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


       final Details details=muser.get(position);
       holder.un.setText(details.getUSERNAME());

       holder.setuserimage(mcontext,details.getIMAGEURL());

      //  Picasso.with(mcontext).load(details.getIMAGEURL()).into(holder.imageenter1);


        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService1.class);

        api2= Client.getClient("https://fcm.googleapis.com/").create(APIService2.class);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
               Intent intent=new Intent(mcontext,Alluserprofile.class);
               intent.putExtra("userid",details.getID());
               DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(details.getID());

               final HashMap typingstatus = new HashMap();
               typingstatus.put("usertyping", "false");
               mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                   }
               });

               intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
               mcontext.startActivity(intent);
           }
       });

       firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

/*
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("unique_userlist").child(firebaseUser.getUid()).child("users").child(firebaseUser.getUid());

       DatabaseReference mm=FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());
       mm.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChildren())
               {
                   DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("unique_userlist").child(firebaseUser.getUid()).child("users").child(firebaseUser.getUid());
                   mref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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

*/
        reference5= FirebaseDatabase.getInstance().getReference("users").child(details.getID());
        reference5.keepSynced(true);


        ref= FirebaseDatabase.getInstance().getReference().child("Notifications");


        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.child("USERNAME").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mref= FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mref1= FirebaseDatabase.getInstance().getReference().child("Friends");


        mref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(details.getID()))
                {
                    String reqtype=dataSnapshot.child(details.getID()).child("requested_type").getValue(String.class);

                    if("Recieved".equals(reqtype))
                    {
                        currentstate="reqrecieved";
                        holder.addit.setText("Accept");
                    }
                    else if("Send".equals(reqtype))
                    {
                        currentstate ="reqsend";
                        holder.addit.setText("Requested");
                    }
                }
                else
                {
                    mref1.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
                    {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(details.getID()))
                            {
                                currentstate="friends";
                                holder.addit.setText("unfriend");
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


        final String request="Request Send";
        mref= FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mref1= FirebaseDatabase.getInstance().getReference().child("Friends");


        mref.keepSynced(true);
        mref1.keepSynced(true);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        currentstate = "Notfriends";


        final DatabaseReference finalMref = mref;
        holder.addit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (currentstate.equals("Notfriends"))
               {
                   holder.bar.setVisibility(View.VISIBLE);

                   notify=true;
                   mref= FirebaseDatabase.getInstance().getReference().child("Friend_Request");


                   mref.child(firebaseUser.getUid()).child(details.getID()).child("requested_type").setValue("Send").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           mref.child(firebaseUser.getUid()).child(details.getID()).child("sender").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                   mref.child(firebaseUser.getUid()).child(details.getID()).child("reciever").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {


                                           mref.child(firebaseUser.getUid()).child(details.getID()).child("id").setValue(details.getID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {


                                                   if (task.isSuccessful()) {
                                                       mref.child(details.getID()).child(firebaseUser.getUid()).child("requested_type").setValue("Recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void aVoid) {
                                                               mref.child(details.getID()).child(firebaseUser.getUid()).child("sender").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {

                                                                       mref.child(details.getID()).child(firebaseUser.getUid()).child("reciever").setValue(details.getID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                           @Override
                                                                           public void onSuccess(Void aVoid) {


                                                                               mref.child(details.getID()).child(firebaseUser.getUid()).child("id").setValue(details.getID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                   @Override
                                                                                   public void onSuccess(Void aVoid) {




                                                                                       DatabaseReference mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(details.getID());

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

                                                                               /*          countref=FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
                                                                                        countref.child("count").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                            }
                                                                                        });
*/

                                                                                       String senderuserid=firebaseUser.getUid();

                                                                                       HashMap<String, String> notificationmap = new HashMap();
                                                                                       notificationmap.put("from",senderuserid);
                                                                                       notificationmap.put("type", "request");

                                                                                       ref.child(details.getID()).push().setValue(notificationmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                           @Override
                                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                                               if (task.isSuccessful()) {

                                                                                                   Toast.makeText(mcontext, "Request send...", Toast.LENGTH_SHORT).show();
                                                                                                   currentstate = "reqsend";
                                                                                                   holder.addit.setText("Requested");

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
                                                       Toast.makeText(mcontext, "Failed Sending Request", Toast.LENGTH_SHORT).show();
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
                  DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                   reference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Details details=dataSnapshot.getValue(Details.class);
                           if(notify)
                           {
                               sendNotification1(details.getID(),details.getUSERNAME(),msg);
                           }
                           notify=false;
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

                   holder.bar.setVisibility(View.GONE);
               }

               //cancel friend request

               if(currentstate.equals("reqsend"))
               {
                   holder.bar.setVisibility(View.VISIBLE);

                   mref= FirebaseDatabase.getInstance().getReference().child("Friend_Request");

                   mref.child(firebaseUser.getUid()).child(details.getID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
                   {
                       @Override
                       public void onSuccess(Void aVoid) {

                           mref.child(details.getID()).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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



                                   Toast.makeText(mcontext, "Request canceled...", Toast.LENGTH_SHORT).show();
                                   currentstate ="Notfriends";
                                   holder.addit.setText("ADD");

                               }
                           });

                       }


                   });
                   holder.bar.setVisibility(View.GONE);
               }

               if(currentstate.equals("reqrecieved"))
               {
                   notify=true;
                   holder.bar.setVisibility(View.VISIBLE);
                   // final String currentdate = DateFormat.getDateTimeInstance().format(new Date());


                   Calendar calendar=Calendar.getInstance();
                   final SimpleDateFormat currentdate1=new SimpleDateFormat("MMM dd, YYYY");
                   final String currentdate=currentdate1.format(calendar.getTime());

                   final String userid=details.getID();


                   mref1.child(firebaseUser.getUid()).child(details.getID()).child("firstrequested").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                           mref1.child(firebaseUser.getUid()).child(details.getID()).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   mref1.child(firebaseUser.getUid()).child(details.getID()).child("accepted").setValue(details.getID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {

                                           mref1.child(firebaseUser.getUid()).child(details.getID()).child("id").setValue(details.getID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {

                                                   mref1.child(firebaseUser.getUid()).child(userid).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {

                                                           mref1.child(firebaseUser.getUid()).child(userid).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {


                                                                   mref1.child(userid).child(firebaseUser.getUid()).child("firstrequested").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                       @Override
                                                                       public void onSuccess(Void aVoid) {


                                                                           mref1.child(userid).child(firebaseUser.getUid()).child("friendssince").setValue(currentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void aVoid) {

                                                                                   mref1.child(userid).child(firebaseUser.getUid()).child("accepted").setValue(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                       @Override
                                                                                       public void onSuccess(Void aVoid) {

                                                                                           mref1.child(userid).child(firebaseUser.getUid()).child("id").setValue(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                               @Override
                                                                                               public void onSuccess(Void aVoid) {

                                                                                                   mref1.child(userid).child(firebaseUser.getUid()).child("acceptedname").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                       @Override
                                                                                                       public void onSuccess(Void aVoid) {

                                                                                                           mref1.child(userid).child(firebaseUser.getUid()).child("toggle").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                               @Override
                                                                                                               public void onSuccess(Void aVoid) {


                                                                                                                   finalMref.child(firebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                       @Override
                                                                                                                       public void onSuccess(Void aVoid) {

                                                                                                                           finalMref.child(userid).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                                                                                                                   Toast.makeText(mcontext, "Accepted", Toast.LENGTH_SHORT).show();
                                                                                                                                   currentstate = "friends";
                                                                                                                                   holder.addit.setText("unfriend");

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


                   holder.bar.setVisibility(View.GONE);
               }


               if(currentstate.equals("friends"))
               {
                   holder.bar.setVisibility(View.VISIBLE);
                   mref1.child(firebaseUser.getUid()).child(details.getID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           mref1.child(details.getID()).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(mcontext, "unfriended", Toast.LENGTH_SHORT).show();
                                   currentstate="Notfriends";
                                   holder.addit.setText("ADD");
                               }
                           });
                       }
                   });
                   holder.bar.setVisibility(View.GONE);
               }
           }
       });
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


    public class ViewHolder extends RecyclerView.ViewHolder{

       public TextView un;
       public CircleImageView imageenter1;
       public CardView cdremove;
       Button addit;

       ProgressBar bar;

       public ViewHolder(View itemView)
       {

           super(itemView);
           un=itemView.findViewById(R.id.un);
           imageenter1=itemView.findViewById(R.id.imageenter1);

           bar=itemView.findViewById(R.id.bar);
           addit=itemView.findViewById(R.id.addit);
           cdremove=itemView.findViewById(R.id.cdremove);
       }


        public void setuserimage(Context mcontext, String imageurl) {

            Picasso.with(mcontext).load(imageurl).placeholder(R.drawable.pro).into(imageenter1);
        }

    }


    private void sendNotification2(final String userid, final String username, final String msg) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Details details=snapshot.getValue(Details.class);

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
                    Data1 data=new Data1(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"Friend Request",reciever);

                    Sender1 sender1=new Sender1(data,token.getToken());

                    apiService.sendNotification(sender1)
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
