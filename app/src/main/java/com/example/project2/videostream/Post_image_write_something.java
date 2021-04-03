package com.example.project2.videostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.R;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_image_write_something extends AppCompatActivity {

    ImageView uploadpost, cancelpost;
    private StorageReference mStorageRef;
    CircleImageView image;
    TextInputLayout titletext,captiontext,locationtext;
    ImageView poster;
    TextView caption,title,location;
    Bitmap bitmap;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_write_something);

        uploadpost = findViewById(R.id.uploadpost);
        cancelpost = findViewById(R.id.cancelpost);
        final boolean[] s = {false};
        final boolean[] s1 = {false};

        final int[] c = {0};
        final int[] c1 = {0};
        final int[] c2 = {0};

        image = findViewById(R.id.image);
        poster = findViewById(R.id.poster);
        caption = findViewById(R.id.caption);
        captiontext = findViewById(R.id.captiontext);
      //  location = findViewById(R.id.location);
       // locationtext = findViewById(R.id.locationtext);
        title = findViewById(R.id.title);
        titletext = findViewById(R.id.titletext);

        caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1[0]++;
                if(c1[0]==1)
                {
                    captiontext.setVisibility(View.VISIBLE);
                }
                if(c1[0]==2)
                {
                    captiontext.setVisibility(View.GONE);
                    c1[0]=0;
                }

            }
        });


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1[0] =true;
                c[0]++;
                if(c[0]==1)
                {
                    titletext.setVisibility(View.VISIBLE);
                }
                if(c[0]==2)
                {
                    titletext.setVisibility(View.GONE);
                    c[0]=0;
                }

            }
        });



        String uri = getIntent().getStringExtra("resulturi");

        Uri photo=Uri.parse(uri);

        poster.setImageURI(photo);


        String path = getIntent().getStringExtra("path");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("posts").child(firebaseUser.getUid());

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("IMAGEURL").getValue(String.class);

                Picasso.with(getApplicationContext()).load(img).placeholder(R.drawable.pro).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploadpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(captiontext.getEditText().getText().toString())) {


                    final String videoname = getIntent().getStringExtra("videoname");
                    String path = getIntent().getStringExtra("path");
                    String uri = getIntent().getStringExtra("resulturi");
                    final String type = getIntent().getStringExtra("type");
                    final String count = getIntent().getStringExtra("count");

                    Uri resulturi = Uri.parse(uri);


                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                    final String savecurrentdate = currentdate.format(calendar.getTime());


                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                    final String savecurrenttime = currenttime.format(calendar.getTime());

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").child(firebaseUser.getUid()).push();
                    final String push_id = mmref.getKey();


                    mStorageRef = FirebaseStorage.getInstance().getReference();

                    final StorageReference filepath = mStorageRef.child("Profile_posts_images").child(videoname + ".jpg");
                    mmref.keepSynced(true);


                    filepath.putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mStorageRef.child("Profile_posts_images").child(videoname + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String download_url = task.getResult().toString();

                                    mmref.child("imageurl").setValue(download_url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mmref.child("imagename").setValue(videoname).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                                                            mmref.child("description").setValue(captiontext.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    mmref.child("count").setValue(count).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            mmref.child("type").setValue("image").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

                                                                                                    Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
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
}