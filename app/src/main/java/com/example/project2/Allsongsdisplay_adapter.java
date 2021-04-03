package com.example.project2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


    public class Allsongsdisplay_adapter extends RecyclerView.Adapter<com.example.project2.Allsongsdisplay_adapter.ViewHolder>{

        private Context mcontext;
        private List<Displaysongs> msongs;
        private DatabaseReference mref2,mref1,mref21,dataDetails,dataDetails1;
        FirebaseUser firebaseUser;
        MediaPlayer mediaPlayer;
        String user_id;


        public Allsongsdisplay_adapter(Context mcontext, List<Displaysongs> msongs, String userid)
        {
            this.mcontext=mcontext;
            this.msongs= msongs;
            this.user_id=userid;
        }

        @NonNull
        @Override
        public Allsongsdisplay_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.song_cardlayout, parent, false);

            return new Allsongsdisplay_adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final Displaysongs displaysongs = msongs.get(position);


            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


            mref1 = FirebaseDatabase.getInstance().getReference().child("Music_upload").child(firebaseUser.getUid()).child(user_id);
            mref1.keepSynced(true);


            holder.un.setText(displaysongs.getSongname());

       /*     holder.un.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jcplayer1.playAudio(jcAudios1.get(position));
                    jcplayer1.setVisibility(View.VISIBLE);
                    jcplayer1.createNotification(R.drawable.ic_album_black_24dp);
                }
            });

*/

            holder.un.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                            String audiourl = displaysongs.getSongurl();

                            if (audiourl != null)
                            {

                                mediaPlayer = new MediaPlayer();
                                try {
                                    mediaPlayer.setDataSource(audiourl);
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(final MediaPlayer mediaPlayer) {
                                            mediaPlayer.start();

                                        }
                                    });
                                    mediaPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }


                }

                //final String audiourl = displaysongs.getSongurl();
            });

        }

        @Override
        public int getItemCount() {
            return msongs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {

            public TextView un;
            public CircleImageView imageenter1;



            public ViewHolder(View itemView)
            {

                super(itemView);

                un=itemView.findViewById(R.id.un);
                imageenter1=itemView.findViewById(R.id.imageenter1);

            }

/*
            public void setUserimage(String image, Context mcontext) {

                Picasso.with(mcontext).load(image).placeholder(R.drawable.pro).into(imageenter1);
            }

 */
        }

    }
