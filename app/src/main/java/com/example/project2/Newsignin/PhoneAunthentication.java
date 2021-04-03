package com.example.project2.Newsignin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.CountryData;
import com.example.project2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PhoneAunthentication extends AppCompatActivity {

    EditText code;
    Button button;

    private String mVerificationId;
    private FirebaseAuth mAuth;
    String name, dob, codes, password,cn,names;
    int ipos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_auth);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        codes = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("username");
        dob = getIntent().getStringExtra("dob");
        password = getIntent().getStringExtra("password");

        String cc=codes.replace("+","");

        for (int i = 0; i < CountryData.countryAreaCodes.length; i++)
            if (CountryData.countryAreaCodes[i].contains(cc)) {
                ipos=i;
            }

        cn=CountryData.countryNames[ipos];



        mAuth = FirebaseAuth.getInstance();

        code=findViewById(R.id.code);
        button=findViewById(R.id.button);

        sendVerificationCode(phonenumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codes.isEmpty() || code.length() < 6) {
                    code.setError("Enter valid code");
                    code.requestFocus();
                    return;
                }

                verifyVerificationCode(codes);
            }
        });

    }

    private void sendVerificationCode(String phonenumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                codes+ phonenumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);


    }

    private void verifyVerificationCode(String code12) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code12);

        //signing the user
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneAunthentication.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                            final String savecurrentdate = currentdate.format(calendar.getTime());

                            SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                            final String savecurrenttime = currenttime.format(calendar.getTime());



                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                             @Override
                             public void onSuccess(InstanceIdResult instanceIdResult) {
                                       final String devicetoken = instanceIdResult.getToken();

                                 final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                 DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("All_Contacts").child(firebaseUser.getPhoneNumber());


                                 DatabaseReference mref11 = FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
                                 mref11.child("TOGGLE").setValue("OFF");


                                 mref.setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful())
                                         {
                                             DatabaseReference mref99= FirebaseDatabase.getInstance().getReference().child("All_UID").child(firebaseUser.getUid());

                                             mref99.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {

                                                    final DatabaseReference dataDetails= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                                    final DatabaseReference dataDetails1= FirebaseDatabase.getInstance().getReference("usersdisplay").child(firebaseUser.getUid());


                                                     if(name.contains(" "))
                                                     {
                                                         names=name.replace(" ","");
                                                     }
                                                     else {
                                                         names=name;
                                                     }

                                                     Random random=new Random();
                                                     String id=String.format("%04d",random.nextInt(10000));

                                                     final HashMap<String, Object> hashMap = new HashMap<>();

                                                     hashMap.put("password", password);
                                                     hashMap.put("ID", firebaseUser.getUid());
                                                     hashMap.put("USERNAME", name);
                                                     hashMap.put("USERNAME_ID", names+id);
                                                     hashMap.put("DOB", dob);
                                                     hashMap.put("TIME", savecurrenttime);
                                                     hashMap.put("DATE", savecurrentdate);
                                                     hashMap.put("DEVICETOKEN", devicetoken);
                                                     hashMap.put("IMAGEURL", "https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_Images%2FALLFxjjJmTMZt0n1rtp7EvdkF5n1.jpg?alt=media&token=eceb136c-6cac-4fe2-82f0-bb41c476cd71");
                                                     hashMap.put("SONGURL", "https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_songs%2Falarm.mp3?alt=media&token=68d3406a-2179-4708-92f8-39ac3a383597");
                                                     hashMap.put("songname", "default");
                                                     hashMap.put("online", "false");
                                                     hashMap.put("cc", codes);
                                                     hashMap.put("cn",cn);


                                                     dataDetails.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             dataDetails.keepSynced(true);
                                                             if (task.isSuccessful()) {

                                                                 dataDetails1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {
                                                                         if (task.isSuccessful()) {

                                                                             Intent intent = new Intent(getApplicationContext(), Getting_Username.class);
                                                                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                             startActivity(intent);
                                                                             finish();

                                                                         }

                                                                     }
                                                                 });

                                                             }
                                                         }
                                                     });

                                                 }
                                             });

                                         }
                                     }
                                 });

                             }

                          });

                        } else {

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }

     private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
     @Override
     public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                            //Getting the code sent by SMS
            String code11 = phoneAuthCredential.getSmsCode();

            if (code11 != null) {
               code.setText(code11);

               verifyVerificationCode(code11);

            }
     }

     @Override
     public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
     }

     @Override
     public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

         super.onCodeSent(s, forceResendingToken);

             mVerificationId = s;

          }
       };
    }