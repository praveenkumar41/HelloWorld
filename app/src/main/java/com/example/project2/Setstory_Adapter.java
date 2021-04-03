package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.HashMap;
import java.util.List;

import android.widget.Toast;

public class Setstory_Adapter extends RecyclerView.Adapter<Setstory_Adapter.ViewHolder> {

    private List<Stories> mstory;
    Context mcontext;
    AppCompatActivity appCompatActivity;

    String userid;
    double c1=1,c2=1,c3=1,c4=1,c5=1;
    boolean flag1=true,flag2=true,flag3=true,flag4=true,flag5=true;


    public Setstory_Adapter(Context applicationContext, List<Stories> mstory, String userids) {

        this.mcontext=applicationContext;
        this.mstory=mstory;
        this.userid=userids;

    }

    @NonNull
    @Override
    public Setstory_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setstory_item, viewGroup, false);
        return new Setstory_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        addview(mstory.get(position).getStoryid(),mstory.get(position).getUserid());


        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            holder.views.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }


        if ("text".equals(mstory.get(position).getType())) {
            holder.text.setText(mstory.get(position).getImageurl());
            holder.image.setVisibility(View.GONE);
            holder.player_view.setVisibility(View.GONE);
            holder.allseek.setVisibility(View.GONE);

        } else if ("image".equals(mstory.get(position).getType())) {
            Picasso.with(mcontext).load(mstory.get(position).getImageurl()).into(holder.image);
            holder.text.setVisibility(View.GONE);
            holder.player_view.setVisibility(View.GONE);
            holder.allseek.setVisibility(View.GONE);

        } else if ("video".equals(mstory.get(position).getType())) {
            holder.player_view.setVisibility(View.VISIBLE);

            holder.text.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.allseek.setVisibility(View.GONE);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            holder.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mcontext, trackSelector);

            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            Uri videourl = Uri.parse(mstory.get(position).getImageurl());

            String videouri = String.valueOf(videourl);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    mcontext, Util.getUserAgent(mcontext, "RecyclerView VideoPlayer"));


            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videouri));


            holder.player_view.setPlayer(holder.simpleExoPlayer);

            holder.player_view.setKeepScreenOn(true);

            holder.simpleExoPlayer.prepare(videoSource);

            holder.simpleExoPlayer.setPlayWhenReady(true);

        }
        else if("card".equals(mstory.get(position).getType()))
        {
            holder.card.setVisibility(View.VISIBLE);
            holder.allseek.setVisibility(View.GONE);

            if ("image".equals(mstory.get(position).getCardtype()) && !TextUtils.isEmpty(mstory.get(position).getDescription())) {

                holder.description.setText(mstory.get(position).getDescription());

                Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(mcontext, mstory.get(position).getBitmap());
                holder.thumbnail.setImageBitmap(bitmap11);
                holder.videothumbnail.setVisibility(View.GONE);

            } else if ("image".equals(mstory.get(position).getCardtype()) && TextUtils.isEmpty(mstory.get(position).getDescription())) {
                holder.description.setVisibility(View.GONE);
                Bitmap bitmap11 = ImageViewHelperCorner.imagefromDrawable(mcontext,mstory.get(position).getBitmap());
                holder.thumbnail.setImageBitmap(bitmap11);
                holder.videothumbnail.setVisibility(View.GONE);

            } else if ("text".equals(mstory.get(position).getCardtype())) {
                holder.thumbnail.setVisibility(View.GONE);
                holder.videothumbnail.setVisibility(View.GONE);
                holder.description.setText(mstory.get(position).getDescription());


            } else if ("video".equals(mstory.get(position).getCardtype()) && !(TextUtils.isEmpty(mstory.get(position).getDescription()))) {

                Uri uri = Uri.parse(mstory.get(position).getImageurl());
                holder.clickplay.setVisibility(View.VISIBLE);
                holder.thumbnail.setVisibility(View.GONE);

                Glide.with(mcontext).asBitmap()
                        .load(uri)
                        .into(holder.videothumbnail);

                holder.description.setText(mstory.get(position).getDescription());


            } else if ("video".equals(mstory.get(position).getCardtype()) && (TextUtils.isEmpty(mstory.get(position).getDescription()))) {

               holder.thumbnail.setVisibility(View.GONE);
                Uri uri = Uri.parse(mstory.get(position).getImageurl());
                holder.clickplay.setVisibility(View.VISIBLE);

                Glide.with(mcontext).asBitmap()
                        .load(uri)
                        .into(holder.videothumbnail);

                holder.description.setVisibility(View.GONE);
            }
        }
        else if("poll".equals(mstory.get(position).getType()))
        {
            holder.text.setText(mstory.get(position).getImageurl());
            holder.image.setVisibility(View.GONE);
            holder.player_view.setVisibility(View.GONE);

            if(mstory.get(position).getOptions_count().equals("2"))
            {
                holder.seek3.setVisibility(View.GONE);
                holder.seek4.setVisibility(View.GONE);
                holder.seek5.setVisibility(View.GONE);
                holder.seek3text.setVisibility(View.GONE);
                holder.seek4text.setVisibility(View.GONE);
                holder.seek5text.setVisibility(View.GONE);
                holder.seek3per.setVisibility(View.GONE);
                holder.seek4per.setVisibility(View.GONE);
                holder.seek5per.setVisibility(View.GONE);


                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String op1=dataSnapshot.child("0").child("cricketerName").getValue(String.class);
                        String op2=dataSnapshot.child("1").child("cricketerName").getValue(String.class);
                        String seeks1=dataSnapshot.child("seek1").getValue(String.class);
                        String seeks2=dataSnapshot.child("seek2").getValue(String.class);

                        holder.seek1text.setText(op1);
                        holder.seek2text.setText(op2);
/*
                        if(seeks1!=null && seeks2!=null)
                        {
                            int poll1=Integer.parseInt(seeks1);
                            holder.seek1.setProgress(poll1);

                            int poll2=Integer.parseInt(seeks2);
                            holder.seek2.setProgress(poll2);
                        }
                 */
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else if(mstory.get(position).getOptions_count().equals("3"))
            {
                holder.seek4.setVisibility(View.GONE);
                holder.seek5.setVisibility(View.GONE);
                holder.seek4text.setVisibility(View.GONE);
                holder.seek5text.setVisibility(View.GONE);
                holder.seek4per.setVisibility(View.GONE);
                holder.seek5per.setVisibility(View.GONE);


                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String op1=dataSnapshot.child("0").child("cricketerName").getValue(String.class);
                        String op2=dataSnapshot.child("1").child("cricketerName").getValue(String.class);
                        String op3=dataSnapshot.child("2").child("cricketerName").getValue(String.class);
                        String seeks1=dataSnapshot.child("seek1").getValue(String.class);
                        String seeks2=dataSnapshot.child("seek2").getValue(String.class);
                        String seeks3=dataSnapshot.child("seek3").getValue(String.class);

                        holder.seek1text.setText(op1);
                        holder.seek2text.setText(op2);
                        holder.seek3text.setText(op3);
/*
                        if(seeks1!=null && seeks2!=null && seeks3!=null)
                        {
                            int poll1=Integer.parseInt(seeks1);
                            holder.seek1.setProgress(poll1);

                            int poll2=Integer.parseInt(seeks2);
                            holder.seek2.setProgress(poll2);

                            int poll3=Integer.parseInt(seeks3);
                            holder.seek3.setProgress(poll3);
                        }
  */
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(mstory.get(position).getOptions_count().equals("4"))
            {
                holder.seek5.setVisibility(View.GONE);
                holder.seek5text.setVisibility(View.GONE);

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String op1=dataSnapshot.child("0").child("cricketerName").getValue(String.class);
                        String op2=dataSnapshot.child("1").child("cricketerName").getValue(String.class);
                        String op3=dataSnapshot.child("2").child("cricketerName").getValue(String.class);
                        String op4=dataSnapshot.child("3").child("cricketerName").getValue(String.class);
                        String seeks1=dataSnapshot.child("seek1").getValue(String.class);
                        String seeks2=dataSnapshot.child("seek2").getValue(String.class);
                        String seeks3=dataSnapshot.child("seek3").getValue(String.class);
                        String seeks4=dataSnapshot.child("seek4").getValue(String.class);

                        holder.seek1text.setText(op1);
                        holder.seek2text.setText(op2);
                        holder.seek3text.setText(op3);
                        holder.seek4text.setText(op4);
/*
                        if(seeks1!=null && seeks2!=null && seeks3!=null && seeks4!=null)
                        {
                            int poll1=Integer.parseInt(seeks1);
                            holder.seek1.setProgress(poll1);

                            int poll2=Integer.parseInt(seeks2);
                            holder.seek2.setProgress(poll2);

                            int poll3=Integer.parseInt(seeks3);
                            holder.seek3.setProgress(poll3);

                            int poll4=Integer.parseInt(seeks4);
                            holder.seek4.setProgress(poll4);
                        }
  */
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(mstory.get(position).getOptions_count().equals("5"))
            {

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String op1=dataSnapshot.child("0").child("cricketerName").getValue(String.class);
                        String op2=dataSnapshot.child("1").child("cricketerName").getValue(String.class);
                        String op3=dataSnapshot.child("2").child("cricketerName").getValue(String.class);
                        String op4=dataSnapshot.child("3").child("cricketerName").getValue(String.class);
                        String op5=dataSnapshot.child("4").child("cricketerName").getValue(String.class);

                        String seeks1=dataSnapshot.child("seek1").getValue(String.class);
                        String seeks2=dataSnapshot.child("seek2").getValue(String.class);
                        String seeks3=dataSnapshot.child("seek3").getValue(String.class);
                        String seeks4=dataSnapshot.child("seek4").getValue(String.class);
                        String seeks5=dataSnapshot.child("seek5").getValue(String.class);

                        holder.seek1text.setText(op1);
                        holder.seek2text.setText(op2);
                        holder.seek3text.setText(op3);
                        holder.seek4text.setText(op4);
                        holder.seek5text.setText(op5);

                        /*
                        if(seeks1!=null && seeks2!=null && seeks3!=null && seeks4!=null && seeks5!=null)
                        {
                            int poll1=Integer.parseInt(seeks1);
                            holder.seek1.setProgress(poll1);

                            int poll2=Integer.parseInt(seeks2);
                            holder.seek2.setProgress(poll2);

                            int poll3=Integer.parseInt(seeks3);
                            holder.seek3.setProgress(poll3);

                            int poll4=Integer.parseInt(seeks4);
                            holder.seek4.setProgress(poll4);

                            int poll5=Integer.parseInt(seeks5);
                            holder.seek5.setProgress(poll5);
                        }
  */
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        holder.seek1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.seek1text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag1)
                {
                    c1++;
                    c2=1;
                    c3=1;
                    c4=1;
                    c5=1;
                    flag1=false;
                    flag2=true;
                    flag3=true;
                    flag4=true;
                    flag5=true;

                    calculatepercentage(holder.seek1,holder.seek2,holder.seek3,holder.seek4,holder.seek5,holder.seek1per,holder.seek2per,holder.seek3per,holder.seek4per,holder.seek5per,position);
                }
            }
        });

        holder.seek2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.seek2text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2)
                {
                    c1=1;
                    c2++;
                    c3=1;
                    c4=1;
                    c5=1;
                    flag1=true;
                    flag2=false;
                    flag3=true;
                    flag4=true;
                    flag5=true;

                    calculatepercentage(holder.seek1,holder.seek2,holder.seek3,holder.seek4,holder.seek5,holder.seek1per,holder.seek2per,holder.seek3per,holder.seek4per,holder.seek5per, position);
                }
            }
        });



        holder.seek3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.seek3text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag3)
                {
                    c1=1;
                    c2=1;
                    c3++;
                    c4=1;
                    c5=1;

                    flag1=true;
                    flag2=true;
                    flag3=false;
                    flag4=true;
                    flag5=true;

                    calculatepercentage(holder.seek1,holder.seek2,holder.seek3,holder.seek4,holder.seek5,holder.seek1per,holder.seek2per,holder.seek3per,holder.seek4per,holder.seek5per, position);
                }
            }
        });



        holder.seek4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.seek4text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag4)
                {
                    c1=1;
                    c2=1;
                    c3=1;
                    c4++;
                    c5=1;
                    flag1=true;
                    flag2=true;
                    flag3=true;
                    flag4=false;
                    flag5=true;

                    calculatepercentage(holder.seek1,holder.seek2,holder.seek3,holder.seek4,holder.seek5,holder.seek1per,holder.seek2per,holder.seek3per,holder.seek4per,holder.seek5per, position);
                }
            }
        });



        holder.seek5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.seek5text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag5)
                {
                    c1=1;
                    c2=1;
                    c3=1;
                    c4=1;
                    c5++;
                    flag1=true;
                    flag2=true;
                    flag3=true;
                    flag4=true;
                    flag5=false;

                    calculatepercentage(holder.seek1,holder.seek2,holder.seek3,holder.seek4,holder.seek5,holder.seek1per,holder.seek2per,holder.seek3per,holder.seek4per,holder.seek5per, position);
                }
            }
        });




        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(mstory.get(position).getUserid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String image1 = dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                holder.cardtitle.setText(name1);
                Picasso.with(mcontext).load(image1).placeholder(R.drawable.pro).into(holder.cardimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext,PostViewer_Activity.class);
                intent.putExtra("bitmap",mstory.get(position).getBitmap());
                intent.putExtra("cardtype",mstory.get(position).getCardtype());
                intent.putExtra("description",mstory.get(position).getDescription());
                intent.putExtra("imageurl",mstory.get(position).getImageurl());
                intent.putExtra("postid",mstory.get(position).getPostid());
                intent.putExtra("publisher",mstory.get(position).getPublisher());
                intent.putExtra("type",mstory.get(position).getType());
                mcontext.startActivity(intent);
                

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());
                mref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(mcontext, "Story deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        holder.views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext,Story_seen_List.class);
                intent.putExtra("id",userid);
                intent.putExtra("storyid",mstory.get(position).getStoryid());
                intent.putExtra("title","views");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);

            }
        });
    }

    private void calculatepercentage(SeekBar seek1, SeekBar seek2, SeekBar seek3, SeekBar seek4, SeekBar seek5, TextView seek1per, TextView seek2per, TextView seek3per, TextView seek4per, TextView seek5per, int position) {

        double total=c1+c2+c3+c4+c5;

        double percent1=(c1/total)*100;
        double percent2=(c2/total)*100;
        double percent3=(c3/total)*100;
        double percent4=(c4/total)*100;
        double percent5=(c5/total)*100;

        seek1per.setText(String.format("%.0f%%",percent1));
        seek1.setProgress((int)percent1);
        final String s1=Integer.toString((int)percent1);

        seek2per.setText(String.format("%.0f%%",percent2));
        seek2.setProgress((int)percent2);
        final String s2=Integer.toString((int)percent2);

        seek3per.setText(String.format("%.0f%%",percent3));
        seek3.setProgress((int)percent3);
        final String s3=Integer.toString((int)percent3);

        seek4per.setText(String.format("%.0f%%",percent4));
        seek4.setProgress((int)percent4);
        final String s4=Integer.toString((int)percent4);

        seek5per.setText(String.format("%.0f%%",percent5));
        seek5.setProgress((int)percent5);
        final String s5=Integer.toString((int)percent5);


        final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story").child(mstory.get(position).getUserid()).child(mstory.get(position).getStoryid());

        mref.child("seek1").setValue(s1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mref.child("seek2").setValue(s2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mref.child("seek3").setValue(s3).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                mref.child("seek4").setValue(s4).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mref.child("seek5").setValue(s5).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    }


    private void addview(String storypushid, String userids)
    {
        FirebaseDatabase.getInstance().getReference().child("story").child(userids).child(storypushid).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }

        @Override
        public int getItemCount () {
            return mstory.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            TextView text;
            ImageView image;
            PlayerView player_view;
            SimpleExoPlayer simpleExoPlayer;
            RelativeLayout allseek;

            CircleImageView cardimage,delete,views;
            TextView cardtitle, cardtitle1, description, likes, comments, repostcount;
            PinchZoomImageView thumbnail, videothumbnail;
            ImageView clickplay;
            SeekBar seek1,seek2,seek3,seek4,seek5;
            TextView seek1text,seek2text,seek3text,seek4text,seek5text;
            TextView seek1per,seek2per,seek3per,seek4per,seek5per;

            CardView card;


            public ViewHolder(View itemView) {
                super(itemView);

                allseek=itemView.findViewById(R.id.allseek);
                text = itemView.findViewById(R.id.text);
                image = itemView.findViewById(R.id.image);
                player_view = itemView.findViewById(R.id.player_view);

                seek1=itemView.findViewById(R.id.seek1);
                seek2=itemView.findViewById(R.id.seek2);
                seek3=itemView.findViewById(R.id.seek3);
                seek4=itemView.findViewById(R.id.seek4);
                seek5=itemView.findViewById(R.id.seek5);

                seek1text=itemView.findViewById(R.id.seek1text);
                seek2text=itemView.findViewById(R.id.seek2text);
                seek3text=itemView.findViewById(R.id.seek3text);
                seek4text=itemView.findViewById(R.id.seek4text);
                seek5text=itemView.findViewById(R.id.seek5text);

                seek1per=itemView.findViewById(R.id.seek1per);
                seek2per=itemView.findViewById(R.id.seek2per);
                seek3per=itemView.findViewById(R.id.seek3per);
                seek4per=itemView.findViewById(R.id.seek4per);
                seek5per=itemView.findViewById(R.id.seek5per);

                card = itemView.findViewById(R.id.card);

                cardimage = itemView.findViewById(R.id.cardimage);
                cardtitle =itemView.findViewById(R.id.cardtitle);
                cardtitle1 = itemView.findViewById(R.id.cardtitle1);
                description = itemView.findViewById(R.id.description);
                likes = itemView.findViewById(R.id.likes);
                comments = itemView.findViewById(R.id.comments);
                thumbnail = itemView.findViewById(R.id.thumbnail);
                videothumbnail =itemView.findViewById(R.id.videothumbnail);
                clickplay = itemView.findViewById(R.id.clickplay);

                delete=itemView.findViewById(R.id.delete);
                views=itemView.findViewById(R.id.views);
            }
        }

    }