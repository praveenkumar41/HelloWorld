package com.example.project2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    TextView date12;
    EditText un;
    Button b1;

    RadioButton rb;
    RadioGroup rg;
    DatePickerDialog.OnDateSetListener mDatesetlistener;
    FirebaseAuth mAuth;
    //String Gender;
    FirebaseUser firebaseUser;
    String devicetoken;

    DatabaseReference mref6,mref,dataDetails,dataDetails1,ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);


        b1 = findViewById(R.id.b1);
        date12 = findViewById(R.id.date12);
        un = findViewById(R.id.un);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        ref=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        ref.keepSynced(true);

        rb= findViewById(R.id.rm);

        rg= findViewById(R.id.rg);


        date12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(StartActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, mDatesetlistener, year, Month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }

        });

        DatabaseReference countref=FirebaseDatabase.getInstance().getReference().child("count_of_request").child(firebaseUser.getUid());
        countref.child("count").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        mDatesetlistener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int Month, int day)
            {
                Month = Month + 1;
                String s1 = "OnDateSet: mm/dd/yyy: " + Month + "/" + day + "/" + year;

                String DOF = Month + "/" + day + "/" + year;

                date12.setText(DOF);
            }

        };

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Addit();

            }
        });

        mref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
        mref.child("TOGGLE").setValue("OFF").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Done..", Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void Addit()
    {

        final String id=mAuth.getCurrentUser().getUid();
        final String Username=un.getText().toString();
        final String DateofBirth=date12.getText().toString();

        String Gender = null;

        int selectedid = rg.getCheckedRadioButtonId();
        rb = findViewById(selectedid);
        Gender= rb.getText().toString();


        dataDetails1 = FirebaseDatabase.getInstance().getReference().child("usersdisplay");

        dataDetails = FirebaseDatabase.getInstance().getReference("users").child(id);
        dataDetails.keepSynced(true);

        final String currentid=mAuth.getCurrentUser().getUid();




        final String finalGender = Gender;

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                devicetoken = instanceIdResult.getToken();


                if ((!TextUtils.isEmpty(Username)) && (!TextUtils.isEmpty(DateofBirth)) && (!TextUtils.isEmpty(finalGender)))
                {

                    final HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ID", id);
                    hashMap.put("USERNAME", Username);
                    hashMap.put("DOB", DateofBirth);
                    hashMap.put("GENDER", finalGender);

                    if(finalGender.equals("Male"))
                    {
                        hashMap.put("IMAGEURL","https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_Images%2FALLFxjjJmTMZt0n1rtp7EvdkF5n1.jpg?alt=media&token=eceb136c-6cac-4fe2-82f0-bb41c476cd71");
                    }
                    else
                    {
                        hashMap.put("IMAGEURL","https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_Images%2FALLFxjjJmTMZt0n1rtp7EvdkF5n1.jpg?alt=media&token=f7b5b325-061c-45f6-b351-908167beacc4");
                    }

                    hashMap.put("SONGURL", "https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_songs%2Falarm.mp3?alt=media&token=68d3406a-2179-4708-92f8-39ac3a383597");
                    hashMap.put("songname","default");
                    hashMap.put("DEVICETOKEN", devicetoken);
                    hashMap.put("online","false");


                    dataDetails.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                    dataDetails.keepSynced(true);
                            if (task.isSuccessful()) {

                                dataDetails1.child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), Startapp.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }


                                            }
                                        });

                                            }

                        }
                    });


                }
            }
        });

    }
}