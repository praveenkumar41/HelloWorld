package com.example.project2;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.example.project2.Notification.Client;
import com.example.project2.Notification.Data;
import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification.Sender;
import com.example.project2.Notification.Token;
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

public class FriendsChatpad extends AppCompatActivity {
    CircleImageView onlineimage;
    TextView onlinename, lastseen,groupdate;
    DatabaseReference seendatabase, mref, chatref, chatref1, datadetails, datadetails5, mref6, mref7, reference, reference77;
    FirebaseUser firebaseUser;
    EditText chatpad;
    ImageButton camera, sendbutton, addotherfiles, musicbutton;
    ImageView backintent,usersetting;
    BoomMenuButton addbutton;
    private RecyclerView rv;
    private List<Chats> mchat;
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




    String[] camerapermission;
    String[] storagepermission;



    StorageReference mStorageRef;
    DatabaseReference cloneuseridchats, mref90;
    Boolean urls;
    String thelastmessages;
    MediaPlayer mediaPlayer;

    ValueEventListener seenlistener;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    ArrayList<Uri> MusicList = new ArrayList<Uri>();

    APIService apiService;
    boolean notify=false;

    Bitmap bitmap;
    int itempos=0;
    Activity activity;


    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenlistener);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendschatpad);



        camerapermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        final String user_id = getIntent().getStringExtra("userid");
        userid = user_id;
        receiverid = user_id;



        mStorageRef = FirebaseStorage.getInstance().getReference();


       // musicbutton = findViewById(R.id.musicbutton);
        backintent = findViewById(R.id.backintent);

        usersetting=findViewById(R.id.usersetting);

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


       // groupdate=findViewById(R.id.groupdate);

        usersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Usermessage_settings.class);
                intent.putExtra("userid",user_id);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });


        //    seenmessages1(user_id);
        onlineimage = findViewById(R.id.onlineimage);
        onlinename = findViewById(R.id.onlinename);
        lastseen = findViewById(R.id.lastseen);
        addbutton = findViewById(R.id.addbutton);
        sendbutton = findViewById(R.id.sendbutton);

        chatpad = findViewById(R.id.chatpad);


        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mchat = new ArrayList<>();


        chatpadAdapter = new ChatpadAdapter(getApplicationContext(), mchat, user_id);
        rv.setAdapter(chatpadAdapter);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        currentuserid = firebaseUser.getUid();

//        musicbutton.setVisibility(View.INVISIBLE);

        mref = FirebaseDatabase.getInstance().getReference("users").child(userid);
        mref.keepSynced(true);

        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(getApplicationContext(), Startapp.class);
               // intent.putExtra("userid", userid);
               // startActivity(intent);
                finish();
            }
        });


        mref6 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(firebaseUser.getUid()).child(userid);
        mref7 = FirebaseDatabase.getInstance().getReference().child("Typingstatus").child(userid).child(firebaseUser.getUid());
        mref6.keepSynced(true);
        mref7.keepSynced(true);

        datadetails = FirebaseDatabase.getInstance().getReference();
        datadetails.keepSynced(true);

        loadchats();

        cloneuseridchats = FirebaseDatabase.getInstance().getReference();
        cloneuseridchats.keepSynced(true);

        chatref = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatref1 = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatref.keepSynced(true);
        chatref1.keepSynced(true);


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


        mref7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String typingstatus = dataSnapshot.child("usertyping").getValue(String.class);


                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("USERNAME").getValue(String.class);
                        final String image = dataSnapshot.child("IMAGEURL").getValue(String.class);

                        String online = dataSnapshot.child("online").getValue().toString();

                        if(online!=null)
                        {
                            lastseen.setTextColor(Color.WHITE);
                            if ("true".equals(typingstatus))
                            {
                                lastseen.setText("Typing...");
                            } else if (online.equals("true"))
                            {
                                lastseen.setText("online");
                            } else {
                                Timeago timeago = new Timeago();

                                long lasttime = Long.parseLong(online);

                                String Lastseentime = Timeago.Timeago(lasttime, getApplicationContext());
                                lastseen.setText(Lastseentime);
                            }
                        }

                        String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                        onlinename.setText(name1);
                        onlinename.setTextColor(Color.WHITE);

                    //    Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.pro).into(onlineimage);


                        Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(onlineimage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.pro).into(onlineimage);

                            }
                        });


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


        chatpad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    final HashMap typingstatus = new HashMap();
                    typingstatus.put("usertyping", "false");
                    mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });


                } else {
                    final HashMap typingstatus = new HashMap();
                    typingstatus.put("usertyping", "true");
                    mref6.setValue(typingstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

/*
        chatref.child("Chatting").child(currentuserid).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(userid)) {
                    Map chatAddmap = new HashMap();
                    chatAddmap.put("seen", false);
                    chatAddmap.put("time", ServerValue.TIMESTAMP);

                    Map chatUsermap = new HashMap();
                    chatUsermap.put("Chatting/" + currentuserid + "/" + userid, chatAddmap);
                    chatUsermap.put("Chatting/" + userid + "/" + currentuserid, chatAddmap);

                    chatref.updateChildren(chatUsermap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notify=true;
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


                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                        String savecurrenttime = currenttime.format(calendar.getTime());

                        //String current_user_ref = "Chats/";
                       // String current_user_ref = "Chats/" + currentuserid + "/" + userid;
                       // String chat_user_ref = "Chats/" + userid + "/" + currentuserid;

                        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();

                        String pushid = mref.getKey();
                        pushedid = pushid;

//                    HashMap chatmap = new HashMap();
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", message);
                        chatmap.put("pushid", pushid);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver", userid);
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
                                Toast.makeText(FriendsChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });



/*
                        //  HashMap chatusermap = new HashMap();
                        HashMap<String, Object> chatusermap = new HashMap();
                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                        //user_message_push.setValue(chatusermap);

                        chatpad.setText("");

                        chatref.keepSynced(true);
                        cloneuseridchats.keepSynced(true);

                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });

                        //------------------------------------------------------------------------------------------------------------------------------------
                        //Clone for chats---------------------------------------------------------------------------------------------------------------------
/*
                        current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                        chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                        HashMap<String, Object> chatusermap1 = new HashMap();
                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------------------------------------------------------
*/
                    } else if (matcher.find()) {


                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                        String savecurrentdate = currentdate.format(calendar.getTime());


                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                        String savecurrenttime = currenttime.format(calendar.getTime());

                        //String current_user_ref = "Chats/";
                    //    String current_user_ref = "Chats/" + currentuserid + "/" + userid;
                      //  String chat_user_ref = "Chats/" + userid + "/" + currentuserid;

                        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();

                        String pushid = mref.getKey();
                        pushedid = pushid;

//                    HashMap chatmap = new HashMap();
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", message);
                        chatmap.put("pushid", pushid);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver", userid);
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
                                Toast.makeText(FriendsChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });



/*
                        //  HashMap chatusermap = new HashMap();
                        HashMap<String, Object> chatusermap = new HashMap();
                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                        //user_message_push.setValue(chatusermap);

                        cloneuseridchats.keepSynced(true);

                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
/*
                        //------------------------------------------------------------------------------------------------------------------------------------
                        //Clone for chats---------------------------------------------------------------------------------------------------------------------

                        current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                        chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                        HashMap<String, Object> chatusermap1 = new HashMap();
                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------------------------------------------------------

*/
                    } else if (!matcher.find()) {


                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                        String savecurrentdate = currentdate.format(calendar.getTime());


                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                        String savecurrenttime = currenttime.format(calendar.getTime());

                        //String current_user_ref = "Chats/";
                      //  String current_user_ref = "Chats/" + currentuserid + "/" + userid;
                      //  String chat_user_ref = "Chats/" + userid + "/" + currentuserid;

                        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();

                        String pushid = mref.getKey();
                        pushedid = pushid;

//                    HashMap chatmap = new HashMap();
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", message);
                        chatmap.put("pushid", pushid);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver", userid);
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
                                Toast.makeText(FriendsChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });



/*
                        //  HashMap chatusermap = new HashMap();
                        HashMap<String, Object> chatusermap = new HashMap();
                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                        //user_message_push.setValue(chatusermap);

                        chatpad.setText("");

                        chatref.keepSynced(true);
                        cloneuseridchats.keepSynced(true);

                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });

                        //------------------------------------------------------------------------------------------------------------------------------------
                        //Clone for chats---------------------------------------------------------------------------------------------------------------------
/*
                        current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                        chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                        HashMap<String, Object> chatusermap1 = new HashMap();
                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------------------------------------------------------

*/
                    }
                }




                DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child(receiverid);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       final String online=dataSnapshot.child("online").getValue().toString();


                        final String msg=message;
                        reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Details details=dataSnapshot.getValue(Details.class);
                                if(notify)
                                {
                                    if(!"true".equals(online))
                                    {
                                        sendNotification(receiverid,details.getUSERNAME(),msg);
                                    }

                                }
                                notify=false;
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



            }
        });
        String currentuser = firebaseUser.getUid();


        seenmessages(user_id);

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

    private void sendNotification(String reciever, final String username, final String message)
    {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        tokens.keepSynced(true);
        Query query=tokens.orderByKey().equalTo(reciever);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,""+message,"New message from"+" "+username,userid);

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new retrofit2.Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(FriendsChatpad.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void dateforgroupchat()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String[] date = new String[1];
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.keepSynced(true);
        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    assert chats != null;
                    date[0]=chats.getDate();
                }


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                String savecurrentdate = currentdate.format(calendar.getTime());




                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentdate1 = new SimpleDateFormat("dd");
                SimpleDateFormat currentmonth = new SimpleDateFormat("MMM");
                SimpleDateFormat currentyear = new SimpleDateFormat("YYYY");


                String savecurrentdate1 = currentdate1.format(calendar.getTime());
                String savecurrentdate2 = currentmonth.format(calendar.getTime());
                String savecurrentdate3 = currentyear.format(calendar.getTime());

                int s=Integer.parseInt(savecurrentdate1);

                s=s-1;
                String str = Integer.toString(s);


                String s2=savecurrentdate2+" "+str+","+" "+savecurrentdate3;

                if(savecurrentdate.equals(date[0])){
                    groupdate.setText("Today");
                }
                else if(s2.equals(date[0]))
                {
                     groupdate.setText("Yesterday");
                }
                else{
                    groupdate.setText(date[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    private void seenmessages(final String userid) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.keepSynced(true);
        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);

                    if (firebaseUser.getUid().equals(chats.getReceiverid()) && (userid.equals(chats.getSender()))) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen", true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


/*


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if(requestCode==GALLERY_PICK) {
               // assert data != null;
               // if (data.getClipData() != null) {
                  //  int count = data.getClipData().getItemCount();

                    int i = 0;
                    final Uri uriProfileImage = data.getData();

                  //  while (i < count) {
                  //      Uri uriProfileImage = data.getClipData().getItemAt(i).getUri();

                    //    ImageList.add(uriProfileImage);
                      //  i++;
                  //  }
                 //   Toast.makeText(getApplicationContext(), "ITEMS" + i, Toast.LENGTH_SHORT).show();
               //     for (int j = 0; j < ImageList.size(); j++) {
                 //       final Uri perImage = ImageList.get(j);

                        //  final String current_user_ref = "Chats/";
                        final String current_user_ref = "Chats/" + currentuserid + "/" + userid;
                        final String chat_user_ref = "Chats/" + userid + "/" + currentuserid;

                        final DatabaseReference user_message_push = chatref.child("Chats").child(currentuserid).child(userid).push();

                        //final DatabaseReference user_message_push = chatref.child("Chats").push();

                        final String pushid = user_message_push.getKey();


                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                        final String savecurrentdate = currentdate.format(calendar.getTime());


                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                        final String savecurrenttime = currenttime.format(calendar.getTime());


                        final String currentuserid = firebaseUser.getUid();

                        final StorageReference filepath = mStorageRef.child("Chats_Images" +uriProfileImage.getLastPathSegment()).child(pushid + ".jpg");

                        filepath.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Task<Uri> download_url =taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                //String downloadurll=download_url.toString();

                                mStorageRef.child("Chats_Images" + uriProfileImage.getLastPathSegment()).child(pushid + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String download_url = task.getResult().toString();

//                                    HashMap chatmap = new HashMap();
                                        HashMap<String, Object> chatmap = new HashMap<>();
                                        chatmap.put("message", download_url);
                                        chatmap.put("pushid", pushid);
                                        chatmap.put("sender", firebaseUser.getUid());
                                        chatmap.put("receiver", userid);
                                        chatmap.put("seen", false);
                                        chatmap.put("time", savecurrenttime);
                                        chatmap.put("type", "image");
                                        chatmap.put("receiverid", receiverid);
                                        chatmap.put("seentext", false);
                                        chatmap.put("date", savecurrentdate);

                                        //                HashMap chatusermap = new HashMap();
                                        HashMap<String, Object> chatusermap = new HashMap();
                                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                                        //  user_message_push.setValue(chatusermap);

                                        Toast.makeText(getApplicationContext(), "Image upload Successful", Toast.LENGTH_SHORT).show();


                                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                                }
                                            }
                                        });


                                        String current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                                        String chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                                        HashMap<String, Object> chatusermap1 = new HashMap();
                                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                                }
                                            }
                                        });
                                        //---------------------------------------------------------------------------------------------------------------------------------------
                                        //---------------------------------------------------------------------------------------------------------------------------------------


                                    }
                                });

                            }

                        });

                    }

                   if (requestCode == MUSIC_PICK) {
                        assert data != null;
                        if (data.getClipData() != null) {
                            Uri uriProfilemusic = data.getData();
                            upload1(uriProfilemusic);

                        }
                  }



          }
            }

*/

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

                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();

                    String pushid = mref.getKey();

                    final String currentuserid = firebaseUser.getUid();

                    final StorageReference filepath = mStorageRef.child("Chats_Images" + ss.getLastPathSegment()).child(pushid + ".jpg");
                    uploadimage(filepath, ss, pushid,bitmap,mref);

                }
            } else if (requestCode == MUSIC_PICK) {
                if ((data != null) && (data.getData() != null)) {

                    final Uri ss1 = data.getData();

                    DatabaseReference mref11=FirebaseDatabase.getInstance().getReference("Chats").push();

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

                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();

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

        //  final String current_user_ref = "Chats/";
   //     final String current_user_ref = "Chats/" + currentuserid + "/" + userid;
     //   final String chat_user_ref = "Chats/" + userid + "/" + currentuserid;


        final Cursor returncursor=getContentResolver().query(ss1,null,null,null,null);
        final int nameindex=returncursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returncursor.moveToFirst();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
        final String savecurrentdate = currentdate.format(calendar.getTime());


        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("Chats").push();


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

//                                    HashMap chatmap = new HashMap();
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", download_url);
                        chatmap.put("pushid", pushid);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver", userid);
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
                                Toast.makeText(FriendsChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });


                        /*
                        //                HashMap chatusermap = new HashMap();
                        HashMap<String, Object> chatusermap = new HashMap();
                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                        //  user_message_push.setValue(chatusermap);

                        Toast.makeText(getApplicationContext(), "Song upload Successful", Toast.LENGTH_SHORT).show();


                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
/*

                        String current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                        String chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                        HashMap<String, Object> chatusermap1 = new HashMap();
                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------------------------------------------------------
*/
                    }
                });

            }

        });

    }


    private void uploadimage(StorageReference filepath, final Uri ss, final String pushid, final Bitmap bitmap, final DatabaseReference mref)
    {

        //  final String current_user_ref = "Chats/";
  //      final String current_user_ref = "Chats/" + currentuserid + "/" + userid;
    //    final String chat_user_ref = "Chats/" + userid + "/" + currentuserid;

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


//                                    HashMap chatmap = new HashMap();
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", download_url);
                        chatmap.put("pushid", pushid);
                        chatmap.put("bitmap", bit);
                        chatmap.put("sender", firebaseUser.getUid());
                        chatmap.put("receiver", userid);
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
                                Toast.makeText(FriendsChatpad.this, "send!", Toast.LENGTH_SHORT).show();
                            }
                        });


                        /*
                        //                HashMap chatusermap = new HashMap();
                        HashMap<String, Object> chatusermap = new HashMap();
                        chatusermap.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap.put(chat_user_ref + "/" + pushid, chatmap);

                        //  user_message_push.setValue(chatusermap);

                        Toast.makeText(getApplicationContext(), "Image upload Successful", Toast.LENGTH_SHORT).show();

                        chatref.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });

/*
                        String current_user_ref = "Chatsclone/" + currentuserid + "/" + userid;
                        String chat_user_ref = "Chatsclone/" + userid + "/" + currentuserid;
                        HashMap<String, Object> chatusermap1 = new HashMap();
                        chatusermap1.put(current_user_ref + "/" + pushid, chatmap);
                        chatusermap1.put(chat_user_ref + "/" + pushid, chatmap);


                        cloneuseridchats.updateChildren(chatusermap1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------------------------------------------------------
*/

                    }
                });

            }

        });
    }

             private void loadchats ()
             {
                datadetails.child("Chats").addChildEventListener(new ChildEventListener() {
                  @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Chats chats = dataSnapshot.getValue(Chats.class);



                        mchat.add(chats);
                        chatpadAdapter.notifyDataSetChanged();

                        rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
}