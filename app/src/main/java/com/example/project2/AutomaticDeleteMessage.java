package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
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

import java.util.Objects;

public class AutomaticDeleteMessage extends AppCompatActivity {

    Switch switchdeletemessage;
    DatabaseReference mref,mref1,ref2;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_delete_message);

        final String userid=getIntent().getStringExtra("userid");

        switchdeletemessage=findViewById(R.id.switchdeletemessage);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();



        switchdeletemessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b)
      {
          if(b){

              mref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
              mref.child("TOGGLE").setValue("ON").addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(getApplicationContext(), "Done..", Toast.LENGTH_SHORT).show();
                  }
              });
          }
          else{

              mref=FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
              mref.child("TOGGLE").setValue("OFF").addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(getApplicationContext(), "Done..", Toast.LENGTH_SHORT).show();
                  }
              });
          }

      }
  });


    }

}
