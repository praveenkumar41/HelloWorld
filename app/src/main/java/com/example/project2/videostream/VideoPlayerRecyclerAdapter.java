package com.example.project2.videostream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.project2.ChatpadAdapter;
import com.example.project2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<VideoPlayerRecyclerAdapter.ViewHolder> {

    private ArrayList<MediaObject> mediaObjects;
    private RequestManager requestManager;
    Context mcontext;
    Bitmap bitmap;
    private static final int IMAGE_POST= 0;
    private static final int VIDEO_POST = 1;
    private static final int TEXT_POST= 2;


    public VideoPlayerRecyclerAdapter(Context context, ArrayList<MediaObject> mediaObjects, RequestManager requestManager) {
        this.mcontext=context;
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }


    @NonNull
    @Override
    public VideoPlayerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_video_list_item, viewGroup, false);
            return new VideoPlayerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        MediaObject mediaObject=mediaObjects.get(position);

        try {
            bitmap = retriveVideoFrameFromVideo(mediaObject.getVideourl());
            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 1200, 660, false);
                holder.thumbnail.setImageBitmap(bitmap);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


//        this.requestManager.load(mm).into(holder.thumbnail);
        // ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String image1=dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name=dataSnapshot.child("USERNAME").getValue(String.class);
                String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);

                holder.title.setText(name1);
                Picasso.with(mcontext).load(image1).placeholder(R.drawable.pro).into(holder.image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        if("image".equals(mediaObject.getType()))
        {
            Picasso.with(mcontext).load(mediaObject.getVideourl()).placeholder(R.drawable.pro).into(holder.image);
        }
*/
/*
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.b1.isSelected()) {
                            holder.like.setImageResource(R.drawable.like);
                            holder.b1.setSelected(false);

                        } else {
                            holder.b1.setSelected(true);
                            holder.like.setImageResource(R.drawable.likecount);
                            holder.b1.likeAnimation();
                        }
                    }
                });
            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.b2.isSelected()) {
                            holder.heart.setImageResource(R.drawable.heart);
                            holder.b2.setSelected(false);
                        } else {
                            holder.b2.setSelected(true);
                            holder.heart.setImageResource(R.drawable.heartcount);
                            holder.b2.likeAnimation();
                        }
                    }
                });
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        FrameLayout media_container;
        TextView title,name;
        ImageView thumbnail, volumeControl;
        CircleImageView image,like,heart;
        ProgressBar progressBar;
        Context mcontext;
        View parent;
        RequestManager requestManager;
        SmallBangView b1,b2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            media_container = itemView.findViewById(R.id.media_container);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            progressBar = itemView.findViewById(R.id.progressBar);
            image=itemView.findViewById(R.id.image);
            //         like=itemView.findViewById(R.id.like);
            //       heart=itemView.findViewById(R.id.heart);
            b1=itemView.findViewById(R.id.b1);
            b2=itemView.findViewById(R.id.b2);
            volumeControl = itemView.findViewById(R.id.volume_control);
            parent.setTag(this);

        }

    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}

