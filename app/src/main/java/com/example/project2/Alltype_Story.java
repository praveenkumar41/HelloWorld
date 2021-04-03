package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project2.GalleryPick.GalleryFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.luolc.emojirain.EmojiRainLayout;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.otaliastudios.autocomplete.CharPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Alltype_Story extends AppCompatActivity {

    CircleImageView back, image, text, camera, gallery, poll, closephoto, phototext;
    CardView card;

    Button poststory;
    Uri uriimg;

    TextView name;
    EditText ed;
    PinchZoomImageView story_image;
    String myUrl;
    StorageTask storageTask;
    StorageReference mstoragereference;
    private static final int GALLERY_PICK = 101;
    private static final int VIDEO_PICK = 102;
    private static final int CAMERA_PICK = 99;
    Uri mImageuri, Videouri;
    RelativeLayout edlayout, btlayout, main;

    CircleImageView cardimage;
    TextView cardtitle, cardtitle1, description, likes, comments, repostcount;
    PinchZoomImageView thumbnail, videothumbnail;
    ImageView clickplay;
    Uri imagepick;
    private Autocomplete mentionsAutocomplete,HashtagAutocomplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltype_story);

        final String postid = getIntent().getStringExtra("postid");
        String set = getIntent().getStringExtra("set");

        card = findViewById(R.id.card);

        cardimage = findViewById(R.id.cardimage);
        cardtitle = findViewById(R.id.cardtitle);
        cardtitle1 = findViewById(R.id.cardtitle1);
        description = findViewById(R.id.description);
        likes = findViewById(R.id.likes);
        comments = findViewById(R.id.comments);
        thumbnail = findViewById(R.id.thumbnail);
        videothumbnail = findViewById(R.id.videothumbnail);
        clickplay = findViewById(R.id.clickplay);

        final String[] bitmap = {""};
        final String[] description1 = {""};
        final String[] imageurl = {""};
        final String[] publisher = {""};
        final String[] pushid = {""};
        final String[] type = {""};

        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        poll = findViewById(R.id.poll);
        poststory = findViewById(R.id.poststory);
        name = findViewById(R.id.name);
        story_image = findViewById(R.id.story_image);
        edlayout = findViewById(R.id.edlayout);
        btlayout = findViewById(R.id.btlayout);

        phototext = findViewById(R.id.phototext);
        closephoto = findViewById(R.id.closephoto);
        main = findViewById(R.id.main);

        ed = findViewById(R.id.ed);
        ed.setBackgroundResource(android.R.color.transparent);

        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Poll_Activity.class);
                startActivity(intent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
                imagepick = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagepick);
                startActivityForResult(intent, CAMERA_PICK);
            }
        });

        final String finalSet = set;
        poststory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uriimg != null)
                {
                    publishStory(uriimg);
                }
                else if( imagepick!=null)
                {
                    publishStory(imagepick);
                }
                else if (!TextUtils.isEmpty(ed.getText().toString())) {
                    publishtextStory(ed);
                } else if (Videouri != null) {
                    publishvideoStory(Videouri);
                } else if ("true".equals(finalSet)) {
                    publishcard(postid,bitmap[0],description1[0],imageurl[0],publisher[0],pushid[0],type[0]);
                }
            }
        });


        if ("true".equals(set) && !TextUtils.isEmpty(postid)) {
            DatabaseReference reference99 = FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
            reference99.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    card.setVisibility(View.VISIBLE);

                    bitmap[0] = dataSnapshot.child("bitmap").getValue(String.class);
                    description1[0] = dataSnapshot.child("description").getValue(String.class);
                    imageurl[0] = dataSnapshot.child("imageurl").getValue(String.class);
                    publisher[0] = dataSnapshot.child("publisher").getValue(String.class);
                    pushid[0] = dataSnapshot.child("pushid").getValue(String.class);
                    type[0] = dataSnapshot.child("type").getValue(String.class);


                    if ("image".equals(type[0]) && !TextUtils.isEmpty(description1[0])) {

                        description.setText(description1[0]);

                        Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(), bitmap[0]);
                        thumbnail.setImageBitmap(bitmap11);
                        videothumbnail.setVisibility(View.GONE);

                    } else if ("image".equals(type[0]) && TextUtils.isEmpty(description1[0])) {
                        description.setVisibility(View.GONE);
                        Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(), bitmap[0]);
                        thumbnail.setImageBitmap(bitmap11);
                        videothumbnail.setVisibility(View.GONE);

                    } else if ("text".equals(type[0])) {
                        thumbnail.setVisibility(View.GONE);
                        videothumbnail.setVisibility(View.GONE);
                        description.setText(description1[0]);


                    } else if ("video".equals(type[0]) && !(TextUtils.isEmpty(description1[0]))) {

                        Uri uri = Uri.parse(imageurl[0]);
                        clickplay.setVisibility(View.VISIBLE);
                        thumbnail.setVisibility(View.GONE);

                        Glide.with(getApplicationContext()).asBitmap()
                                .load(uri)
                                .into(videothumbnail);

                        description.setText(description1[0]);


                    } else if ("video".equals(type[0]) && (TextUtils.isEmpty(description1[0]))) {

                        thumbnail.setVisibility(View.GONE);
                        Uri uri = Uri.parse(imageurl[0]);
                        clickplay.setVisibility(View.VISIBLE);

                        Glide.with(getApplicationContext()).asBitmap()
                                .load(uri)
                                .into(videothumbnail);

                        description.setVisibility(View.GONE);
                    }

                    btlayout.setVisibility(View.GONE);
                    edlayout.setVisibility(View.GONE);
                    ed.setVisibility(View.GONE);


                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(publisher[0]);

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String image1 = dataSnapshot.child("IMAGEURL").getValue(String.class);
                            String name11 = dataSnapshot.child("USERNAME").getValue(String.class);
                            String username = dataSnapshot.child("USERNAME_ID").getValue(String.class);
                            String name1 = name11.substring(0, 1).toUpperCase() + name11.substring(1);

                            name.setText("@"+username);
                            cardtitle.setText(name1);
                            Picasso.with(getApplicationContext()).load(image1).placeholder(R.drawable.pro).into(cardimage);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            set = "false";
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(), GalleryFragment.class);
                startActivityForResult(intent,1);

                /*

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialogforfetchpost, null);


                CardView c1 = view1.findViewById(R.id.c1);
                CardView c2 = view1.findViewById(R.id.c2);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

                c1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent galleryintent = new Intent();
                        galleryintent.setType("image/*");
                        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryintent, "Select Image"), GALLERY_PICK);
                        dialog.dismiss();
                    }
                });

                c2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_PICK);
                        dialog.dismiss();

                    }
                });

                */
            }
        });


        closephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                story_image.setVisibility(View.GONE);
                btlayout.setVisibility(View.VISIBLE);
                phototext.setVisibility(View.GONE);
                closephoto.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                edlayout.setVisibility(View.VISIBLE);
                ed.setVisibility(View.VISIBLE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setupHashtagAutocomplete();
        setupMentionsAutocomplete();
    }


    private void publishcard(String postid, String bitmap, String description, String imageurl, String publisher, String pushid, String type) {

        String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("story").child(myid);
        reference.keepSynced(true);
        String storyid = reference.push().getKey();
        long timeend = System.currentTimeMillis() + 86400000;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("imageurl",imageurl);
        hashMap.put("timestart", ServerValue.TIMESTAMP);
        hashMap.put("timeend", timeend);
        hashMap.put("storyid", storyid);
        hashMap.put("userid", myid);
        hashMap.put("type", "card");
        hashMap.put("description",description);
        hashMap.put("bitmap",bitmap);
        hashMap.put("postid",postid);
        hashMap.put("publisher",publisher);
        hashMap.put("cardtype",type);
        hashMap.put("progress","");


        reference.child(storyid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    finish();
                }
            }
        });
    }



    private void publishvideoStory(Uri videouri) {


        if(Videouri!=null)
        {
            mstoragereference= FirebaseStorage.getInstance().getReference().child("story");

            final StorageReference reference=mstoragereference.child(System.currentTimeMillis()+"."+getFileExtension(Videouri));

            storageTask=reference.putFile(Videouri);

            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return reference.getDownloadUrl();
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
                        hashMap.put("type","video");

                        reference.child(storyid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                finish();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(Alltype_Story.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Alltype_Story.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No video Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void publishtextStory(EditText ed) {

        if(!TextUtils.isEmpty(ed.getText().toString()))
        {

                 String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                 DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("story").child(myid);
                 reference.keepSynced(true);
                 String storyid=reference.push().getKey();
                 long timeend=System.currentTimeMillis()+86400000;

                 HashMap<String,Object> hashMap=new HashMap<>();
                 hashMap.put("imageurl",ed.getText().toString());
                 hashMap.put("timestart", ServerValue.TIMESTAMP);
                 hashMap.put("timeend",timeend);
                 hashMap.put("storyid",storyid);
                 hashMap.put("userid",myid);
                 hashMap.put("type","text");

                 reference.child(storyid).setValue(hashMap);

                 finish();

        }
        else {
                 Toast.makeText(Alltype_Story.this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void publishStory(Uri uri)
    {
        if(uri!=null)
        {

            mstoragereference= FirebaseStorage.getInstance().getReference().child("story");

            final StorageReference imagereference=mstoragereference.child(System.currentTimeMillis()+"."+getFileExtension(uri));

            storageTask=imagereference.putFile(uri);
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
                        hashMap.put("type","image");

                        reference.child(storyid).setValue(hashMap);

                        finish();
                    }
                    else
                    {
                        Toast.makeText(Alltype_Story.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Alltype_Story.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /*
        if(resultCode==RESULT_OK)
        {
            if(requestCode==GALLERY_PICK)
            {
                mImageuri=data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageuri);
                    story_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ed.setVisibility(View.GONE);
                edlayout.setVisibility(View.GONE);
                btlayout.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                closephoto.setVisibility(View.VISIBLE);
                phototext.setVisibility(View.VISIBLE);

            }
            else if(requestCode==VIDEO_PICK)
            {
                Videouri=data.getData();
            }
        }
        */
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            if(requestCode==1)
            {

                String images=data.getStringExtra("uri");
                uriimg=Uri.fromFile(new File(images));

                Glide.with(getApplicationContext()).load(images).into(story_image);

                ed.setVisibility(View.GONE);
                edlayout.setVisibility(View.GONE);
                btlayout.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                closephoto.setVisibility(View.VISIBLE);
                phototext.setVisibility(View.VISIBLE);

            }
            else if (requestCode==99)
            {
                Glide.with(getApplicationContext()).load(imagepick).into(story_image);

                ed.setVisibility(View.GONE);
                edlayout.setVisibility(View.GONE);
                btlayout.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                closephoto.setVisibility(View.VISIBLE);
                phototext.setVisibility(View.VISIBLE);

            }
        }

    }

    public Bitmap getBitmapImage(PinchZoomImageView imageView){
        Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        return bm;
    }

    private void setupMentionsAutocomplete() {

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('@'); // Look for @mentions
        AutocompletePresenter<Details> presenter = new UserPresenter(this);
        AutocompleteCallback<Details> callback = new AutocompleteCallback<Details>() {
            @Override
            public boolean onPopupItemClicked(@NonNull Editable editable, @NonNull Details item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getUSERNAME_ID();
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start+replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }
            public void onPopupVisibilityChanged(boolean shown) {}
        };

        mentionsAutocomplete = Autocomplete.<Details>on(ed)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
    }


    private void setupHashtagAutocomplete() {

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('#'); // Look for @mentions
        AutocompletePresenter<String> presenter = new HashPresenter(this);
        AutocompleteCallback<String> callback = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(@NonNull Editable editable, @NonNull String item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item;
                editable.replace(start, end, replacement);

                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start+replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }
            public void onPopupVisibilityChanged(boolean shown) {}
        };

        HashtagAutocomplete = Autocomplete.<String>on(ed)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
    }
}
