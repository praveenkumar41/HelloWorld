package com.example.project2.videostream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.project2.Comments_Activity;
import com.example.project2.ImageViewHelperCorner;
import com.example.project2.ImageView_forphoto;
import com.example.project2.Othersusersbottomsheets;
import com.example.project2.Otheruserswithmusicprofile;
import com.example.project2.PinchZoomImageView;
import com.example.project2.PostViewer_Activity;
import com.example.project2.Post_Timeago;
import com.example.project2.PostbottomSheets;
import com.example.project2.R;
import com.example.project2.SharebottomSheets;
import com.example.project2.Timeago;
import com.example.project2.VideoPlayingActivity;
import com.example.project2.repost_bottomsheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

import static androidx.media2.MediaUtils.TAG;

public class ImagedisplayAdapter extends RecyclerView.Adapter<ImagedisplayAdapter.ViewHolder> {

    private ArrayList<MediaObject1> mediaObjects1;
    private RequestManager requestManager;
    Context mcontext;
    Bitmap bitmap;
    private static final int IMAGE_POST= 0;
    private static final int VIDEO_POST = 1;
    private static final int TEXT_POST= 2;
    List<String> gettinglikedby;

    String savedpushid[]=new String[10];
    String Lastseentime;
    String flag="false";
    String ccount;

    public ImagedisplayAdapter(Context context, ArrayList<MediaObject1> mediaObjects1) {
        this.mcontext=context;
        this.mediaObjects1 = mediaObjects1;
    }

    @NonNull
    @Override
    public ImagedisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        return new ImagedisplayAdapter.ViewHolder(view);
    }

    
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final MediaObject1 mediaObject = mediaObjects1.get(position);

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, Comments_Activity.class);
                intent.putExtra("pushid", mediaObject.getPushid());
                intent.putExtra("publisherid", mediaObject.getPublisher());
                mcontext.startActivity(intent);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext, Otheruserswithmusicprofile.class);
                intent.putExtra("userid", mediaObject.getPublisher());
                mcontext.startActivity(intent);
            }
        });


        holder.title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext, Otheruserswithmusicprofile.class);
                intent.putExtra("userid", mediaObject.getPublisher());
                mcontext.startActivity(intent);
            }
        });


        if ("image".equals(mediaObject.getType()) && !TextUtils.isEmpty(mediaObject.getDescription())) {

            holder.description.setLinkText(mediaObject.getDescription());

            holder.description.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
                @Override
                public void onLinkClicked(int i, final String s) {
                    String MobilePattern = "[0-9]{10}";
                    if(s.startsWith("#"))
                    {
                        Toast.makeText(mcontext, s, Toast.LENGTH_SHORT).show();
                    }
                    if(s.startsWith("@"))
                    {
                        Toast.makeText(mcontext, "@@@", Toast.LENGTH_SHORT).show();
                        final String[] getid = {""};
                        final String s1=s.substring(1,s.length()-0);
                        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users");
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {
                                    if(snapshot.child("USERNAME_ID").getValue().equals(s1))
                                    {
                                        getid[0] =snapshot.child("ID").getValue(String.class);
                                        Log.d(TAG, "onDataChange: "+getid[0]);
                                    }
                                }

                                Intent intent=new Intent(mcontext,Otheruserswithmusicprofile.class);
                                intent.putExtra("userid",getid[0]);
                                mcontext.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(mcontext, "@@@@", Toast.LENGTH_SHORT).show();

                    }
                    if(s.startsWith("https://") || (s.endsWith(".com") ||(s.startsWith("www"))))
                    {
                        Uri webpage = Uri.parse(s);
                        if (!s.startsWith("http://") && !s.startsWith("https://")) {
                            webpage = Uri.parse("http://" + s);
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        mcontext.startActivity(intent);
                    }
                    if(s.matches(MobilePattern))
                    {
                        Uri u = Uri.parse("tel:" + s);
                        Intent intent = new Intent(Intent.ACTION_DIAL, u);
                        try
                        {
                            mcontext.startActivity(intent);
                        }
                        catch (SecurityException s1)
                        {
                            Toast.makeText(mcontext, s1.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if(s.contains("@gmail.com"))
                    {
                        Toast.makeText(mcontext, "email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("email"));
                        String s11[] = {s};
                        intent.putExtra(Intent.EXTRA_EMAIL, s11);
                        intent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(intent, "Launch Email");
                        mcontext.startActivity(chooser);
                    }
                }
            });

            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(mcontext, mediaObject.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);
            holder.videothumbnail.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject.getAgo();

            Lastseentime =timeago.Post_Timeago(lasttime, mcontext);

            holder.agotime.setText(Lastseentime);

        } else if ("image".equals(mediaObject.getType()) && TextUtils.isEmpty(mediaObject.getDescription())) {

            holder.description.setVisibility(View.GONE);

            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(mcontext, mediaObject.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);

            holder.videothumbnail.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject.getAgo();

            Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);


        } else if ("text".equals(mediaObject.getType())) {
            holder.thumbnail.setVisibility(View.GONE);
            holder.videothumbnail.setVisibility(View.GONE);

          //  SocialTextView textView = new SocialTextView(mcontext);
          //  textView.setLinkText(example);

            holder.description.setLinkText(mediaObject.getDescription());

            holder.description.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
                @Override
                public void onLinkClicked(int i, String s) {
                    String MobilePattern = "[0-9]{10}";
                    if(s.startsWith("#"))
                    {
                        Toast.makeText(mcontext, s, Toast.LENGTH_SHORT).show();
                    }
                    else if(s.startsWith("@"))
                    {

                    }
                    else if(s.startsWith("https://") || (s.endsWith(".com") ||(s.startsWith("www"))))
                    {
                        Uri webpage = Uri.parse(s);
                        if (!s.startsWith("http://") && !s.startsWith("https://")) {
                            webpage = Uri.parse("http://" + s);
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        mcontext.startActivity(intent);
                    }
                    else if(s.matches(MobilePattern))
                    {
                        Uri u = Uri.parse("tel:" + s);
                        Intent intent = new Intent(Intent.ACTION_DIAL, u);
                        try
                        {
                            mcontext.startActivity(intent);
                        }
                        catch (SecurityException s1)
                        {
                            Toast.makeText(mcontext, s1.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(s.contains("@gmail.com"))
                    {
                        Toast.makeText(mcontext, "email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("email"));
                        String s11[] = {s};
                        intent.putExtra(Intent.EXTRA_EMAIL, s11);
                        intent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(intent, "Launch Email");
                        mcontext.startActivity(chooser);
                    }
                }
            });

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject.getAgo();

            Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);

        } else if ("video".equals(mediaObject.getType()) && !(TextUtils.isEmpty(mediaObject.getDescription()))) {

            Uri uri = Uri.parse(mediaObject.getImageurl());
            holder.clickplay.setVisibility(View.VISIBLE);
            holder.thumbnail.setVisibility(View.GONE);

            Glide.with(mcontext).asBitmap()
                    .load(uri)
                    .into(holder.videothumbnail);

            holder.description.setLinkText(mediaObject.getDescription());

            holder.description.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
                @Override
                public void onLinkClicked(int i, String s) {
                    String MobilePattern = "[0-9]{10}";
                    if(s.startsWith("#"))
                    {
                        Toast.makeText(mcontext, s, Toast.LENGTH_SHORT).show();
                    }
                    else if(s.startsWith("@"))
                    {

                    }
                    else if(s.startsWith("https://") || (s.endsWith(".com") ||(s.startsWith("www"))))
                    {
                        Uri webpage = Uri.parse(s);
                        if (!s.startsWith("http://") && !s.startsWith("https://")) {
                            webpage = Uri.parse("http://" + s);
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        mcontext.startActivity(intent);
                    }
                    else if(s.matches(MobilePattern))
                    {
                        Uri u = Uri.parse("tel:" + s);
                        Intent intent = new Intent(Intent.ACTION_DIAL, u);
                        try
                        {
                            mcontext.startActivity(intent);
                        }
                        catch (SecurityException s1)
                        {
                            Toast.makeText(mcontext, s1.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(s.contains("@gmail.com"))
                    {
                        Toast.makeText(mcontext, "email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("email"));
                        String s11[] = {s};
                        intent.putExtra(Intent.EXTRA_EMAIL, s11);
                        intent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(intent, "Launch Email");
                        mcontext.startActivity(chooser);
                    }
                }
            });

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject.getAgo();

            Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);

        } else if ("video".equals(mediaObject.getType()) && (TextUtils.isEmpty(mediaObject.getDescription()))) {

            holder.thumbnail.setVisibility(View.GONE);
            Uri uri = Uri.parse(mediaObject.getImageurl());
            holder.clickplay.setVisibility(View.VISIBLE);

            Glide.with(mcontext).asBitmap()
                    .load(uri)
                    .into(holder.videothumbnail);

            holder.description.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject.getAgo();

            Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);
        }


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(mediaObject.getPublisher());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String image1 = dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String username = dataSnapshot.child("USERNAME_ID").getValue(String.class);

                String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                holder.title1.setText("@"+username);
                holder.title.setText(name1);
                Picasso.with(mcontext).load(image1).placeholder(R.drawable.pro).into(holder.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.clickplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, VideoPlayingActivity.class);
                String videourl = mediaObject.getImageurl();
                intent.putExtra("videouri", videourl);
                mcontext.startActivity(intent);

            }
        });


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("image".equals(mediaObject.getType())) {
                    Intent intent = new Intent(mcontext, ImageView_forphoto.class);
                    String imageurl = mediaObject.getImageurl();
                    intent.putExtra("uri", imageurl);
                    intent.putExtra("bitmaps",mediaObject.getBitmap());
                    mcontext.startActivity(intent);
                }
            }
        });

        // notifyDataSetChanged();

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.like.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(mediaObject.getPushid())
                                    .child(firebaseUser.getUid()).setValue(true);
                            holder.b1.likeAnimation();

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(mediaObject.getPushid())
                                    .child(firebaseUser.getUid()).removeValue();
                        }
                    }
                });
            }
        });

/*
        holder.b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if (holder.repost.getTag().equals("repost")) {

                            Bundle bundle=new Bundle();
                            bundle.putString("postid",mediaObject.getPushid());
                            bundle.putString("reposttag",holder.repost.getTag().toString());
                            repost_bottomsheet bottomsheet=new repost_bottomsheet();
                            bottomsheet.setArguments(bundle);
                            bottomsheet.show(((AppCompatActivity)mcontext).getSupportFragmentManager(),"repost_bottomsheet");

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Repost").child(mediaObject.getPushid())
                                    .child(firebaseUser.getUid()).removeValue();

                            savedpushid[0]=mediaObject.getPushid();


                            final List<String> mrepost=new ArrayList<>();
                            final String[] s=new String[1];

                            final DatabaseReference refry =FirebaseDatabase.getInstance().getReference().child("posts");

                            refry.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mrepost.clear();
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren())
                                    {
                                        if(snapshot.hasChild("repost_publisher"))
                                        {
                                            if((firebaseUser.getUid().equals(mediaObject.getRepost_publisher())) && (savedpushid[0].equals(mediaObject.getRepost_pushid())))
                                            {
                                                s[0]=mediaObject.getRepost_pushid();
                                            }
                                        }

                                    }

                                    if(!TextUtils.isEmpty(s[0]))
                                    {
                                        refry.child(mrepost.get(0)).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                  }
        });

        */


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BitmapDrawable bitmapDrawable=(BitmapDrawable) holder.thumbnail.getDrawable();

                if(bitmapDrawable!=null)
                {
                    flag="true";
                }

              //  Bitmap b=bitmapDrawable.getBitmap();
                String decrpt=holder.description.getText().toString();

                Bundle bundle=new Bundle();
                bundle.putString("postid",mediaObject.getPushid());
                bundle.putString("publisherid",mediaObject.getPublisher());
                bundle.putString("uri",mediaObject.getImageurl());
          //      bundle.putSerializable("bitmapdrawable", (Serializable) bitmapDrawable);
                bundle.putString("desc",decrpt);
                //bundle.putParcelable("bit", (Parcelable) b);
                bundle.putString("flag",flag);
               SharebottomSheets bottomsheet=new SharebottomSheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(((AppCompatActivity)mcontext).getSupportFragmentManager(),"SharebottomSheets");
            }
        });



        holder.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.b3.isSelected()) {
                            holder.book.setImageResource(R.drawable.bookmark);
                            holder.b3.setSelected(false);
                        } else {
                            holder.b3.setSelected(true);
                            holder.book.setImageResource(R.drawable.afterbook);
                            holder.b3.likeAnimation();
                        }
                    }
                });
            }
        });

        holder.postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Bundle bundle=new Bundle();
                        bundle.putString("postid",mediaObject.getPushid());
                        bundle.putString("publisherid",mediaObject.getPublisher());
                        PostbottomSheets bottomsheet=new PostbottomSheets();
                        bottomsheet.setArguments(bundle);
                        bottomsheet.show(((AppCompatActivity)mcontext).getSupportFragmentManager(),"PostbottomSheets");
            }
        });

        islikes(mediaObject.getPushid(), holder.like);
        nooflikes(holder.likes, mediaObject.getPushid());
        getComments(mediaObject.getPushid(), holder.comments);

        //isrepost(mediaObject.getPushid(), holder.repost);
        //noofrepost(holder.repostcount, mediaObject.getPushid());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mcontext, PostViewer_Activity.class);
                intent.putExtra("bitmap",mediaObjects1.get(position).getBitmap());
                intent.putExtra("description",mediaObjects1.get(position).getDescription());
                intent.putExtra("imageurl",mediaObjects1.get(position).getImageurl());
                intent.putExtra("postid",mediaObjects1.get(position).getPushid());
                intent.putExtra("publisher",mediaObjects1.get(position).getPublisher());
                intent.putExtra("cardtype",mediaObjects1.get(position).getType());
                mcontext.startActivity(intent);
            }
        });

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.book.getTag().equals("save"))
                {
                     FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(mediaObject.getPushid()).setValue("true");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(mediaObject.getPushid()).removeValue();
                }
            }
        });

        ispostsaved(mediaObject.getPushid(), holder.book);
        gettinglikedbytextandimage(mediaObject.getPushid(),holder.likedbyimage,holder.likedbytext,holder.likes,holder.viewmore);

    }

    /*

    private void noofrepost(final TextView repostcount, String pushid) {


        DatabaseReference mref4=FirebaseDatabase.getInstance().getReference().child("Repost").child(pushid);

        mref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                repostcount.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
  }

    private void isrepost(String pushid, final CircleImageView repost)
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Repost").child(pushid);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    repost.setImageResource(R.drawable.repostcount);
                    repost.setTag("reposted");
                }
                else
                {
                    repost.setImageResource(R.drawable.ic_repeat_black_24dp);
                    repost.setTag("repost");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    */

    @Override
    public int getItemCount() {
        return mediaObjects1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView title1,title,likes,comments,agotime,likedbytext,viewmore;
        SocialTextView description;
        PinchZoomImageView thumbnail,videothumbnail;
        CircleImageView image,like,comment,book,share,likedbyimage;
        Context mcontext;
        View parent;
        RequestManager requestManager;
        SmallBangView b1,b3,b4;
        ImageView postbutton,clickplay;
        CardView card;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            viewmore=itemView.findViewById(R.id.viewmore);
            likedbyimage=itemView.findViewById(R.id.likedbyimage);
            likedbytext=itemView.findViewById(R.id.likedbytext);
            videothumbnail=itemView.findViewById(R.id.videothumbnail);
            title1=itemView.findViewById(R.id.title1);
            description=itemView.findViewById(R.id.description);
            postbutton=itemView.findViewById(R.id.postbutton);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            book=itemView.findViewById(R.id.book);
            likes=itemView.findViewById(R.id.likes);
            comments=itemView.findViewById(R.id.comments);

            agotime=itemView.findViewById(R.id.agotime);

            card=itemView.findViewById(R.id.card);

            share=itemView.findViewById(R.id.share);

            clickplay=itemView.findViewById(R.id.clickplay);
            image=itemView.findViewById(R.id.image);
            b1=itemView.findViewById(R.id.b1);
            b3=itemView.findViewById(R.id.b3);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title= itemView.findViewById(R.id.title);
            parent.setTag(this);
        }
    }

    public void gettinglikedbytextandimage(String pushid, final CircleImageView likedbyimage, final TextView likedbytext, final TextView likes, final TextView viewmore)
    {
        gettinglikedby=new ArrayList<>();
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Likes").child(pushid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gettinglikedby.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                   gettinglikedby.add(ds.getKey());
                }

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                if(gettinglikedby.size()==0)
                {
                    likedbyimage.setVisibility(View.GONE);
                    likedbytext.setVisibility(View.GONE);
                    viewmore.setVisibility(View.GONE);
                }
                else if(gettinglikedby.size()==1)
                {
                    if(!firebaseUser.getUid().equals(gettinglikedby.get(0)))
                    {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(gettinglikedby.get(0));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String text=dataSnapshot.child("USERNAME_ID").getValue(String.class);
                                String image=dataSnapshot.child("IMAGEURL").getValue(String.class);

                                String likedbyname=gettinglikedby.get(0);
                                String like=likes.getText().toString();
                                int count=Integer.parseInt(like);
                                int c=count-1;
                                String like1=Integer.toString(c);
                                String textsample="Liked by "+text+" "+"and";
                                viewmore.setVisibility(View.VISIBLE);

                                likedbytext.setText(textsample);
                                Picasso.with(mcontext).load(image).into(likedbyimage);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        likedbyimage.setVisibility(View.GONE);
                        likedbytext.setVisibility(View.GONE);
                        viewmore.setVisibility(View.GONE);
                    }
                }
                else if(gettinglikedby.size()==2)
                {
                    if(!firebaseUser.getUid().equals(gettinglikedby.get(0)))
                    {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(gettinglikedby.get(0));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String text=dataSnapshot.child("USERNAME_ID").getValue(String.class);
                                String image=dataSnapshot.child("IMAGEURL").getValue(String.class);


                                String likedbyname=gettinglikedby.get(0);
                                String like=likes.getText().toString();
                                int count=Integer.parseInt(like);
                                int c=count-1;
                                String like1=Integer.toString(c);

                                String textsample="Liked by "+text+" "+"and";
                                viewmore.setVisibility(View.VISIBLE);

                                likedbytext.setText(textsample);
                                Picasso.with(mcontext).load(image).into(likedbyimage);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(!firebaseUser.getUid().equals(gettinglikedby.get(1)))
                    {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(gettinglikedby.get(1));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String text=dataSnapshot.child("USERNAME_ID").getValue(String.class);
                                String image=dataSnapshot.child("IMAGEURL").getValue(String.class);



                                String likedbyname=gettinglikedby.get(1);
                                String like=likes.getText().toString();
                                int count=Integer.parseInt(like);
                                int c=count-1;
                                String like1=Integer.toString(c);

                                String textsample="Liked by "+text+"and";
                                viewmore.setVisibility(View.VISIBLE);

                                likedbytext.setText(textsample);
                                Picasso.with(mcontext).load(image).into(likedbyimage);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        likedbyimage.setVisibility(View.GONE);
                        likedbytext.setVisibility(View.GONE);
                        viewmore.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String pushid, final TextView comments)
    {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("comments").child(pushid);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                comments.setText(""+dataSnapshot.getChildrenCount());
                if(comments.getText().toString().equals("0"))
                {
                    comments.setVisibility(View.GONE);
                }
                else if(comments.getText().toString().equals("1"))
                {
                    comments.setVisibility(View.VISIBLE);
                }
                else if(comments.getText().toString().equals("2"))
                {
                    comments.setVisibility(View.VISIBLE);
                }
                else
                {
                    comments.setVisibility(View.VISIBLE);
                }
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
                if(likes.getText().toString().equals("0"))
                {
                    likes.setVisibility(View.GONE);
                }
                else
                {
                    likes.setVisibility(View.VISIBLE);
                    likes.setText(dataSnapshot.getChildrenCount()+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

