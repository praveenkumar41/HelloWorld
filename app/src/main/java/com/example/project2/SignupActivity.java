package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.os.Build.ID;

public class SignupActivity extends AppCompatActivity implements Country_names_popup.CountrynamesListener{

    EditText password,email,phone,phonepassword,code;
    Button button;
    FirebaseAuth mAuth;
    ProgressBar bar;
    FirebaseUser firebaseUser;
    DatabaseReference dataDetails,ref;
    TextView select;
    int flag=0;

    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        button=findViewById(R.id.button);

        code=findViewById(R.id.code);

        select=findViewById(R.id.select);

        phone=findViewById(R.id.phone);
        bar=findViewById(R.id.bar);
        mAuth = FirebaseAuth.getInstance();
        dataDetails= FirebaseDatabase.getInstance().getReference();

        ref= FirebaseDatabase.getInstance().getReference("users");
        ref.keepSynced(true);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(select.getText().toString().equals("instead use phone"))
                {
                    select.setText("instead use email");
                    phone.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                }
                else
                {
                    select.setText("instead use phone");
                    email.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.GONE);
                    code.setVisibility(View.GONE);
                }
            }
        });


        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Country_names_popup cn=new Country_names_popup();
                cn.show(getSupportFragmentManager(),"country names");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additems();
            }
        });
    }

    @Override
    public void applycodes(String codes) {
        code.setText("+"+codes);
    }


    private void additems() {

        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String codes = code.getText().toString().trim();
        String phonenumber = phone.getText().toString().trim();


        if ((!TextUtils.isEmpty(Email)) && (TextUtils.isEmpty(Password))) {
            Toast.makeText(SignupActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
        }
        if ((!TextUtils.isEmpty(Email)) && (!TextUtils.isEmpty(Password))) {
            email_aunthentication(Email,Password);
        } else if ((!TextUtils.isEmpty(codes)) && (!TextUtils.isEmpty(phonenumber)) && (!TextUtils.isEmpty(Password))) {
            Toast.makeText(SignupActivity.this, "Password required", Toast.LENGTH_SHORT).show();

            String Rphonenumber=codes+phonenumber;

            phone_aunthentication(codes,Rphonenumber,Password,phonenumber);
        }

    }

    private void phone_aunthentication(String codes, String Rphonenumber, String phonepass, String phonenumber)
    {
        Intent intent=new Intent(getApplicationContext(),phone_code_verification.class);
        intent.putExtra("code",codes);
        intent.putExtra("realphone",Rphonenumber);
        intent.putExtra("phonepassword",phonepass);
        intent.putExtra("phonenumber",phonenumber);
        startActivity(intent);
    }


    private void email_aunthentication(String email, String password) {
        button.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                button.setVisibility(View.VISIBLE);
                bar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    final String currentid=mAuth.getCurrentUser().getUid();

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String devicetoken = instanceIdResult.getToken();
                            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mref.keepSynced(true);
                            mref.child("DEVICETOKEN").setValue(devicetoken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    finish();
                                    Intent intent = new Intent(SignupActivity.this, StartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "user Register sucessful", Toast.LENGTH_SHORT).show();

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
}


