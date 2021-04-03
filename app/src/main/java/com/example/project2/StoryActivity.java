package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;
import tyrantgit.explosionfield.ExplosionField;

public class StoryActivity extends AppCompatActivity/* implements StoriesProgressView.StoriesListener*/ {

    int counter=0;
    long pressTime=0L;
    long limit =500L;

    StoriesProgressView storiesProgressView;
  //  ImageView image,story_photo;
    TextView story_username,storytext;
    DatabaseReference mref;
    RelativeLayout main;

    //String userids;
    ArrayList<String> storyids;
    List<Stories> mstory;
    List<String> userid11;

    CircleIndicator3 ci;

    TextView text;

    ExoPlayer player_view;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;

    LinearLayout r_seen;
    TextView seen_number;
    ImageView story_delete;

    TextView title1;
    CircleImageView image;

    ViewPager2 vp2;
   // String storypushid,usernameid;
    String times;

   // PlayerView player_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);


       String userids = getIntent().getStringExtra("userid");
       String storypushid = getIntent().getStringExtra("storyid");
       times=getIntent().getStringExtra("timestart");


        vp2 = findViewById(R.id.vp2);
        ci = findViewById(R.id.ci);

        title1=findViewById(R.id.title1);
        image=findViewById(R.id.image);

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child(userids);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               String usernameid=dataSnapshot.child("USERNAME_ID").getValue(String.class);
               String userimg=dataSnapshot.child("IMAGEURL").getValue(String.class);

//               title1.setText("@"+usernameid);
               Picasso.with(getApplicationContext()).load(userimg).into(image);

                if(!TextUtils.isEmpty(usernameid))
                {

                  //  String text="@"+usernameid+" "+" "+times+" ";

                  //  int start=usernameid.length()+3;
                  //  int end=start+4;

                 //   SpannableString ss=new SpannableString(text);

                   // ForegroundColorSpan grey=new ForegroundColorSpan(Color.GRAY);
                   // ss.setSpan(grey,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    title1.setText("@"+usernameid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        getStories(userids);


        //    userinfo(userids);
    }

    private void getStories(final String userid)
    {
        userid11=new ArrayList<>();
        storyids=new ArrayList<>();
        mstory=new ArrayList<>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("story").child(userid);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyids.clear();
                userid11.clear();
                mstory.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Stories stories=snapshot.getValue(Stories.class);
                    long timecurrent=System.currentTimeMillis();
                    if(timecurrent>stories.getTimestart() && timecurrent<stories.getTimeend())
                    {
                        mstory.add(stories);
                        storyids.add(stories.getStoryid());
                        userid11.add(stories.getUserid());

                    }
                }
              Setstory_Adapter adapter=new Setstory_Adapter(getApplicationContext(),mstory,userid);
              vp2.setPageTransformer(new ZoomOutPageTransformer());
              vp2.setAdapter(adapter);
              ci.setViewPager(vp2);
              ci.setBackgroundResource(android.R.color.transparent);


             // vp2.setPadding(150,0,150,0);
             vp2.setClipToPadding(false);
             vp2.setClipChildren(false);
             vp2.setOffscreenPageLimit(3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

  //      seenNumber(userids);

         // addview(storypushid,userids);
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]

                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
/*
    private void userinfo(String userid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Details details=dataSnapshot.getValue(Details.class);
                assert details != null;

                Picasso.with(getApplicationContext()).load(details.getIMAGEURL()).networkPolicy(NetworkPolicy.OFFLINE).into(story_photo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(details.getIMAGEURL()).placeholder(R.drawable.pro).into(story_photo);

                    }
                });

                story_username.setText(details.getUSERNAME());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/

 /*   private void addview(String storypushid, String userids)
    {
        FirebaseDatabase.getInstance().getReference().child("story").child(userids).child(storypushid).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }


/*
    private void seenNumber(String storyid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("story").child(userids).child(storyid).child("views");
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount()==0)
                {
                    seen_number.setText(""+dataSnapshot.getChildrenCount());
                }
                else{
                    int count= (int) (dataSnapshot.getChildrenCount()-1);
//                    seen_number.setText(""+count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


  */
}