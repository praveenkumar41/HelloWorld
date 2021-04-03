package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.project2.Comments_Activity;
import com.example.project2.ImageViewHelperCorner;
import com.example.project2.ImageView_forphoto;
import com.example.project2.PinchZoomImageView;
import com.example.project2.PostbottomSheets;
import com.example.project2.R;
import com.example.project2.Timeago;
import com.example.project2.VideoPlayingActivity;
import com.example.project2.videostream.MediaObject;
import com.example.project2.videostream.MediaObject1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.project2.Comments_Activity;
import com.example.project2.ImageViewHelperCorner;
import com.example.project2.ImageView_forphoto;
import com.example.project2.PinchZoomImageView;
import com.example.project2.PostbottomSheets;
import com.example.project2.R;
import com.example.project2.Timeago;
import com.example.project2.VideoPlayingActivity;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.project2.Comments_Activity;
import com.example.project2.ImageViewHelperCorner;
import com.example.project2.ImageView_forphoto;
import com.example.project2.PinchZoomImageView;
import com.example.project2.PostbottomSheets;
import com.example.project2.R;
import com.example.project2.Timeago;
import com.example.project2.VideoPlayingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class Save_Post_display_Adapter extends RecyclerView.Adapter<Save_Post_display_Adapter.ViewHolder> {

    private ArrayList<MediaObject1> msaves;
    private RequestManager requestManager;
    Context mcontext;
    Bitmap bitmap;
    private static final int IMAGE_POST= 0;
    private static final int VIDEO_POST = 1;
    private static final int TEXT_POST= 2;

    String savedpushid[]=new String[10];
    List<String> gettinglikedby;

    public Save_Post_display_Adapter(Context context, ArrayList<MediaObject1> mediaObjects1) {
        this.mcontext=context;
        this.msaves = mediaObjects1;
    }

    @NonNull
    @Override
    public Save_Post_display_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        return new Save_Post_display_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Save_Post_display_Adapter.ViewHolder holder, int position) {

        final MediaObject1 media11 =msaves.get(position);


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, Comments_Activity.class);
                intent.putExtra("pushid", media11.getPushid());
                intent.putExtra("publisherid",  media11.getPublisher());
                mcontext.startActivity(intent);

            }
        });


        if ("image".equals( media11.getType()) && !TextUtils.isEmpty( media11.getDescription())) {

            holder.description.setLinkText( media11.getDescription());

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



            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(mcontext,  media11.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);
            holder.videothumbnail.setVisibility(View.GONE);
            Post_Timeago timeago = new Post_Timeago();
            long lasttime =  media11.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);

        } else if ("image".equals( media11.getType()) && TextUtils.isEmpty( media11.getDescription())) {
            holder.description.setVisibility(View.GONE);
            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(mcontext,  media11.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);
            holder.videothumbnail.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime =  media11.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);


        } else if ("text".equals( media11.getType())) {
            holder.thumbnail.setVisibility(View.GONE);
            holder.videothumbnail.setVisibility(View.GONE);
            holder.description.setLinkText( media11.getDescription());

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
            long lasttime =  media11.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);

        } else if ("video".equals( media11.getType()) && !(TextUtils.isEmpty( media11.getDescription()))) {

            Uri uri = Uri.parse( media11.getImageurl());
            holder.clickplay.setVisibility(View.VISIBLE);
            holder.thumbnail.setVisibility(View.GONE);

            Glide.with(mcontext).asBitmap()
                    .load(uri)
                    .into(holder.videothumbnail);

            holder.description.setLinkText( media11.getDescription());

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
            long lasttime =  media11.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);


        } else if ("video".equals( media11.getType()) && (TextUtils.isEmpty( media11.getDescription()))) {

            holder.thumbnail.setVisibility(View.GONE);
            Uri uri = Uri.parse( media11.getImageurl());
            holder.clickplay.setVisibility(View.VISIBLE);

            Glide.with(mcontext).asBitmap()
                    .load(uri)
                    .into(holder.videothumbnail);

            holder.description.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime =  media11.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime, mcontext);
            holder.agotime.setText(Lastseentime);
        }

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child( media11.getPublisher());
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String image1 = dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name = dataSnapshot.child("USERNAME").getValue(String.class);
                String nameid = dataSnapshot.child("USERNAME_ID").getValue(String.class);
                String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                holder.title.setText(name1);
                holder.title1.setText("@"+nameid);
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
                String videourl =  media11.getImageurl();
                intent.putExtra("videouri", videourl);
                mcontext.startActivity(intent);

            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("image".equals( media11.getType())) {
                    Intent intent = new Intent(mcontext, ImageView_forphoto.class);
                    String imageurl =  media11.getImageurl();
                    intent.putExtra("uri", imageurl);
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
                            FirebaseDatabase.getInstance().getReference().child("Likes").child( media11.getPushid())
                                    .child(firebaseUser.getUid()).setValue(true);
                            holder.b1.likeAnimation();

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child( media11.getPushid())
                                    .child(firebaseUser.getUid()).removeValue();

                        }
                    }
                });
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


            }
        });

        islikes( media11.getPushid(), holder.like);
        nooflikes(holder.likes,  media11.getPushid());
        getComments( media11.getPushid(), holder.comments,holder.viewcomments);

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.book.getTag().equals("save"))
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child( media11.getPushid()).setValue("true");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child( media11.getPushid()).removeValue();
                }

            }
        });

        ispostsaved( media11.getPushid(), holder.book);

        gettinglikedbytextandimage(media11.getPushid(),holder.likedbyimage,holder.likedbytext,holder.likes);

    }

    @Override
    public int getItemCount() {
        return msaves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,title1,likes,comments,agotime,repostcount,likedbytext,viewcomments;
        SocialTextView description;
        PinchZoomImageView thumbnail,videothumbnail;
        CircleImageView image,like,comment,book,repost,likedbyimage;
        Context mcontext;
        View parent;
        RequestManager requestManager;
        SmallBangView b1,b3;
        ImageView postbutton,clickplay;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            agotime=itemView.findViewById(R.id.agotime);
            title1=itemView.findViewById(R.id.title1);
            videothumbnail=itemView.findViewById(R.id.videothumbnail);


            likedbyimage=itemView.findViewById(R.id.likedbyimage);
            likedbytext=itemView.findViewById(R.id.likedbytext);

            description=itemView.findViewById(R.id.description);
            postbutton=itemView.findViewById(R.id.postbutton);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            book=itemView.findViewById(R.id.book);
            likes=itemView.findViewById(R.id.likes);
            comments=itemView.findViewById(R.id.comments);


            clickplay=itemView.findViewById(R.id.clickplay);
            image=itemView.findViewById(R.id.image);
            b1=itemView.findViewById(R.id.b1);
            b3=itemView.findViewById(R.id.b3);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title= itemView.findViewById(R.id.title);
            parent.setTag(this);

        }
    }

    private void gettinglikedbytextandimage(String pushid, final CircleImageView likedbyimage, final TextView likedbytext, final TextView likes)
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
                                String textsample="Liked by "+text+" "+"and"+" "+like1+" "+"others";


                                SpannableString ss=new SpannableString(textsample);
                                StyleSpan bold=new StyleSpan(Typeface.BOLD);

                                int o=9+text.length()+5+like1.length();
                                int f=o+7;
                                int tot=9+text.length()+1;
                                ss.setSpan(bold,8,tot, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ss.setSpan(bold,o,f,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                likedbytext.setText(ss);
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

                                String textsample="Liked by "+text+" "+"and"+" "+like1+" "+"others";

                                SpannableString ss=new SpannableString(textsample);
                                StyleSpan bold=new StyleSpan(Typeface.BOLD);

                                int o=9+text.length()+5+like1.length();
                                int f=o+7;
                                int tot=9+text.length()+1;
                                ss.setSpan(bold,8,tot,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ss.setSpan(bold,o,f,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                likedbytext.setText(ss);
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

                                String textsample="Liked by "+text+" "+"and"+" "+like1+" "+"others";

                                SpannableString ss=new SpannableString(textsample);
                                StyleSpan bold=new StyleSpan(Typeface.BOLD);

                                int o=9+text.length()+5+like1.length();
                                int f=o+7;
                                int tot=9+text.length()+1;
                                ss.setSpan(bold,8,tot,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ss.setSpan(bold,o,f,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                likedbytext.setText(ss);
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void getComments(String pushid, final TextView comments, final TextView viewcomments)
    {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("comments").child(pushid);
        mref.keepSynced(true);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                comments.setText(""+dataSnapshot.getChildrenCount());
                if(comments.getText().toString().equals("0"))
                {
                    viewcomments.setVisibility(View.GONE);
                    comments.setVisibility(View.GONE);
                }
                else if(comments.getText().toString().equals("1"))
                {
                    comments.setVisibility(View.VISIBLE);
                    viewcomments.setVisibility(View.VISIBLE);
                    viewcomments.setText("view"+" "+""+dataSnapshot.getChildrenCount()+" "+"comments");
                }
                else if(comments.getText().toString().equals("2"))
                {
                    comments.setVisibility(View.VISIBLE);
                    viewcomments.setVisibility(View.VISIBLE);
                    viewcomments.setText("view all"+" "+""+dataSnapshot.getChildrenCount()+" "+"comments");
                }
                else
                {
                    comments.setVisibility(View.VISIBLE);
                    viewcomments.setVisibility(View.VISIBLE);
                    viewcomments.setText("view all"+" "+""+dataSnapshot.getChildrenCount()+" "+"comments");
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
        mref.keepSynced(true);

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
        mref.keepSynced(true);

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
        mref4.keepSynced(true);

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

