package com.example.project2;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.project2.videostream.MediaObject1;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class Photo_display_Adapter extends RecyclerView.Adapter<Photo_display_Adapter.ViewHolder> {

    private ArrayList<MediaObject1> mediaObjects1;
    Context mcontext;
    Bitmap bitmap;
    String savedpushid;
    List<String> gettinglikedby;


    public Photo_display_Adapter(Context context, ArrayList<MediaObject1> mediaObjects1) {
        this.mcontext = context;
        this.mediaObjects1 = mediaObjects1;
    }

    @NonNull
    @Override
    public Photo_display_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        return new Photo_display_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MediaObject1 mediaObject44 = mediaObjects1.get(position);

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, Comments_Activity.class);
                intent.putExtra("pushid", mediaObject44.getPushid());
                intent.putExtra("publisherid", mediaObject44.getPublisher());
                mcontext.startActivity(intent);

            }
        });


        if(!TextUtils.isEmpty(mediaObject44.getDescription()))
        {
            holder.description.setLinkText(mediaObject44.getDescription());

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



            Bitmap bitmap=ImageViewHelperCorner.imagefromDrawable(mcontext,mediaObject44.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);
            holder.videothumbnail.setVisibility(View.GONE);
            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject44.getAgo();

            String Lastseentime = timeago.Post_Timeago(lasttime,mcontext);
            holder.agotime.setText(Lastseentime);

        }

        else if(TextUtils.isEmpty(mediaObject44.getDescription()))
        {
            holder.description.setVisibility(View.GONE);
            Bitmap bitmap=ImageViewHelperCorner.imagefromDrawable(mcontext,mediaObject44.getBitmap());
            holder.thumbnail.setImageBitmap(bitmap);
            holder.videothumbnail.setVisibility(View.GONE);

            Post_Timeago timeago = new Post_Timeago();
            long lasttime = mediaObject44.getAgo();

            String Lastseentime = Post_Timeago.Post_Timeago(lasttime,mcontext);
            holder.agotime.setText(Lastseentime);

        }


        holder.postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("postid",mediaObject44.getPushid());
                bundle.putString("publisherid",mediaObject44.getPublisher());
                PostbottomSheets bottomsheet=new PostbottomSheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(((AppCompatActivity)mcontext).getSupportFragmentManager(),"PostbottomSheets");
            }
        });





        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(mcontext, ImageView_forphoto.class);
                    String imageurl=mediaObject44.getImageurl();
                    intent.putExtra("uri",imageurl);
                    mcontext.startActivity(intent);

            }
        });



        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(mediaObjects1.get(position).getPublisher());
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

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.like.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(mediaObject44.getPushid())
                                    .child(firebaseUser.getUid()).setValue(true);
                            holder.b1.likeAnimation();

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(mediaObject44.getPushid())
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


        islikes(mediaObject44.getPushid(),holder.like);
        nooflikes(holder.likes,mediaObject44.getPushid());
        getComments(mediaObject44.getPushid(),holder.comments,holder.viewcomments);


        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.book.getTag().equals("save"))
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(mediaObject44.getPushid()).setValue("true");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("saved").child(firebaseUser.getUid()).child(mediaObject44.getPushid()).removeValue();
                }
            }
        });

        ispostsaved(mediaObject44.getPushid(), holder.book);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("postid",mediaObject44.getPushid());
                bundle.putString("publisherid",mediaObject44.getPublisher());
                SharebottomSheets bottomsheet=new SharebottomSheets();
                bottomsheet.setArguments(bundle);
                bottomsheet.show(((AppCompatActivity)mcontext).getSupportFragmentManager(),"SharebottomSheets");
            }
        });

        gettinglikedbytextandimage(mediaObject44.getPushid(),holder.likedbyimage,holder.likedbytext,holder.likes);

    }

    @Override
    public int getItemCount() {
        return mediaObjects1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,title1, likes, comments, agotime,likedbytext,viewcomments;
        SocialTextView description;
        PinchZoomImageView thumbnail, videothumbnail;
        CircleImageView image, like, comment, book,share,likedbyimage;
        Context mcontext;
        View parent;
        RequestManager requestManager;
        SmallBangView b1, b3;
        ImageView postbutton, clickplay;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            agotime = itemView.findViewById(R.id.agotime);
            videothumbnail = itemView.findViewById(R.id.videothumbnail);
            likedbyimage=itemView.findViewById(R.id.likedbyimage);
            likedbytext=itemView.findViewById(R.id.likedbytext);


            title1=itemView.findViewById(R.id.title1);
            description = itemView.findViewById(R.id.description);
            postbutton = itemView.findViewById(R.id.postbutton);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            book = itemView.findViewById(R.id.book);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);


            share= itemView.findViewById(R.id.share);

            clickplay = itemView.findViewById(R.id.clickplay);
            image = itemView.findViewById(R.id.image);
            b1 = itemView.findViewById(R.id.b1);
            b3 = itemView.findViewById(R.id.b3);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            parent.setTag(this);

        }
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


    public void gettinglikedbytextandimage(String pushid, final CircleImageView likedbyimage, final TextView likedbytext, final TextView likes)
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


}