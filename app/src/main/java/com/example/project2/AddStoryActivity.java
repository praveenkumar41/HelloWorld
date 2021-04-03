package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {

    private Uri mImageuri;
    private static final int GALLERY_PICK=101;
    String myUrl="";
    private StorageTask storageTask;
    StorageReference mstoragereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        mstoragereference= FirebaseStorage.getInstance().getReference().child("story");

        Intent galleryintent = new Intent();
        galleryintent.setType("image/*");
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryintent, "Select Image"), GALLERY_PICK);


     //   CropImage.activity().setAspectRatio(16,16).start(AddStoryActivity.this);

    }


    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void publishStory()
    {
       final ProgressBar pd=new ProgressBar(this);

        if(mImageuri!=null)
        {
            final StorageReference imagereference=mstoragereference.child(System.currentTimeMillis()+"."+getFileExtension(mImageuri));

            storageTask=imagereference.putFile(mImageuri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return imagereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                   if(task.isSuccessful())
                   {
                       Uri downloaduri= (Uri) task.getResult();
                       myUrl=downloaduri.toString();

                       String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                       DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("story").child(myid);
                       reference.keepSynced(true);
                       String storyid=reference.push().getKey();
                       long timeend=System.currentTimeMillis()+86400000;

                       HashMap<String,Object> hashMap=new HashMap<>();
                       hashMap.put("imageurl",myUrl);
                       hashMap.put("timestart", ServerValue.TIMESTAMP);
                       hashMap.put("timeend",timeend);
                       hashMap.put("storyid",storyid);
                       hashMap.put("userid",myid);

                       reference.child(storyid).setValue(hashMap);
                       pd.setVisibility(View.GONE);

                       finish();

                   }
                   else
                   {
                       Toast.makeText(AddStoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
        {

            mImageuri=data.getData();

            String url= String.valueOf(mImageuri);

            Intent intent=new Intent(getApplicationContext(),Alltype_Story.class);
            intent.putExtra("uri",url);
            startActivity(intent);

            finish();

           // publishStory();
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}
