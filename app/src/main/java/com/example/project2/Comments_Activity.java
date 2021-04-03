package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class Comments_Activity extends AppCompatActivity {

    ImageView backintent;
    RecyclerView rv;
    hani.momanii.supernova_emoji_library.Helper.EmojiconEditText chatpad,chatpad1;
    ImageView posttoall;
    String pushid,publisherid;
    CircleImageView image;
    ImageView emojibutton;
    TextView title,title1,desc;
    View rootView;
    EmojIconActions emojIcon;
    List<Comments> mcomments;

    Comment_Adapter comment_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_);

        image=findViewById(R.id.image);
        title1=findViewById(R.id.title1);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);

        emojibutton=findViewById(R.id.emojibutton);
        rootView =findViewById(R.id.root_view1);

        backintent=findViewById(R.id.backintent);
        rv=findViewById(R.id.rv);
        chatpad=findViewById(R.id.chatpad);
        chatpad1=findViewById(R.id.chatpad1);
        posttoall=findViewById(R.id.posttoall);

        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pushid=getIntent().getStringExtra("pushid");
        publisherid=getIntent().getStringExtra("publisherid");

        emojIcon = new EmojIconActions(this,rootView,chatpad,emojibutton);

        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard","open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

        emojIcon.ShowEmojIcon();
        emojIcon.setUseSystemEmoji(true);
        emojIcon.addEmojiconEditTextList(chatpad1);


        getting_publisherinfo(publisherid,pushid);

        posttoall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chatpad.getText().toString().equals(""))
                {
                    Toast.makeText(Comments_Activity.this, "Empty Comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    addcomment();
                }

            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mcomments=new ArrayList<>();
         comment_adapter=new Comment_Adapter(this,mcomments);
         rv.setAdapter(comment_adapter);
         readcomments();


    }

    private void getting_publisherinfo(String publisherid, String pushid) {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child(publisherid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String titles=dataSnapshot.child("USERNAME").getValue(String.class);
                String img=dataSnapshot.child("IMAGEURL").getValue(String.class);
                String title11=dataSnapshot.child("USERNAME_ID").getValue(String.class);

                title.setText(titles);
                title1.setText("@"+title11);
                Picasso.with(getApplicationContext()).load(img).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference mref11=FirebaseDatabase.getInstance().getReference().child("posts").child(pushid);
        mref11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String d=dataSnapshot.child("description").getValue(String.class);
                if(!TextUtils.isEmpty(d))
                {
                    desc.setText(d);
                }
                else
                {
                    desc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readcomments()
    {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("comments").child(pushid);
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mcomments.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Comments comments=snapshot.getValue(Comments.class);
                    mcomments.add(comments);
                }

                Collections.reverse(mcomments);
                comment_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addcomment()
    {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("comments").child(pushid);
        reference.keepSynced(true);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",chatpad.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());

        reference.push().setValue(hashMap);
        chatpad.setText("");

    }
}
