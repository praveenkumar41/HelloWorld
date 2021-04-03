package com.example.project2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.Newsignin.FirstPage_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class Intoapp extends AppCompatActivity {

    Button b1, b2;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {

            databaseReference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            databaseReference.keepSynced(true);

            databaseReference.child("online").setValue("true");
            Intent intent=new Intent(getApplicationContext(),Startapp.class);
            startActivity(intent);

        }
        else{

          // databaseReference.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
       // databaseReference.child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onPause() {
        super.onPause();
     //  databaseReference.child("online").setValue(ServerValue.TIMESTAMP);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intoapp);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intoapp.this, MainActivity.class);
                startActivity(intent);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intoapp.this, FirstPage_Activity.class);
                startActivity(intent);
            }
        });



    }
}
