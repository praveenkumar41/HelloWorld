package com.example.project2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Usereditprofilepage extends AppCompatActivity {

    CircleImageView usereditprofileimage;
    ImageView userprofilechangephoto,usereditprofilewrong;
    EditText editname,editemail,editdob,editbio;
    private StorageReference mStorageRef;


    FirebaseUser firebaseUser;
    private ProgressBar mprogress;
    DatabaseReference mmref;

    private static final int GALLERY_PICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usereditprofilepage);

        usereditprofileimage=findViewById(R.id.usereditprofileimage);
        userprofilechangephoto=findViewById(R.id.userprofilechangephoto);


        usereditprofilewrong=findViewById(R.id.usereditprofilewrong);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        usereditprofilewrong.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        userprofilechangephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryintent=new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryintent,"Select Profile Image"),GALLERY_PICK);

               // CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(Usereditprofilepage.this);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK )
        {
            Uri uriProfileImage = data.getData();
            CropImage.activity(uriProfileImage).setAspectRatio(1,1).start(this);

          //  Toast.makeText(getApplicationContext(), uriProfileImage, Toast.LENGTH_SHORT).show();
        }


        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Working Please Wait...", Toast.LENGTH_LONG).show();


                Uri resultUri=result.getUri();

                final String currentuserid=firebaseUser.getUid();

                mmref= FirebaseDatabase.getInstance().getReference("users").child(currentuserid).child("IMAGEURL");
                mmref.keepSynced(true);

                final StorageReference filepath=mStorageRef.child("Profile_Images").child(currentuserid+".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Task<Uri> download_url =taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        //String downloadurll=download_url.toString();

                        mStorageRef.child("Profile_Images").child(currentuserid+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                  String download_url=task.getResult().toString();

                                mmref.setValue(download_url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });

                    }

                });

            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error=result.getError();
            }
        }

   }

/*    public static String random()
    {
        Random generator=new Random();
        StringBuilder randomStringBulider=new StringBuilder();
        int randomlength =generator.nextInt(10);
        char tempchar;
        for(int i=0;i<randomlength;i++)
        {
            tempchar=(char)(generator.nextInt(96)+32);
            randomStringBulider.append(tempchar);
        }
        return randomStringBulider.toString();

    }

 */
}