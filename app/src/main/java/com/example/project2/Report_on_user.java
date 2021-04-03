package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Report_on_user extends AppCompatActivity {

    Button reportsubmitbutton,reportsubmitbutton1;
    RadioGroup rg;
    RadioButton rb;
    FirebaseUser firebaseUser;
    DatabaseReference mref,mref1,mref11,mref12,mref222;
    AlertDialog.Builder alertdialog;
    String report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_on_user);

        final String userid=getIntent().getStringExtra("userid");


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        mref= FirebaseDatabase.getInstance().getReference().child("Report").child(firebaseUser.getUid()).child(userid);
        mref1= FirebaseDatabase.getInstance().getReference().child("Report").child(userid).child(firebaseUser.getUid());
        mref.keepSynced(true);
        mref1.keepSynced(true);

        mref12= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref12.keepSynced(true);

        mref11= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref11.keepSynced(true);

         alertdialog =new AlertDialog.Builder(this);


        reportsubmitbutton=findViewById(R.id.reportsubmitbutton);

        rg=findViewById(R.id.rg);
        rb=findViewById(R.id.rm);


        reportsubmitbutton.setVisibility(View.VISIBLE);
        reportsubmitbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                int selectedid = rg.getCheckedRadioButtonId();
                rb = findViewById(selectedid);
                report=rb.getText().toString();

                if(report!=null)
                {

                alertdialog.setTitle("Report User");
                alertdialog.setIcon(R.drawable.report);


                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                    final String savecurrentdate = currentdate.format(calendar.getTime());


                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                    final String savecurrenttime = currenttime.format(calendar.getTime());



                    alertdialog.setMessage("sure,Do you want to report this user");

                alertdialog.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        mref.child("reportedby").setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mref.child("reportedAccount").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mref.child("reason").setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
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


                                                                                                mref12.child(firebaseUser.getUid()).child(userid).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        mref12.child(userid).child(firebaseUser.getUid()).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    Toast.makeText(getApplicationContext(), "User Reported", Toast.LENGTH_SHORT).show();
                                                                                                                    finish();
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
                        finish();
                    }
                });

                alertdialog.create();
                alertdialog.show();

            }
                else {
                    Toast.makeText(getApplicationContext(), "Select a reason to Report User", Toast.LENGTH_SHORT).show();
                }
        }
    });
    }
}
