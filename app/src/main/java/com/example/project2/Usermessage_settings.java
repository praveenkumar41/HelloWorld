package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Usermessage_settings extends AppCompatActivity {

    CircleImageView myid;
    TextView name;
    FirebaseUser firebaseUser;
    LinearLayout l1,l2,l3,block,report,delete;
    TextView totalmediacount,messagecount,photocount,videocount,linkcount;
    int pc=0,vc=0,lc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermessage_settings);

        final String user_id = getIntent().getStringExtra("userid");

        myid=findViewById(R.id.myid);
        name=findViewById(R.id.name);

        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        block=findViewById(R.id.block);
        report=findViewById(R.id.report);
        delete=findViewById(R.id.delete);
        messagecount=findViewById(R.id.messagecount);


        photocount=findViewById(R.id.photocount);
        videocount=findViewById(R.id.videocount);
        linkcount=findViewById(R.id.linkcount);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users").child(user_id);
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image1=dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name1=dataSnapshot.child("USERNAME").getValue(String.class);

                name.setText(name1);
                Picasso.with(getApplicationContext()).load(image1).into(myid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Report_on_user.class);
                intent.putExtra("userid",user_id);
                startActivity(intent);
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               AlertDialog.Builder alertdialog =new AlertDialog.Builder(view.getRootView().getContext());
                final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Blocked_Acc").child(firebaseUser.getUid()).child(user_id);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                final String savecurrentdate = currentdate.format(calendar.getTime());


                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                final String savecurrenttime = currenttime.format(calendar.getTime());


                alertdialog.setMessage("Do you want to block this user");

                alertdialog.setPositiveButton("Block", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        mref.child("blockedby").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mref.child("blockedid").setValue(user_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                                mref.child("date").setValue(savecurrentdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                        mref.child("time").setValue(savecurrenttime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                mref.child("type").setValue("chats").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                       if (task.isSuccessful()) {
                                                                                            Toast.makeText(getApplicationContext(), "User blocked", Toast.LENGTH_SHORT).show();
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
                    }

                });

                alertdialog.setNegativeButton("cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                alertdialog.create();
                alertdialog.show();

            }

        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertdialog1 =new AlertDialog.Builder(view.getRootView().getContext());

                alertdialog1.setMessage("only messages will be deleted in your inbox other user will be still able to see it");

                alertdialog1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference11=FirebaseDatabase.getInstance().getReference().child("Chats").child(firebaseUser.getUid()).child(user_id);
                        reference11.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Usermessage_settings.this, "deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


                alertdialog1.setNegativeButton("cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                alertdialog1.create();
                alertdialog1.show();
            }
        });




        messagescount(user_id);
        messagescount1(user_id);
        
    }


    private void messagescount(String user_id)
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chats").child(firebaseUser.getUid()).child(user_id);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount()==0)
                {
                    messagecount.setText("("+dataSnapshot.getChildrenCount()+")");
                }
                else{
                    int count11= (int) (dataSnapshot.getChildrenCount());
                    if(count11>0 && count11<=9)
                    {
                        messagecount.setText("("+"0"+count11+")");
                    }
                    else
                    {
                        messagecount.setText("("+count11+")");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void messagescount1(String user_id) {

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chats").child(firebaseUser.getUid()).child(user_id);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    if(snapshot.child("type").getValue(String.class).equals("link") && snapshot.child("type").getValue(String.class).equals("url"))
                    {
                        lc++;
                    }
                }

                String counter=Integer.toString(lc);
                linkcount.setText(counter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
