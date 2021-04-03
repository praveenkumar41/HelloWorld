package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_write_something extends AppCompatActivity {

    ImageView uploadpost, cancelpost;
    private StorageReference mStorageRef;
    CircleImageView image;
    TextInputLayout des;
    Bitmap bitmap;
    String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write_something);

        uploadpost = findViewById(R.id.uploadpost);
        cancelpost = findViewById(R.id.cancelpost);

        image=findViewById(R.id.image);
        des=findViewById(R.id.des);

        String path = getIntent().getStringExtra("path");

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("posts").child(firebaseUser.getUid());
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img=dataSnapshot.child("IMAGEURL").getValue(String.class);

                Picasso.with(getApplicationContext()).load(img).placeholder(R.drawable.pro).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        uploadpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(des.getEditText().getText().toString()))
                {

                final String videoname = getIntent().getStringExtra("videoname");
                String path = getIntent().getStringExtra("path");
                String uri = getIntent().getStringExtra("resulturi");
                    String type = getIntent().getStringExtra("type");
                final String count=getIntent().getStringExtra("count");

                Uri resulturi = Uri.parse(uri);

                    Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                final String savecurrentdate = currentdate.format(calendar.getTime());


                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                final String savecurrenttime = currenttime.format(calendar.getTime());

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts_videos").child(firebaseUser.getUid()).push();
                final String push_id = mmref.getKey();


                mStorageRef = FirebaseStorage.getInstance().getReference();


                final StorageReference filepath = mStorageRef.child("Profile_posts_videos").child(videoname + ".mp4");
                mmref.keepSynced(true);


                    filepath.putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        mStorageRef.child("Profile_posts_videos").child(videoname + ".mp4").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String download_url = task.getResult().toString();

                                mmref.child("videourl").setValue(download_url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mmref.child("videoname").setValue(videoname).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mmref.child("pushid").setValue(push_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mmref.child("date").setValue(savecurrentdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                mmref.child("time").setValue(savecurrenttime).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        mmref.child("description").setValue(des.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                mmref.child("count").setValue(count).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {


                                                                                        mmref.child("type").setValue("video").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {


                                                                                                Toast.makeText(getApplicationContext(), "Song upload Successfull", Toast.LENGTH_SHORT).show();
                                                                                                finish();

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

                            }
                        });

                    }

                }
        });

        cancelpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
/*
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
*/

}