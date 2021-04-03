package com.example.project2.GroupChat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;

import com.bumptech.glide.Glide;
import com.example.project2.FriendsChatpad;
import com.example.project2.GalleryPick.GalleryFragment;
import com.example.project2.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    StorageReference mStorageRef;
    CircleImageView groupimage;
    EditText groupname;
    String myUrl;
    TextView noofperson;
    ArrayList<String> noofpart=new ArrayList<>();
    CropImageView cropImageView;
    Uri resulturi,uriimg;
    FloatingActionButton creategroup;
    StorageTask storageTask;
    Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_groupchat);

        noofpart=getIntent().getStringArrayListExtra("list");


        groupimage=findViewById(R.id.groupimage);
        groupname=findViewById(R.id.groupname);
        noofperson=findViewById(R.id.noofperson);
        cropImageView=findViewById(R.id.cropImageView);

        creategroup=findViewById(R.id.creategroup);

        groupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(), GalleryFragment.class);
                intent.putExtra("groupprofile","true");
                startActivityForResult(intent,90);
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        String no=Integer.toString(noofpart.size());
        noofperson.setText(no+" "+"participants");

        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(resulturi!=null && !TextUtils.isEmpty(groupname.getText().toString()) && noofpart.size()!=0)
                {
                    creategroup(resulturi,groupname,noofpart);
                }
                else
                {
                    Toast.makeText(GroupChatActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void creategroup(Uri resulturi, final EditText groupname, final ArrayList<String> noofpart) {

        StorageReference mstoragereference = FirebaseStorage.getInstance().getReference().child("Group_Profiles");

        final StorageReference imagereference = mstoragereference.child(System.currentTimeMillis() + "." +"jpg");

        storageTask = imagereference.putFile(resulturi);
        storageTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imagereference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Uri downloaduri = (Uri) task.getResult();
                    myUrl = downloaduri.toString();


                    final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("Groups").push();
                    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                    HashMap<String,Object> hashMap=new HashMap<>();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                    final String savecurrentdate = currentdate.format(calendar.getTime());

                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                    final String savecurrenttime = currenttime.format(calendar.getTime());

                    hashMap.put("createdby",firebaseUser.getUid());
                    hashMap.put("groupdesc","");
                    hashMap.put("groupimage",myUrl);
                    hashMap.put("groupid",mref.getKey());
                    hashMap.put("grouptitle",groupname.getText().toString());
                    hashMap.put("timestamp", ServerValue.TIMESTAMP);
                    hashMap.put("time",savecurrenttime);
                    hashMap.put("date",savecurrentdate);

                    mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            for(int i=0;i<noofpart.size();i++)
                            {
                                mref.child("participants").child(noofpart.get(i)).child("role").setValue("participant");
                            }
                            Toast.makeText(GroupChatActivity.this, "Group Created!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }
        });

    }


                private String getFileExtension(Uri uri)
                {
                    ContentResolver contentResolver=getContentResolver();
                    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
                    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 90) {
            if (resultCode == RESULT_OK) {
                String images = data.getStringExtra("uri");
                uriimg = Uri.fromFile(new File(images));

                CropImage.activity(uriimg).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resulturi = result.getUri();

                Glide.with(getApplicationContext()).load(resulturi).into(groupimage);
            }
        }
    }
}
