package com.example.project2.Newsignin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Details;
import com.example.project2.R;
import com.example.project2.Startapp;
import com.example.project2.Suggestion_list_Adapter;
import com.example.project2.User_ids;
import com.example.project2.videostream.MediaObject1;
import com.example.project2.videostream.PostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Getting_Username extends AppCompatActivity implements Suggestion_list_Adapter.AdapterListener{

    TextInputLayout usernamebox;
    TextInputEditText username;
    Button button;
    TextView exists;
    List<String> suggestionlist;

    DatabaseReference dataDetails,dataDetails1,data;
    FirebaseUser firebaseUser;
    String name,dob,devicetoken,password,date,time,cc,cn;
    int flag=0;
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    Suggestion_list_Adapter adapter;
    String name1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getting_username);

        rv=findViewById(R.id.rv);


        exists=findViewById(R.id.exists);


        usernamebox = findViewById(R.id.usernamebox);
        username = findViewById(R.id.username);
        button = findViewById(R.id.button);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        dataDetails= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        dataDetails1= FirebaseDatabase.getInstance().getReference("usersdisplay").child(firebaseUser.getUid());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!TextUtils.isEmpty(usernamebox.getEditText().getText().toString()))) {

                    checking_username_alreadyexist_or_not(usernamebox.getEditText().getText().toString());

                }
            }
        });

        getting_suggestion_list();

        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);
        adapter=new Suggestion_list_Adapter(suggestionlist,this);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

    }


    private void getting_suggestion_list() {
        suggestionlist=new ArrayList<>();

        FirebaseUser firebaseUser11=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser11.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                suggestionlist.clear();
                String name=dataSnapshot.child("USERNAME").getValue(String.class);

                if(!TextUtils.isEmpty(name) && name.contains(" "))
                {
                    name1=name.replace(" ","");
                }
                else if(!TextUtils.isEmpty(name))
                {
                    name1=name;
                }

                    for(int i=0;i<16;i++)
                    {
                        Random random=new Random();
                        String id=String.format("%04d",random.nextInt(10000));
                        String total=name1+id;

                        suggestionlist.add(total);

                    }
                    String count=Integer.toString(suggestionlist.size());
                Toast.makeText(Getting_Username.this, count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checking_username_alreadyexist_or_not(final String s) {

        final List<String> alluserids = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (s.equals(snapshot.child("USERNAME_ID").getValue(String.class))) {
                 //       alluserids.add(s);
                        flag=1;
                        break;
                    }
                }

                String c=Integer.toString(alluserids.size());
                Toast.makeText(Getting_Username.this, c, Toast.LENGTH_SHORT).show();


                if(flag==0)
                {
                    DatabaseReference pushuserid=FirebaseDatabase.getInstance().getReference().child("Alluserids").child("@"+usernamebox.getEditText().getText().toString()).child("id");
                    pushuserid.setValue(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            dataDetails.child("USERNAME_ID").setValue("@"+usernamebox.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dataDetails.keepSynced(true);
                                    if (task.isSuccessful()) {

                                        dataDetails1.child("USERNAME_ID").setValue("@"+usernamebox.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Intent intent = new Intent(getApplicationContext(),Startapp.class);
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
                    });
                }
                else {
                    exists.setText("already exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void applycodes(String codes) {

        usernamebox.getEditText().setText(codes);
    }
}