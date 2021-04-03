package com.example.project2.GroupChat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project2.APIService;
import com.example.project2.ChatpadAdapter;
import com.example.project2.Chats;
import com.example.project2.Details;
import com.example.project2.FriendsChatpad;
import com.example.project2.Notification.Client;
import com.example.project2.R;
import com.example.project2.Usermessage_settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class GroupChatpad extends AppCompatActivity {



    CircleImageView onlineimage;
    TextView onlinename, lastseen, groupdate;
    FirebaseUser firebaseUser;
    EditText chatpad;
    ImageButton camera, sendbutton, addotherfiles, musicbutton;
    ImageView backintent, usersetting;
    BoomMenuButton addbutton;
    private RecyclerView rv;
    private ChatpadAdapter chatpadAdapter;


    String currentuserid;
    String pushedid;
    String userid;
    String receiverid;
    String typing;
    Integer[] image = {R.drawable.iconsmusic1, R.drawable.iconsdocuments, R.drawable.galleryphotos};
    String[] button = {"Music", "Camera", "Gallery"};
    private static final int GALLERY_PICK = 1;
    private static final int MUSIC_PICK = 101;
    private static final int CAMERA_PICK = 102;

    private static final int STORAGE_REQUEST = 10;
    private static final int CAMERA_REQUEST = 11;

    Uri imagepick;
    List<Chats> mgroupchats;

    String[] camerapermission;
    String[] storagepermission;


    StorageReference mStorageRef;
    DatabaseReference cloneuseridchats, mref90;
    Boolean urls;
    String thelastmessages;

    APIService apiService;
    boolean notify = false;

    Bitmap bitmap;
    int itempos = 0;
    String groupid;
    GroupChatpadAdapter groupChatpadAdapter;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchatpad);

        groupid = getIntent().getStringExtra("groupid");

        camerapermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mStorageRef = FirebaseStorage.getInstance().getReference();
        backintent = findViewById(R.id.backintent);

        usersetting = findViewById(R.id.usersetting);

        usersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Usermessage_settings.class);
                intent.putExtra("groupid", groupid);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });


        onlineimage = findViewById(R.id.onlineimage);
        onlinename = findViewById(R.id.onlinename);
        lastseen = findViewById(R.id.lastseen);
        addbutton = findViewById(R.id.addbutton);
        sendbutton = findViewById(R.id.sendbutton);

        chatpad = findViewById(R.id.chatpad);


        addbutton.setNormalColor(Color.parseColor("#FF03A9F4"));
        addbutton.setShadowEffect(false);

        addbutton.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        addbutton.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);

        addbutton.setButtonEnum(ButtonEnum.TextOutsideCircle);


        for (int i = 0; i < addbutton.getPiecePlaceEnum().pieceNumber(); i++) {
            int position = i;
            if (i == 0) {
                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.galleryphotos).normalText("Gallery").listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
                        // Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   /*     Intent galleryintent = new Intent();
                        galleryintent.setType("image/*");
                        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryintent, "Select Image"), GALLERY_PICK);
*/

                        if(!checkstoragepermission())
                        {
                            requeststoragepermission();
                        }
                        else
                        {
                            PickFromGallery();
                        }


                    }
                });
                addbutton.addBuilder(builder);
            } else if (i == 1) {

                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.camera_).normalText("Camera").listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        if(!checkcamerapermission())
                        {
                            requestcamerapermission();
                        }
                        else
                        {
                            PickFromCamera();
                        }

                    }
                });
                addbutton.addBuilder(builder);
            } else {
                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.iconsmusic1).normalText("Music").listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Toast.makeText(getApplicationContext(), "333", Toast.LENGTH_SHORT).show();
                        Intent musicintent = new Intent();
                        musicintent.setType("audio/*");
                        musicintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(musicintent, "Select Audio"), MUSIC_PICK);

                    }
                });

                addbutton.addBuilder(builder);
            }
        }




        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadgroup(groupid);
        loadchats(groupid);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmessage(groupid);
            }
        });
    }

    private void loadchats(String groupid)
    {
        mgroupchats=new ArrayList<>();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("GroupChats").child(groupid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mgroupchats.clear();

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Chats chats=ds.getValue(Chats.class);
                    mgroupchats.add(chats);
                }
                groupChatpadAdapter=new GroupChatpadAdapter(getApplicationContext(),mgroupchats);
                rv.setAdapter(groupChatpadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendmessage(String groupid) {

        final String message = chatpad.getText().toString();

        if (!TextUtils.isEmpty(message)) {

            urls = URLUtil.isValidUrl(message) && Patterns.DOMAIN_NAME.matcher(message).matches() || Patterns.PHONE.matcher(message).matches() || Patterns.WEB_URL.matcher(message).matches() || Patterns.IP_ADDRESS.matcher(message).matches();

            final String emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

            Pattern pattern = Pattern.compile(emailRegEx);
            Matcher matcher = pattern.matcher(message);
            if (urls) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                String savecurrentdate = currentdate.format(calendar.getTime());


                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                String savecurrenttime = currenttime.format(calendar.getTime());

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                String pushid = mref.getKey();
                pushedid = pushid;

//                    HashMap chatmap = new HashMap();
                HashMap<String, Object> chatmap = new HashMap<>();
                chatmap.put("message", message);
                chatmap.put("groupid", groupid);
                chatmap.put("pushid", pushid);
                chatmap.put("sender", firebaseUser.getUid());
                chatmap.put("receiver", groupid);
                chatmap.put("seen", false);
                chatmap.put("time", savecurrenttime);
                chatmap.put("type", "url");
                chatmap.put("receiverid", receiverid);
                chatmap.put("date", savecurrentdate);
                chatmap.put("unreadtoggleon", false);

                chatpad.setText("");
                mref.keepSynced(true);

                mref.setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GroupChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (matcher.find()) {


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                String savecurrentdate = currentdate.format(calendar.getTime());


                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                String savecurrenttime = currenttime.format(calendar.getTime());
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                String pushid = mref.getKey();
                pushedid = pushid;

                HashMap<String, Object> chatmap = new HashMap<>();
                chatmap.put("message", message);
                chatmap.put("pushid", pushid);
                chatmap.put("groupid", groupid);
                chatmap.put("sender", firebaseUser.getUid());
                chatmap.put("receiver", groupid);
                chatmap.put("seen", false);
                chatmap.put("time", savecurrenttime);
                chatmap.put("type", "link");
                chatmap.put("receiverid", receiverid);
                chatmap.put("date", savecurrentdate);

                chatpad.setText("");
                mref.keepSynced(true);

                mref.setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GroupChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (!matcher.find()) {


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                String savecurrentdate = currentdate.format(calendar.getTime());

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                String savecurrenttime = currenttime.format(calendar.getTime());

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                String pushid = mref.getKey();
                pushedid = pushid;

//                    HashMap chatmap = new HashMap();
                HashMap<String, Object> chatmap = new HashMap<>();
                chatmap.put("message", message);
                chatmap.put("pushid", pushid);
                chatmap.put("groupid", groupid);
                chatmap.put("sender", firebaseUser.getUid());
                chatmap.put("receiver", groupid);
                chatmap.put("seen", false);
                chatmap.put("time", savecurrenttime);
                chatmap.put("type", "text");
                chatmap.put("receiverid", receiverid);
                chatmap.put("date", savecurrentdate);


                chatpad.setText("");
                mref.keepSynced(true);

                mref.setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GroupChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void loadgroup(String groupid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Groups");
        ref.child(groupid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                    String groupname=ds.child("grouptitle").getValue(String.class);
                    String groupimage=ds.child("groupimage").getValue(String.class);
                    String groupid=ds.child("groupid").getValue(String.class);

                    onlinename.setText(groupname);
                    Picasso.with(getApplicationContext()).load(groupimage).into(onlineimage);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private boolean checkstoragepermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requeststoragepermission()
    {
        ActivityCompat.requestPermissions(this,storagepermission,STORAGE_REQUEST);
    }


    private boolean checkcamerapermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }


    private void requestcamerapermission()
    {
        ActivityCompat.requestPermissions(this,camerapermission,CAMERA_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
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
                if(grantResults.length>0)
                {
                    boolean storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted)
                    {
                        PickFromGallery();
                    }
                    else
                    {
                        Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

                }
                break;
        }
    }


    private void PickFromCamera()
    {
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        imagepick=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagepick);
        startActivityForResult(intent,CAMERA_PICK);
    }

    private void PickFromGallery()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_PICK) {
                if ((data != null) && (data.getData() != null)) {
                    final Uri ss = data.getData();

                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),ss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                    firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                    String pushid = mref.getKey();

                    final String currentuserid = firebaseUser.getUid();

                    final StorageReference filepath = mStorageRef.child("Chats_Images" + ss.getLastPathSegment()).child(pushid + ".jpg");
                    uploadimage(filepath, ss, pushid,bitmap,mref);

                }
            } else if (requestCode == MUSIC_PICK) {
                if ((data != null) && (data.getData() != null)) {

                    final Uri ss1 = data.getData();

                    DatabaseReference mref11=FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                    String pushid = mref11.getKey();
                    final String currentuserid = firebaseUser.getUid();

                    final StorageReference filepath = mStorageRef.child("Chats_Songs" + ss1.getLastPathSegment()).child(pushid + ".mp3");
                    uploadmusic(filepath, ss1, pushid,mref11);

                }

            }
            else if(requestCode==CAMERA_PICK)
            {
                if ((data != null) && (data.getData() != null)) {

                    final Uri ss1 = data.getData();

                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();

                    String pushid = mref.getKey();
                    pushedid = pushid;
                    final String currentuserid = firebaseUser.getUid();

                    final StorageReference filepath = mStorageRef.child("Chats_Songs" + ss1.getLastPathSegment()).child(pushid + ".mp3");
                    uploadmusic(filepath, ss1, pushid, mref);

                }

            }
        }

    }

    private void uploadmusic(StorageReference filepath, final Uri ss1, final String pushid, final DatabaseReference mref11)
    {


        final Cursor returncursor=getContentResolver().query(ss1,null,null,null,null);
        final int nameindex=returncursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returncursor.moveToFirst();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
        final String savecurrentdate = currentdate.format(calendar.getTime());


        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("GroupChats").child(groupid).push();


        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
        final String savecurrenttime = currenttime.format(calendar.getTime());

        filepath.putFile(ss1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Task<Uri> download_url =taskSnapshot.getMetadata().getReference().getDownloadUrl();
                //String downloadurll=download_url.toString();

                mStorageRef.child("Chats_songs" +ss1.getLastPathSegment()).child(returncursor.getString(nameindex) + ".mp3").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String download_url = task.getResult().toString();

                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", download_url);
                        chatmap.put("pushid", pushid);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver",groupid);
                        chatmap.put("seen", false);
                        chatmap.put("time", savecurrenttime);
                        chatmap.put("type", "audio");
                        chatmap.put("receiverid", receiverid);
                        chatmap.put("date", savecurrentdate);
                        chatmap.put("songname",returncursor.getString(nameindex));


                        mref11.keepSynced(true);

                        mref11.setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(GroupChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }

        });

    }


    private void uploadimage(StorageReference filepath, final Uri ss, final String pushid, final Bitmap bitmap, final DatabaseReference mref)
    {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
        final String savecurrentdate = currentdate.format(calendar.getTime());


        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
        final String savecurrenttime = currenttime.format(calendar.getTime());

        filepath.putFile(ss).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Task<Uri> download_url =taskSnapshot.getMetadata().getReference().getDownloadUrl();
                //String downloadurll=download_url.toString();

                mStorageRef.child("Chats_Images" + ss.getLastPathSegment()).child(pushid + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String download_url = task.getResult().toString();

                        String bit=BitMapToString(bitmap);

                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", download_url);
                        chatmap.put("pushid", pushid);
                        chatmap.put("bitmap", bit);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver",groupid);
                        chatmap.put("seen", false);
                        chatmap.put("time", savecurrenttime);
                        chatmap.put("type", "image");
                        chatmap.put("receiverid", receiverid);
                        chatmap.put("date", savecurrentdate);

                        chatpad.setText("");
                        mref.keepSynced(true);

                        mref.setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(GroupChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

        });
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}