package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project2.GalleryPick.GalleryFragment;
import com.example.project2.PinchZoomImageView;
import com.example.project2.Post_write_something;
import com.example.project2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.otaliastudios.autocomplete.CharPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class AddPostActivity extends AppCompatActivity implements LocationListener {

    EditText edittext, imageedittext;
    private Autocomplete mentionsAutocomplete,HashtagAutocomplete;
    TextView tagpeople;
    CircleImageView image;
    ImageView finish,imageplaybutton,removephoto;
    Button posttoall,button;
    RelativeLayout rl;
    ImageView gallery,location,camera;
    PinchZoomImageView thumbnail;
    Uri videouri,uriimg;
    Bitmap videoplaybitmap1;
    RelativeLayout relative;
    ListView itemlist;
    Uri img_uri=null;
    LinearLayout addlocation;
    ImageView removecard;
    LocationManager locationManager;
    Autocomplete hashAutocomplete;

    private final String CHANNEL_ID="1";
    private final int NOTIFICATION_ID=001;

    private static final int GALLERY_PICK = 1;
    private static final int VIDEO_PICK = 80;
    private static final int CAMERA_PICK = 102;

    private static final int STORAGE_REQUEST = 10;
    private static final int CAMERA_REQUEST = 11;

    ArrayList<Bitmap> imagelist1=new ArrayList<>();
    ArrayList<Bitmap>videoplayingbitmap=new ArrayList<>();

    Uri imagepick=null;
    CardView card,urlcard;
    int r;
    String random;
    MetaData data;
    ImageView urlimage;
    TextView urldesc,urltitle,urllink;

    private ArrayAdapter<String> listAdapter;

    String[] camerapermission;
    String[] storagepermission;
    PinchZoomImageView zoomimage;
    String bitma,bitmap99,bitma1;
    boolean yescard=false;
    List<Details> muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        removecard=findViewById(R.id.removecard);
        urlimage=findViewById(R.id.urlimage);
        urldesc=findViewById(R.id.urldesc);
        urltitle=findViewById(R.id.urltitle);
        urllink=findViewById(R.id.urllink);


        urlcard=findViewById(R.id.urlcard);

        thumbnail=findViewById(R.id.thumbnail);
        rl=findViewById(R.id.rl);
        gallery=findViewById(R.id.gallery);
        removephoto=findViewById(R.id.removephoto);

        tagpeople=findViewById(R.id.tagpeople);
        addlocation=findViewById(R.id.addlocation);
        imageplaybutton=findViewById(R.id.imageplaybutton);
        relative=findViewById(R.id.relative);
        location=findViewById(R.id.location);
        camera=findViewById(R.id.camera);

        edittext = findViewById(R.id.edittext);
        image = findViewById(R.id.image);
        posttoall = findViewById(R.id.posttoall);
        finish = findViewById(R.id.finish);

        edittext.setSelection(0);
        edittext.requestFocus(0);

        edittext.setBackgroundResource(android.R.color.transparent);

        camerapermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

     //   Intent intent=getIntent();

     //   String action=intent.getAction();
     //   String type=intent.getType();

  /*
        if(Intent.ACTION_SEND.equals(action) && type!=null)
        {
            if("text/plain".equals(type))
            {
                handlesendText(intent);
            }
            else if(type.startsWith("image"))
            {
                handlesendImage(intent);
            }
        }
*/

        finish=findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edittext.setMaxLines(23);
                if (!checkcamerapermission()) {
                    requestcamerapermission();
                } else {
                    PickFromCamera();

                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddPostActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                else
                {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationEnabled();
                    getLocation();
                }
            }
        });

        removecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlcard.setVisibility(View.GONE);
                yescard=false;

            }
        });

        final Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        final Pattern hashtagPattern = Pattern.compile("#([ء-يA-Za-z0-9_-]+)");
        final String hashtagScheme = "content://com.hashtag.jojo/";

        final Pattern urlPattern = Patterns.DOMAIN_NAME;


        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().equals(""))
                {
                    urlcard.setVisibility(View.GONE);
                    gallery.setEnabled(true);
                    camera.setEnabled(true);
                    location.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                /*
                if(!charSequence.toString().equals(""))
                {
                    if(yescard)
                    {
                     //   urlcard.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        urlcard.setVisibility(View.GONE);
                    }
                }
                */
                if(charSequence.toString().equals(""))
                {
                    urlcard.setVisibility(View.GONE);
                    gallery.setEnabled(true);
                    camera.setEnabled(true);
                    location.setEnabled(true);
                }
                else if(!yescard)
                {
                    urlcard.setVisibility(View.GONE);
                    gallery.setEnabled(true);
                    camera.setEnabled(true);
                    location.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


                    if (isValidUrl(editable.toString()) || isValidDomain(editable.toString())) {
                        if (editable.toString().contains("http://") || editable.toString().contains("https://"))
                        {
                            edittext.setTextColor(Color.parseColor("#1DA1F2"));

                            RichPreview richPreview = new RichPreview(new ResponseListener() {
                                @Override
                                public void onData(MetaData metaData) {

                                    data = metaData;

                                    if (data != null && isValidUrl(edittext.getText().toString()) || isValidDomain(edittext.getText().toString())) {
                                        Glide.with(getApplicationContext()).load(metaData.getImageurl()).into(urlimage);

                                        urltitle.setText(metaData.getTitle());
                                        urldesc.setText(metaData.getDescription());
                                        urllink.setText(metaData.getUrl());
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                            richPreview.getPreview(editable.toString());
                            urlcard.setVisibility(View.VISIBLE);
                            yescard=true;
                            gallery.setEnabled(false);
                            camera.setEnabled(false);
                            location.setEnabled(false);
                        }
                        else
                        {

                            RichPreview richPreview = new RichPreview(new ResponseListener() {
                                @Override
                                public void onData(MetaData metaData) {

                                    data = metaData;

                                    if (data != null) {
                                        Glide.with(getApplicationContext()).load(metaData.getImageurl()).into(urlimage);

                                        urltitle.setText(metaData.getTitle());
                                        urldesc.setText(metaData.getDescription());
                                        urllink.setText(metaData.getUrl());
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                            richPreview.getPreview("http://"+edittext.getText().toString());
                            urlcard.setVisibility(View.VISIBLE);
                            gallery.setEnabled(false);
                            yescard=true;
                            camera.setEnabled(false);
                            location.setEnabled(false);

                        }
                    }
                }
        });


        urlcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data!=null)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
                }
            }
        });


        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mreference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image1=dataSnapshot.child("IMAGEURL").getValue(String.class);
                Picasso.with(getApplicationContext()).load(image1).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        posttoall.setOnClickListener(new View.OnClickListener()
        {
                                         @Override
                                         public void onClick(View view) {

                                             if (!TextUtils.isEmpty(edittext.getText().toString()) && uriimg != null)
                                             {

                                                 final Bitmap s = getBitmapImage(thumbnail);

                                                 ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                 s.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                 byte[] b = boas.toByteArray();
                                                 bitma = Base64.encodeToString(b, Base64.DEFAULT);


                                                 Calendar calendar = Calendar.getInstance();
                                                 SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                 final String savecurrentdate = currentdate.format(calendar.getTime());

                                                 SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                 final String savecurrenttime = currenttime.format(calendar.getTime());

                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                 final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                 final String push_id = mmref.getKey();


                                                 final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                                                 final StorageReference filepath = mStorageRef.child("Profile_posts_images").child("" + ".jpg");
                                                 mmref.keepSynced(true);


                                                 filepath.putFile(uriimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                         mStorageRef.child("Profile_posts_images").child("" + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Uri> task) {
                                                                 String downloadurl = task.getResult().toString();


                                                                 HashMap<String, Object> hashMap = new HashMap<>();

                                                                 hashMap.put("imageurl", downloadurl);
                                                                 hashMap.put("pushid", push_id);
                                                                 hashMap.put("date", savecurrentdate);
                                                                 hashMap.put("time", savecurrenttime);
                                                                 hashMap.put("description", edittext.getText().toString());
                                                                 hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                 hashMap.put("type", "image");
                                                                 hashMap.put("bitmap",bitma);
                                                                 hashMap.put("publisher", firebaseUser.getUid());
                                                                 hashMap.put("tempdate", tempdate);
                                                                 hashMap.put("perdate", perdate);

                                                                 mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                         Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                         finish();

                                                                     }
                                                                 });
                                                             }
                                                         });

                                                     }
                                                 });
                                             } else if (videouri != null  && (!TextUtils.isEmpty(edittext.getText().toString()))) {

                                                 //  Bitmap bitmap100 = videoplayingbitmap.get(0);

/*
                                                 ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                 bitmap100.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                 byte[] b = boas.toByteArray();
                                                 bitmap99 = Base64.encodeToString(b, Base64.DEFAULT);
*/

                                                 Calendar calendar = Calendar.getInstance();
                                                 SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                 final String savecurrentdate = currentdate.format(calendar.getTime());


                                                 SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                 final String savecurrenttime = currenttime.format(calendar.getTime());


                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                 final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                 final String push_id = mmref.getKey();

                                                 final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


                                                 final StorageReference filepath = mStorageRef.child("Profile_posts_videos").child("" + ".mp4");
                                                 mmref.keepSynced(true);


                                                 filepath.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                         mStorageRef.child("Profile_posts_videos").child("" + ".mp4").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Uri> task) {
                                                                 String download_url = task.getResult().toString();


                                                                 HashMap<String, Object> hashMap = new HashMap<>();

                                                                 hashMap.put("imageurl", download_url);
                                                                 hashMap.put("pushid", push_id);
                                                                 hashMap.put("date", savecurrentdate);
                                                                 hashMap.put("time", savecurrenttime);
                                                                 hashMap.put("description", edittext.getText().toString());
                                                                 hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                 hashMap.put("type", "image");
                                                                 hashMap.put("bitmap", bitmap99);
                                                                 hashMap.put("publisher", firebaseUser.getUid());
                                                                 hashMap.put("tempdate",tempdate);
                                                                 hashMap.put("perdate",perdate);

                                                                 mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                         Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                         finish();
                                                                     }
                                                                 });
                                                             }

                                                         });
                                                     }
                                                 });
                                             }
                                             else if(imagepick!=null && !(TextUtils.isEmpty(edittext.getText().toString()))) {

                                                 final Bitmap s = getBitmapImage(thumbnail);

                                                 ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                 s.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                 byte[] b = boas.toByteArray();
                                                 bitma = Base64.encodeToString(b, Base64.DEFAULT);


                                                 Calendar calendar = Calendar.getInstance();
                                                 SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                 final String savecurrentdate = currentdate.format(calendar.getTime());

                                                 SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                 final String savecurrenttime = currenttime.format(calendar.getTime());

                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                 final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                 final String push_id = mmref.getKey();


                                                 final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                                                 final StorageReference filepath = mStorageRef.child("Profile_posts_images").child("" + ".jpg");
                                                 mmref.keepSynced(true);


                                                 filepath.putFile(imagepick).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                         mStorageRef.child("Profile_posts_images").child("" + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Uri> task) {
                                                                 String downloadurl = task.getResult().toString();


                                                                 HashMap<String, Object> hashMap = new HashMap<>();

                                                                 hashMap.put("imageurl", downloadurl);
                                                                 hashMap.put("pushid", push_id);
                                                                 hashMap.put("date", savecurrentdate);
                                                                 hashMap.put("time", savecurrenttime);
                                                                 hashMap.put("description", edittext.getText().toString());
                                                                 hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                 hashMap.put("type", "image");
                                                                 hashMap.put("bitmap", bitma);
                                                                 hashMap.put("publisher", firebaseUser.getUid());
                                                                 hashMap.put("tempdate", tempdate);
                                                                 hashMap.put("perdate", perdate);

                                                                 mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                         Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                         finish();

                                                                     }
                                                                 });
                                                             }
                                                         });
                                                     }
                                                 });

                                             }
                                             else if(imagepick!=null && TextUtils.isEmpty(edittext.getText().toString()))
                                             {

                                                 final Bitmap s = getBitmapImage(thumbnail);

                                                 ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                 s.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                 byte[] b = boas.toByteArray();
                                                 bitma = Base64.encodeToString(b, Base64.DEFAULT);


                                                 Calendar calendar = Calendar.getInstance();
                                                 SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                 final String savecurrentdate = currentdate.format(calendar.getTime());

                                                 SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                 final String savecurrenttime = currenttime.format(calendar.getTime());

                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                 final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                 final String push_id = mmref.getKey();


                                                 final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                                                 final StorageReference filepath = mStorageRef.child("Profile_posts_images").child("" + ".jpg");
                                                 mmref.keepSynced(true);


                                                 filepath.putFile(imagepick).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                         mStorageRef.child("Profile_posts_images").child("" + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Uri> task) {
                                                                 String downloadurl = task.getResult().toString();


                                                                 HashMap<String, Object> hashMap = new HashMap<>();

                                                                 hashMap.put("imageurl", downloadurl);
                                                                 hashMap.put("pushid", push_id);
                                                                 hashMap.put("date", savecurrentdate);
                                                                 hashMap.put("time", savecurrenttime);
                                                                 hashMap.put("description", "");
                                                                 hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                 hashMap.put("type", "image");
                                                                 hashMap.put("bitmap", bitma);
                                                                 hashMap.put("publisher", firebaseUser.getUid());
                                                                 hashMap.put("tempdate", tempdate);
                                                                 hashMap.put("perdate", perdate);

                                                                 mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task) {

                                                                         Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                         finish();

                                                                     }
                                                                 });
                                                             }
                                                         });
                                                     }
                                                 });

                                             }

                                             else if(videouri!=null && TextUtils.isEmpty(edittext.getText().toString()))
                                             {
                                                    Bitmap perImage = imagelist1.get(0);

                                                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                    perImage.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                    byte[] b = boas.toByteArray();
                                                    bitma1 = Base64.encodeToString(b, Base64.DEFAULT);

                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                    final String savecurrentdate = currentdate.format(calendar.getTime());


                                                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                    final String savecurrenttime = currenttime.format(calendar.getTime());

                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                         final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                         final String push_id = mmref.getKey();


                                                         final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


                                                         final StorageReference filepath = mStorageRef.child("Profile_posts_Images").child("" + ".jpg");
                                                         mmref.keepSynced(true);

                                                         filepath.putFile(uriimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                             @Override
                                                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                 mStorageRef.child("Profile_posts_Images").child("" + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Uri> task) {
                                                                         String download_url = task.getResult().toString();

                                                                         HashMap<String, Object> hashMap = new HashMap<>();

                                                                         hashMap.put("imageurl", download_url);
                                                                         hashMap.put("pushid", push_id);
                                                                         hashMap.put("date", savecurrentdate);
                                                                         hashMap.put("time", savecurrenttime);
                                                                         hashMap.put("description", edittext.getText().toString());
                                                                         hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                         hashMap.put("type", "image");
                                                                         hashMap.put("bitmap","");
                                                                         hashMap.put("publisher", firebaseUser.getUid());
                                                                         hashMap.put("tempdate",tempdate);
                                                                         hashMap.put("perdate",perdate);

                                                                         mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                             @Override
                                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                                 Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                                 finish();

                                                                             }
                                                                         });

                                                                     }

                                                                 });


                                                             }
                                                         });
                                                     }

                                             else if(videouri!=null && TextUtils.isEmpty(edittext.getText().toString()))
                                             {

   //                                                  Bitmap s1=videoplayingbitmap.get(0);

                                                 /*   ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                                    s1.compress(Bitmap.CompressFormat.PNG, 100, boas);
                                                    byte[] b = boas.toByteArray();
                                                    bitma2 = Base64.encodeToString(b, Base64.DEFAULT);
*/

                                                 Calendar calendar = Calendar.getInstance();
                                                     SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                     final String savecurrentdate = currentdate.format(calendar.getTime());


                                                     SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                     final String savecurrenttime = currenttime.format(calendar.getTime());


                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                     final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                     final String push_id = mmref.getKey();

                                                     final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


                                                     final StorageReference filepath = mStorageRef.child("Profile_posts_videos").child("" + ".mp4");
                                                     mmref.keepSynced(true);


                                                     filepath.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                         @Override
                                                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                             mStorageRef.child("Profile_posts_videos").child("" + ".mp4").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<Uri> task) {
                                                                     String download_url = task.getResult().toString();

                                                                     HashMap<String, Object> hashMap = new HashMap<>();

                                                                     hashMap.put("imageurl", download_url);
                                                                     hashMap.put("pushid", push_id);
                                                                     hashMap.put("date", savecurrentdate);
                                                                     hashMap.put("time", savecurrenttime);
                                                                     hashMap.put("description", edittext.getText().toString());
                                                                     hashMap.put("ago", ServerValue.TIMESTAMP);
                                                                     hashMap.put("type", "video");
                                                                     hashMap.put("bitmap","");
                                                                     hashMap.put("publisher", firebaseUser.getUid());
                                                                     hashMap.put("tempdate",tempdate);
                                                                     hashMap.put("perdate",perdate);

                                                                     mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {

                                                                             Toast.makeText(getApplicationContext(), "Image upload Successfull", Toast.LENGTH_SHORT).show();
                                                                             finish();

                                                                         }
                                                                     });
                                                                 }

                                                             });
                                                         }
                                                     });

                                                 }

                                             else if(uriimg==null && videouri==null && imagepick==null && !TextUtils.isEmpty(edittext.getText().toString())) {


                                                 r = ThreadLocalRandom.current().nextInt(10, 100);
                                                 random = Integer.toString(r);

                                                 Calendar calendar = Calendar.getInstance();
                                                 SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                                 final String savecurrentdate = currentdate.format(calendar.getTime());


                                                 SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                                 final String savecurrenttime = currenttime.format(calendar.getTime());


                                                 Date date1 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df = new SimpleDateFormat("dd MMM");
                                                 final String tempdate = df.format(date1);


                                                 Date date2 = Calendar.getInstance().getTime();
                                                 SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                                                 final String perdate = df1.format(date2);


                                                 final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                 final DatabaseReference mmref = FirebaseDatabase.getInstance().getReference().child("posts").push();
                                                 final String push_id = mmref.getKey();

                                                 HashMap<String, Object> hashMap = new HashMap<>();

                                                 hashMap.put("imageurl", "");
                                                 hashMap.put("pushid", push_id);
                                                 hashMap.put("date", savecurrentdate);
                                                 hashMap.put("time", savecurrenttime);
                                                 hashMap.put("description", edittext.getText().toString());
                                                 hashMap.put("ago", ServerValue.TIMESTAMP);
                                                 hashMap.put("type", "text");
                                                 hashMap.put("bitmap", "");
                                                 hashMap.put("publisher", firebaseUser.getUid());
                                                 hashMap.put("tempdate",tempdate);
                                                 hashMap.put("perdate",perdate);

                                                 mmref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         Toast.makeText(AddPostActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                         finish();
                                                     }
                                                 });

                                             }

                                             else
                                              {
                                                  Toast.makeText(getApplicationContext(), "Need Post", Toast.LENGTH_SHORT).show();
                                              }


                                         }
                                     });

        removephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uriimg!=null)
                {
                    thumbnail.setImageDrawable(null);
                    addlocation.setVisibility(View.GONE);
                    tagpeople.setVisibility(View.GONE);
                    removephoto.setVisibility(View.GONE);
                }
                else if(imagepick!=null)
                {
                    thumbnail.setImageDrawable(null);
                    addlocation.setVisibility(View.GONE);
                    tagpeople.setVisibility(View.GONE);
                    removephoto.setVisibility(View.GONE);
                }
                else if(videouri!=null)
                {
                    thumbnail.setImageDrawable(null);
                    tagpeople.setVisibility(View.GONE);
                    addlocation.setVisibility(View.GONE);
                    removephoto.setVisibility(View.GONE);
                }
                else
                {
                    thumbnail.setImageDrawable(null);
                    tagpeople.setVisibility(View.GONE);
                    addlocation.setVisibility(View.GONE);
                    removephoto.setVisibility(View.GONE);
                    urlcard.setVisibility(View.GONE);
                }
            }
        });



        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(getApplicationContext(),GalleryFragment.class);
                intent1.setType("image/*");
                startActivityForResult(intent1,1);


                String action=intent1.getAction();
                String type=intent1.getType();

                if(Intent.ACTION_SEND.equals(action) && type!=null)
                {
                    if("text/plain".equals(type))
                    {
                        handlesendText(intent1);
                    }
                    else if(type.startsWith("image"))
                    {
                        handlesendImage(intent1);
                    }
                }

                /*
                final AlertDialog.Builder builder= new AlertDialog.Builder(view.getRootView().getContext());

                View view1=LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialogforfetchpost,null);


                CardView c1=view1.findViewById(R.id.c1);
                CardView c2=view1.findViewById(R.id.c2);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

                c1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!checkstoragepermission()) {
                            requeststoragepermission();
                        } else {
                            PickFromGallery();
                        }
                        dialog.dismiss();
                    }
                });



                c2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!checkstoragepermission()) {
                            requeststoragepermission();
                        }
                        else{
                            PickFromVideo();
                        }

                        dialog.dismiss();

                    }
                });

                */

            }
        });

        imageplaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),VideoPlayingActivity.class);
                String videourl= String.valueOf(videouri);
                intent.putExtra("videouri",videourl);
                startActivity(intent);
            }
        });


        setupMentionsAutocomplete();
        setupHashtagAutocomplete();
    }

    private void getLocation()
    {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void locationEnabled() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }


    private boolean isValidDomain(String url) {
        Pattern p = Patterns.DOMAIN_NAME;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }


    private void handlesendImage(Intent intent)
    {
        Uri imageuri11 =intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imageuri11!=null)
        {
            img_uri=imageuri11;
            tagpeople.setVisibility(View.VISIBLE);
            addlocation.setVisibility(View.VISIBLE);
            removephoto.setVisibility(View.VISIBLE);
            thumbnail.setImageURI(img_uri);
        }
    }


    private void handlesendText(Intent intent)
    {
        String sharedText =intent.getStringExtra(Intent.EXTRA_TEXT);
        if(sharedText!=null)
        {
            edittext.setText(sharedText);
        }
    }


    private void PickFromVideo() {


//        Intent intent=new Intent(getApplicationContext(), GalleryFragment.class);
  //      startActivityForResult(intent,1);

        /*

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent,VIDEO_PICK);
    */
    }


    private void PickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        imagepick = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagepick);
        startActivityForResult(intent, CAMERA_PICK);

    }

    private void PickFromGallery() {

  //      Intent intent=new Intent(getApplicationContext(), GalleryFragment.class);
    //    startActivityForResult(intent,1);

        /*
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");




        String action=intent.getAction();
        String type=intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type!=null)
        {
            if("text/plain".equals(type))
            {
                handlesendText(intent);
            }
            else if(type.startsWith("image"))
            {
                handlesendImage(intent);
            }
        }
    */
    }


    private boolean checkstoragepermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requeststoragepermission() {
        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST);
    }


    private boolean checkcamerapermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }


    private void requestcamerapermission() {
        ActivityCompat.requestPermissions(this, camerapermission, CAMERA_REQUEST);
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        PickFromCamera();
                    } else {
                        Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
            break;
            case STORAGE_REQUEST:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        PickFromGallery();
                        PickFromVideo();
                    } else {
                        Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
                break;
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

  /*

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentuserid = firebaseUser.getUid();

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        mref.keepSynced(true);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_PICK)
            {

                         uriimg = data.getData();


                try {
                            Bitmap bitmap00=MediaStore.Images.Media.getBitmap(getContentResolver(),uriimg);
                            imagelist1.add(bitmap00);

                            String uris=BitMapToString(bitmap00);

                            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(),uris);
                            thumbnail.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    imageplaybutton.setVisibility(View.GONE);

            }

            else if (requestCode == CAMERA_PICK) {

                 final Uri ss1 = data.getData();

                 zoomimage.setImageURI(ss1);

            }

            else if(requestCode==VIDEO_PICK)
            {

                videouri=data.getData();

                try {
                   Bitmap first = MediaStore.Images.Media.getBitmap(getContentResolver(),videouri);
                   videoplayingbitmap.add(first);

                    String uris=BitMapToString(first);

                    Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(),uris);
                    thumbnail.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageplaybutton.setVisibility(View.VISIBLE);

            }

            }
*/

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {

                String images=data.getStringExtra("uri");
                uriimg=Uri.fromFile(new File(images));
                tagpeople.setVisibility(View.VISIBLE);
                addlocation.setVisibility(View.VISIBLE);
                removephoto.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(images).into(thumbnail);

            }
        }
        if(resultCode==RESULT_OK)
        {
            if(requestCode==CAMERA_PICK)
            {
                tagpeople.setVisibility(View.VISIBLE);
                addlocation.setVisibility(View.VISIBLE);
                removephoto.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(imagepick).into(thumbnail);
            }
        }
    }

    public Bitmap getBitmapImage(PinchZoomImageView imageView){
        Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        return bm;
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

            mref.child("city").setValue(addresses.get(0).getLocality()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    mref.child("state").setValue(addresses.get(0).getAdminArea()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mref.child("country").setValue(addresses.get(0).getCountryName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mref.child("postalcode").setValue(addresses.get(0).getPostalCode()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mref.child("locality").setValue(addresses.get(0).getAddressLine(0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

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

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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


                ForegroundColorSpan grey=new ForegroundColorSpan(Color.parseColor("#1DA1F2"));
                editable.setSpan(grey,start,start+replacement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);




                return true;
            }
            public void onPopupVisibilityChanged(boolean shown) {}
        };

        mentionsAutocomplete = Autocomplete.<Details>on(edittext)
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


                ForegroundColorSpan grey=new ForegroundColorSpan(Color.parseColor("#1DA1F2"));
                editable.setSpan(grey,start,start+replacement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                return true;
            }
            public void onPopupVisibilityChanged(boolean shown) {}
        };

        HashtagAutocomplete = Autocomplete.<String>on(edittext)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
    }
}