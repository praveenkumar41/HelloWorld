package com.example.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class PostViewer_Activity extends AppCompatActivity {

    CircleImageView profileimage,comment;
    TextView title,title1,description,likes,comments,agotime,nolikes,nocomments;
    ImageView postbutton,clickplay,backintent;
    PinchZoomImageView thumbnail,videothumbnail;
    SmallBangView b1,b3,b4;
    String flag="false";
    CircleImageView like,book,share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postviewer);

        String bitmap=getIntent().getStringExtra("bitmap");
        final String cardtype=getIntent().getStringExtra("cardtype");
        String description11=getIntent().getStringExtra("description");
        final String imageurl=getIntent().getStringExtra("imageurl");
        final String postid=getIntent().getStringExtra("postid");
        final String publisher=getIntent().getStringExtra("publisher");


        nolikes=findViewById(R.id.nolikes);
        nocomments=findViewById(R.id.nocomments);

        like=findViewById(R.id.like);
        book=findViewById(R.id.book);
        share=findViewById(R.id.share);

        comment=findViewById(R.id.comment);
        profileimage=findViewById(R.id.profileimage);
        title=findViewById(R.id.title);
        title1=findViewById(R.id.title1);
        description=findViewById(R.id.description);
        likes=findViewById(R.id.likes);
        comments=findViewById(R.id.comments);
        agotime=findViewById(R.id.agotime);
        postbutton=findViewById(R.id.postbutton);
        clickplay=findViewById(R.id.clickplay);
        thumbnail=findViewById(R.id.thumbnail);
        videothumbnail=findViewById(R.id.videothumbnail);

        b1=findViewById(R.id.b1);
        b3=findViewById(R.id.b3);

        backintent=findViewById(R.id.backintent);

        DatabaseReference mref11=FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
        mref11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String date=dataSnapshot.child("perdate").getValue(String.class);
                String time=dataSnapshot.child("time").getValue(String.class);

                agotime.setText(time+" "+"."+date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BitmapDrawable bitmapDrawable=(BitmapDrawable) thumbnail.getDrawable();

                if(bitmapDrawable!=null)
                {
                    flag="true";
                }

                Bitmap b=bitmapDrawable.getBitmap();
                String decrpt=description.getText().toString();

                Bundle bundle=new Bundle();
                bundle.putString("postid",postid);
                bundle.putString("publisherid",publisher);
                bundle.putString("uri",imageurl);
                //      bundle.putSerializable("bitmapdrawable", (Serializable) bitmapDrawable);
                bundle.putString("desc",decrpt);
                bundle.putParcelable("bit", (Parcelable) b);
                bundle.putString("flag",flag);
                SharebottomSheets bottomsheet=new SharebottomSheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(getSupportFragmentManager(),"SharebottomSheets");
            }
        });





        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Comments_Activity.class);
                intent.putExtra("pushid", postid);
                intent.putExtra("publisherid", publisher);
                startActivity(intent);
            }
        });

         nocomments.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getApplicationContext(), Comments_Activity.class);
                 intent.putExtra("pushid", postid);
                 intent.putExtra("publisherid", publisher);
                 startActivity(intent);
             }
         });

         nolikes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getApplicationContext(), LikesActivity.class);
                 intent.putExtra("pushid", postid);
                 intent.putExtra("publisherid", publisher);
                 startActivity(intent);
             }
         });


        if ("image".equals(cardtype) && !TextUtils.isEmpty(description11)) {

            description.setText(description11);

            Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(), bitmap);
            thumbnail.setImageBitmap(bitmap11);
            videothumbnail.setVisibility(View.GONE);

        } else if ("image".equals(cardtype) && TextUtils.isEmpty(description11)) {
            description.setVisibility(View.GONE);
            Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(getApplicationContext(), bitmap);
            thumbnail.setImageBitmap(bitmap11);
            videothumbnail.setVisibility(View.GONE);

        } else if ("text".equals(cardtype)) {
            thumbnail.setVisibility(View.GONE);
            videothumbnail.setVisibility(View.GONE);
            description.setText(description11);


        } else if ("video".equals(cardtype) && !(TextUtils.isEmpty(description11))) {

            Uri uri = Uri.parse(imageurl);
            clickplay.setVisibility(View.VISIBLE);
            thumbnail.setVisibility(View.GONE);

            Glide.with(getApplicationContext()).asBitmap()
                    .load(uri)
                    .into(videothumbnail);

            description.setText(description11);

        } else if ("video".equals(cardtype) && (TextUtils.isEmpty(description11))) {

            thumbnail.setVisibility(View.GONE);
            Uri uri = Uri.parse(imageurl);
            clickplay.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext()).asBitmap()
                    .load(uri)
                    .into(videothumbnail);

            description.setVisibility(View.GONE);

        }

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(publisher);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String image1 = dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String username = dataSnapshot.child("USERNAME_ID").getValue(String.class);

//                String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                title.setText(name);
                title1.setText("@"+username);
                Picasso.with(getApplicationContext()).load(image1).placeholder(R.drawable.pro).into(profileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        clickplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), VideoPlayingActivity.class);
                String videourl =imageurl;
                intent.putExtra("videouri", videourl);
                startActivity(intent);

            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("image".equals(cardtype)) {
                    Intent intent = new Intent(getApplicationContext(), ImageView_forphoto.class);
                    String imageurl11 = imageurl;
                    intent.putExtra("uri", imageurl11);
                    startActivity(intent);
                }

            }
        });

        // notifyDataSetChanged();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (like.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                    .child(firebaseUser.getUid()).setValue(true);
                            b1.likeAnimation();

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                    .child(firebaseUser.getUid()).removeValue();
                        }
                    }
                });
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b3.isSelected()) {
                            book.setImageResource(R.drawable.bookmark);
                            b3.setSelected(false);
                        } else {
                            b3.setSelected(true);
                            book.setImageResource(R.drawable.afterbook);
                            b3.likeAnimation();
                        }
                    }
                });
            }
        });

        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("postid",postid);
                bundle.putString("publisherid",publisher);
                PostbottomSheets bottomsheet=new PostbottomSheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(getSupportFragmentManager(),"PostbottomSheets");
            }
        });

        islikes(postid, like);
        nooflikes(likes, postid);
        getComments(postid, comments);



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (book.getTag().equals("save"))
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(postid).setValue("true");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(postid).removeValue();
                }
            }
        });

        ispostsaved(postid, book);

    }


    private void getComments(String pushid, final TextView comments)
    {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("comments").child(pushid);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                comments.setText(""+dataSnapshot.getChildrenCount());
                nocomments.setText(""+dataSnapshot.getChildrenCount()+" "+"Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void ispostsaved(final String pushid, final ImageView imageView)
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid());

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(pushid).exists())
                {
                    imageView.setImageResource(R.drawable.afterbook);
                    imageView.setTag("saved");
                }
                else
                {
                    imageView.setImageResource(R.drawable.bookmark);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void islikes(String postid, final ImageView imageview)
    {

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    imageview.setImageResource(R.drawable.afterlove);
                    imageview.setTag("liked");
                }
                else
                {
                    imageview.setImageResource(R.drawable.heart101);
                    imageview.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void nooflikes(final TextView likes,String postid)
    {
        DatabaseReference mref4=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

        mref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+"");
                nolikes.setText(dataSnapshot.getChildrenCount()+" "+"Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}