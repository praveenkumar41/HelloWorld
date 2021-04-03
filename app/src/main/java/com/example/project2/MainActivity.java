package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.videostream.PostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    TextView fp;
    TextInputLayout email, password;
    Button button;
    FirebaseAuth mAuth;
    ProgressBar bar;
    DatabaseReference ref;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fp = findViewById(R.id.fp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button);
        bar = findViewById(R.id.bar);
        mAuth = FirebaseAuth.getInstance();

        // firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("users");
        ref.keepSynced(true);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(password.getEditText().getText().toString()) && !TextUtils.isEmpty(email.getEditText().getText().toString()))
                {
                    final String emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

                    Pattern pattern = Pattern.compile(emailRegEx);
                    Matcher matcher = pattern.matcher(email.getEditText().getText().toString());

                    if(matcher.find())
                    {
                        loginmethod();
                    }
                    else
                    {
                        email.setError("Invalid MailID");
                    }

                }
                else if(TextUtils.isEmpty(password.getEditText().getText().toString()) && !TextUtils.isEmpty(email.getEditText().getText().toString()))
                {
                    password.setError("Password required");
                }
                else if(!TextUtils.isEmpty(password.getEditText().getText().toString()) && TextUtils.isEmpty(email.getEditText().getText().toString()))
                {
                    email.setError("MailID required");
                }
            }
        });





        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordreset();
            }
        });
    }


    private void loginmethod() {
        String Email = email.getEditText().getText().toString().trim();
        String Password = password.getEditText().getText().toString().trim();


        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String devicetoken = instanceIdResult.getToken();
                            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mref.keepSynced(true);
                            mref.child("DEVICETOKEN").setValue(devicetoken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                    final String savecurrentdate = currentdate.format(calendar.getTime());

                                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                    final String savecurrenttime = currenttime.format(calendar.getTime());

                                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("user_activity").child(firebaseUser.getUid()).push();

                                    HashMap<String,Object> hashMap=new HashMap<>();
                                    hashMap.put("activity","loggedin");
                                    hashMap.put("userid",firebaseUser.getUid());
                                    hashMap.put("pushid",mref.getKey());
                                    hashMap.put("date",savecurrentdate);
                                    hashMap.put("time",savecurrenttime);


                                    mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            finish();
                                            Intent intent = new Intent(MainActivity.this, Startapp.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            Toast.makeText(MainActivity.this, "User Login Sucessful", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void passwordreset()
    {
        String Email=email.getEditText().getText().toString();
        String Password=password.getEditText().getText().toString();

        if(Email.isEmpty())
        {
            email.setError("Email is Required");
        }

        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Reset Email has Sent", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
