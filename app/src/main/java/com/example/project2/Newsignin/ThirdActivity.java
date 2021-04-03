package com.example.project2.Newsignin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.CountryData;
import com.example.project2.Country_names_popup;
import com.example.project2.R;
import com.example.project2.Startapp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThirdActivity extends AppCompatActivity implements Country_names_popup.CountrynamesListener {

    TextInputEditText email,code,phone;
    Button b;
    TextView select;
    TextView heading;
    TextInputLayout edit, phonebox, mailbox, codebox;
    private ArrayAdapter<String> listAdapter, codeAdapter;

    Button button;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    int apos=0;
    String names="";
    DatabaseReference dataDetails,dataDetails1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final String name=getIntent().getStringExtra("username");
        final String dateofbirth=getIntent().getStringExtra("dob");
        final String password=getIntent().getStringExtra("password");

        mAuth = FirebaseAuth.getInstance();

        heading = findViewById(R.id.heading);

        button=findViewById(R.id.button);

        select = findViewById(R.id.select);
        mailbox = findViewById(R.id.mailbox);
        phonebox = findViewById(R.id.phonebox);
        codebox = findViewById(R.id.codebox);


        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (select.getText().toString().equals("Instead use phone")) {
                    select.setText("Instead use email");
                    phonebox.setVisibility(View.VISIBLE);
                    codebox.setVisibility(View.VISIBLE);
                    mailbox.setVisibility(View.GONE);
                } else {
                    select.setText("Instead use phone");
                    mailbox.setVisibility(View.VISIBLE);
                    phonebox.setVisibility(View.GONE);
                    codebox.setVisibility(View.GONE);
                }
            }
        });


        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Country_names_popup cn = new Country_names_popup();
                    cn.show(getSupportFragmentManager(), "country names");
                    code.clearFocus();
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!TextUtils.isEmpty(mailbox.getEditText().getText().toString()) && !TextUtils.isEmpty(password))
                {
                    final String emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

                    Pattern pattern = Pattern.compile(emailRegEx);
                    Matcher matcher = pattern.matcher(mailbox.getEditText().getText().toString());

                    if(matcher.find())
                    {
                        email_aunthentication(mailbox.getEditText().getText().toString(),password,name,dateofbirth);
                    }
                    else
                    {
                        email.setError("Invalid MailID");
                    }
                }

                else if(!TextUtils.isEmpty(codebox.getEditText().getText().toString()) && !TextUtils.isEmpty(phonebox.getEditText().getText().toString()))
                {
                    Intent intent=new Intent(getApplicationContext(),PhoneAunthentication.class);
                    intent.putExtra("type","phone");
                    intent.putExtra("phonenumber",phonebox.getEditText().getText().toString());
                    intent.putExtra("code",codebox.getEditText().getText().toString());
                    intent.putExtra("username",name);
                    intent.putExtra("dob",dateofbirth);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }

                else if(TextUtils.isEmpty(mailbox.getEditText().getText().toString()))
                {
                    mailbox.setError("MailID Required");
                }
                else if(!TextUtils.isEmpty(codebox.getEditText().getText().toString()) && TextUtils.isEmpty(phonebox.getEditText().getText().toString()))
                {
                    phonebox.setError("Phone number Required");
                }
                else if(TextUtils.isEmpty(codebox.getEditText().getText().toString()) && !TextUtils.isEmpty(phonebox.getEditText().getText().toString()))
                {
                    codebox.setError("country code Requried");
                }

            }
        });
    }


    private void email_aunthentication(String mail, final String password, final String name, final String dateofbirth) {

        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    final String currentid=mAuth.getCurrentUser().getUid();

                    final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                    final String savecurrentdate = currentdate.format(calendar.getTime());


                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                    final String savecurrenttime = currenttime.format(calendar.getTime());


                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            final String devicetoken = instanceIdResult.getToken();


                            DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("user_activity").child(firebaseUser.getUid()).push();

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("activity","signup");
                            hashMap.put("userid",firebaseUser.getUid());
                            hashMap.put("pushid",mref.getKey());
                            hashMap.put("date",savecurrentdate);
                            hashMap.put("time",savecurrenttime);

                            mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    DatabaseReference mref11= FirebaseDatabase.getInstance().getReference().child("All_UID").child(firebaseUser.getUid());

                                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
                                    mref.child("TOGGLE").setValue("OFF");

                                    mref11.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            String mailid=firebaseUser.getEmail();

                                            String mail=mailid.replace("@gmail.com","");

                                            DatabaseReference mref12= FirebaseDatabase.getInstance().getReference().child("All_Emailid").child(mail);

                                            mref12.setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    String locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();

                                                    for(int i=0;i<CountryData.countryNames.length;i++)
                                                    {
                                                        if(CountryData.countryNames[i].contains(locale))
                                                        {
                                                            apos=i;
                                                        }
                                                    }

                                                    String cc=CountryData.countryAreaCodes[apos];

                                                    dataDetails= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                                                    dataDetails1= FirebaseDatabase.getInstance().getReference("usersdisplay").child(firebaseUser.getUid());

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
                                                    hashMap.put("USERNAME_ID",names+id);
                                                    hashMap.put("DOB", dateofbirth);
                                                    hashMap.put("TIME", savecurrenttime);
                                                    hashMap.put("DATE", savecurrentdate);
                                                    hashMap.put("DEVICETOKEN", devicetoken);
                                                    hashMap.put("IMAGEURL", "https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_Images%2FALLFxjjJmTMZt0n1rtp7EvdkF5n1.jpg?alt=media&token=eceb136c-6cac-4fe2-82f0-bb41c476cd71");
                                                    hashMap.put("SONGURL", "https://firebasestorage.googleapis.com/v0/b/project2-a6aa0.appspot.com/o/Profile_songs%2Falarm.mp3?alt=media&token=68d3406a-2179-4708-92f8-39ac3a383597");
                                                    hashMap.put("songname", "default");
                                                    hashMap.put("online", "false");
                                                    hashMap.put("cc", "+"+cc);
                                                    hashMap.put("cn", locale);


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
                                    });

                                }
                            });

                        }
                    });
                }

                else{

                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(), "You Are Already Register", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void applycodes(String codes) {
        code.setText("+" + codes);
    }

}
