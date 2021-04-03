package com.example.project2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatpadAdapter extends RecyclerView.Adapter<ChatpadAdapter.ViewHolder> {

    private Context mcontext;
    private List<Chats> mchat;
    String pushed;
    String s1 = "true";
    Boolean seened;
    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable updater;
    private static final int MSG_LEFT = 0;
    private static final int MSG_RIGHT = 1;
    private static final int DATES = 2;
    int realtimelength,counter;


    ValueEventListener seenlistener;


    DatabaseReference musicref,imageref,mref99,mref22,reference88,reference1,reference77,reference66,chatseentext,seendatabase,mref1, datadetails, datadetails1, mref, chatref, chatref1,refry,reference;
    FirebaseUser firebaseUser;
    String imageurl;


    public ChatpadAdapter(Context mcontext, List<Chats> mchat, String pushed) {
        this.mcontext = mcontext;
        this.mchat = mchat;
        this.pushed = pushed;

    }



    @NonNull
    @Override
    public ChatpadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.single_chatpad1, parent, false);
            return new ChatpadAdapter.ViewHolder(view);

        } else
            {
                View view = LayoutInflater.from(mcontext).inflate(R.layout.single_chatpad, parent, false);
                return new ChatpadAdapter.ViewHolder(view);

           }


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Chats chats = mchat.get(position);


        final String pushkey = chats.getPushid();

        final String user_id = chats.getReceiverid();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        chatref = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatref.keepSynced(true);

        chatref1 = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatref1.keepSynced(true);

        seendatabase = FirebaseDatabase.getInstance().getReference().child("seen");
        seendatabase.keepSynced(true);

        Boolean s = true;

        holder.un.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

                final View view1 = LayoutInflater.from(mcontext).inflate(R.layout.deletetextmessage, null);
                Button forme = view1.findViewById(R.id.forme);
                Button forboth = view1.findViewById(R.id.forboth);
                Button copytext = view1.findViewById(R.id.copytext);
                Button cancel = view1.findViewById(R.id.cancel);

                builder.setView(view1);
                builder.setTitle("Delete Message");
                builder.setMessage("Do you Want to delete this message");
                builder.setIcon(R.drawable.ic_delete_black_24dp);
                final AlertDialog dialog = builder.create();
                dialog.show();


                forme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#0080FF"));
                                st.setBoldText();
                                st.setTextColor(Color.parseColor("#FFFFFF"));
                                st.show();

                                dialog.dismiss();


                            }
                        });

                    }
                });


                forboth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                chatref1.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                        st.setBackgroundColor(Color.parseColor("#0080FF"));
                                        st.setBoldText();
                                        st.setTextColor(Color.parseColor("#FFFFFF"));
                                        st.show();
                                        dialog.dismiss();


                                    }
                                });
                            }
                        });

                    }
                });

                copytext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager = (ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String text = holder.un.getText().toString();
                        ClipData clipData = ClipData.newPlainText("text", text);
                        clipboardManager.setPrimaryClip(clipData);
                        StyleableToast st = new StyleableToast(mcontext, "Text Copied", Toast.LENGTH_SHORT);
                        st.setBackgroundColor(Color.parseColor("#0080FF"));
                        st.setBoldText();
                        st.setTextColor(Color.parseColor("#FFFFFF"));
                        st.show();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return false;
            }
        });


        holder.imagemessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

                final View view1 = LayoutInflater.from(mcontext).inflate(R.layout.deleteimagemessages, null);
                Button forme = view1.findViewById(R.id.forme);
                Button forboth = view1.findViewById(R.id.forboth);
                Button cancel = view1.findViewById(R.id.cancel);

                builder.setView(view1);
                builder.setTitle("Delete Message");
                builder.setMessage("Do you Want to delete this message");
                builder.setIcon(R.drawable.ic_delete_black_24dp);
                final AlertDialog dialog = builder.create();
                dialog.show();

                forme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#0080FF"));
                                st.setBoldText();
                                st.setTextColor(Color.parseColor("#FFFFFF"));
                                st.show();
                                dialog.dismiss();


                            }
                        });

                    }
                });


                forboth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                chatref1.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                        st.setBackgroundColor(Color.parseColor("#0080FF"));
                                        st.setBoldText();
                                        st.setTextColor(Color.parseColor("#FFFFFF"));
                                        st.show();
                                        dialog.dismiss();


                                    }
                                });
                            }
                        });
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return false;
            }
        });


        holder.musiccard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

                final View view1 = LayoutInflater.from(mcontext).inflate(R.layout.deleteaudiomessages, null);
                Button forme = view1.findViewById(R.id.forme);
                Button forboth = view1.findViewById(R.id.forboth);
                Button download = view1.findViewById(R.id.download);
                Button cancel = view1.findViewById(R.id.cancel);

                builder.setView(view1);
                builder.setTitle("Delete Message");
                builder.setMessage("Do you Want to delete this message");
                builder.setIcon(R.drawable.ic_delete_black_24dp);
                final AlertDialog dialog = builder.create();
                dialog.show();


                forme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#0080FF"));
                                st.setBoldText();
                                st.setTextColor(Color.parseColor("#FFFFFF"));
                                st.show();
                                dialog.dismiss();


                            }
                        });

                    }
                });


                forboth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatref.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                chatref1.child(chats.getPushid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        StyleableToast st = new StyleableToast(mcontext, "Message Deleted", Toast.LENGTH_SHORT);
                                        st.setBackgroundColor(Color.parseColor("#0080FF"));
                                        st.setBoldText();
                                        st.setTextColor(Color.parseColor("#FFFFFF"));
                                        st.show();
                                        dialog.dismiss();

                                    }
                                });
                            }
                        });
                    }
                });


                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return false;
            }
        });


        final String[] userid = {chats.getReceiverid()};


        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.keepSynced(true);



        holder.imagemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, ImageView_forphoto.class);
                intent.putExtra("uri",chats.getMessage());
                mcontext.startActivity(intent);

            }
        });

        DatabaseReference mrefry12=FirebaseDatabase.getInstance().getReference("users").child(chats.getReceiverid());
        mrefry12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String images=dataSnapshot.child("IMAGEURL").getValue(String.class);

                Picasso.with(mcontext).load(images).into(holder.imageenter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String messagetype = chats.getType();

        if ("text".equals(messagetype)) {
         //   holder.cardimagemessage.setVisibility(View.GONE);
            holder.imagemessage.setVisibility(View.GONE);
            holder.un.setVisibility(View.VISIBLE);
            holder.imagetime1.setVisibility(View.GONE);
            holder.time1.setText(chats.getTime());
            holder.un.setText(chats.getMessage());
            holder.musiccard.setVisibility(View.GONE);


        } else if ("image".equals(messagetype))
        {

            holder.un.setVisibility(View.GONE);
            holder.time1.setVisibility(View.GONE);
            holder.imagetime1.setText(chats.getTime());
            // holder.cardimagemessage.setVisibility(View.VISIBLE);
            holder.imagemessage.setVisibility(View.VISIBLE);
            holder.musiccard.setVisibility(View.GONE);

            Bitmap bitmap = ImageViewHelperCorner.imagefromDrawable(mcontext,chats.getBitmap());
            holder.imagemessage.setImageBitmap(bitmap);

//            Picasso.with(mcontext).load(chats.getMessage()).into(holder.imagemessage);


        }

        else if("link".equals(messagetype)){
            //holder.cardimagemessage.setVisibility(View.GONE);
            holder.imagemessage.setVisibility(View.GONE);
            holder.time1.setText(chats.getTime());
            holder.imagetime1.setVisibility(View.GONE);
            holder.un.setVisibility(View.VISIBLE);
            holder.un.setText(chats.getMessage());
            holder.musiccard.setVisibility(View.GONE);
            SpannableString ss=new SpannableString(chats.getMessage());

            ClickableSpan clickableSpan=new ClickableSpan() {
                @Override
                public void onClick(View view) {
                }
            };

            holder.un.setBackgroundResource(R.drawable.chatdisplaylinkandurl);

            ss.setSpan(clickableSpan,0,holder.un.getText().toString().length()-0,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.un.setText(ss);

            holder.un.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setData(Uri.parse("email"));
                    String s[] = {holder.un.getText().toString()};
                    i.putExtra(Intent.EXTRA_EMAIL, s);
                    i.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(i, "Launch Email");
                    mcontext.startActivity(chooser);
                }
            });

        }
        else if("url".equals(messagetype))
        {

        //    holder.cardimagemessage.setVisibility(View.GONE);
            holder.imagemessage.setVisibility(View.GONE);
            holder.un.setVisibility(View.VISIBLE);
            holder.time1.setText(chats.getTime());
            holder.un.setText(chats.getMessage());
            holder.un.setTextColor(Color.WHITE);
            holder.imagetime1.setVisibility(View.GONE);
            holder.musiccard.setVisibility(View.GONE);
//            holder.imageseentick.setVisibility(View.GONE);
      //      holder.imageseen.setVisibility(View.GONE);

          //  holder.un.setBackgroundResource(R.drawable.chatdisplaylinkandurl);
//            holder.un.setTextColor(Color.parseColor("#1DA1F2"));

            SpannableString ss=new SpannableString(chats.getMessage());

            ClickableSpan clickableSpan=new ClickableSpan() {
                @Override
                public void onClick(View view) {
                   // Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(chats.getMessage()));
                    //mcontext.startActivity(i);
                }
            };

            ss.setSpan(clickableSpan,0,holder.un.getText().toString().length()-0,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.un.setText(ss);

            holder.un.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url=chats.getMessage();

                    Uri webpage = Uri.parse(url);

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        webpage = Uri.parse("http://" + url);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    mcontext.startActivity(intent);
                }
            });

        }

        else if("audio".equals(messagetype))
        {
            holder.un.setVisibility(View.GONE);
          //  holder.cardimagemessage.setVisibility(View.GONE);
            holder.imagemessage.setVisibility(View.GONE);
            holder.musiccard.setVisibility(View.VISIBLE);
            holder.slidingtext.setText(chats.getSongname());
            holder.slidingtext.setSelected(true);
            holder.imagetime1.setVisibility(View.GONE);
            holder.time1.setVisibility(View.GONE);



            mediaPlayer = new MediaPlayer();

             handler=new Handler();

            holder.imageplaypause1.setVisibility(View.GONE);

            holder.playerseekbar.setMax(100);


            holder.playerseekbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    if(mediaPlayer.isPlaying()){
                        SeekBar seekBar=(SeekBar)view;
                        int playposition=(mediaPlayer.getDuration()/100)*seekBar.getProgress();
                        mediaPlayer.seekTo(playposition);
                    }
                    return false;
                }
            });


            holder.imageplaypause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String url=chats.getMessage();
                    if(url!=null) {

                        try {
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.prepare();
                            realtimelength=mediaPlayer.getDuration();



                          /*  mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(final MediaPlayer mediaPlayer)
                                {*/
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                holder.textcurrenttime.setText("0:00");

                                updateseekbar(holder.playerseekbar,realtimelength,holder.texttotalduration,holder.textcurrenttime);

                                holder.imageplaypause.setVisibility(View.GONE);
                                holder.imageplaypause1.setVisibility(View.VISIBLE);
                            } else {
                                mediaPlayer.stop();
                                mediaPlayer.reset();

                                long cp=mediaPlayer.getCurrentPosition();
                                holder.textcurrenttime.setText(millisectotime(cp));

                                holder.playerseekbar.setProgress(0);
                                holder.imageplaypause.setVisibility(View.VISIBLE);
                                holder.imageplaypause1.setVisibility(View.GONE);

                            }

                         //   long duration=mediaPlayer.getDuration();
                          //  holder.texttotalduration.setText(millisectotime(duration));

                          //  updateseekbar(holder.playerseekbar, holder.textcurrenttime);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                      }
                    }


            });



            DatabaseReference mrefx = FirebaseDatabase.getInstance().getReference().child("Automatic_delete_messages").child(firebaseUser.getUid());
            mrefx.keepSynced(true);

            mrefx.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String switchonoff = dataSnapshot.child("TOGGLE").getValue().toString();

                   // Friends friends = dataSnapshot.getValue(Friends.class);

                    if (!TextUtils.isEmpty(switchonoff))
                    {
                        if ("ON".equals(switchonoff)) {
                            whenswitchison(position, chats, holder.seentick, holder.imageenter1, holder.imagemessage, holder.un, holder.imageseentick, holder.audioseentick, holder.time, holder.imagetime, holder.musiccard, user_id);
                        } else {
                            whenswitchisoff(position, chats, holder.seentick, holder.imageenter1, holder.imagemessage, holder.un, holder.imageseentick, holder.audioseentick, holder.time, holder.imagetime, holder.musiccard, user_id,holder.audiotime);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });





            holder.imageplaypause1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                                    mediaPlayer.stop();
                                    holder.textcurrenttime.setText("0:00");
                                    holder.playerseekbar.setProgress(0);
                                    mediaPlayer.reset();


                    //long cp=mediaPlayer.getCurrentPosition();
                    //holder.textcurrenttime.setText(millisectotime(cp));



                    holder.imageplaypause.setVisibility(View.VISIBLE);
                    holder.imageplaypause1.setVisibility(View.GONE);

                }
            });


            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    holder.playerseekbar.setSecondaryProgress(i);
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    holder.textcurrenttime.setText("0:00");

                    holder.playerseekbar.setProgress(0);
                    holder.imageplaypause.setVisibility(View.VISIBLE);
                    holder.imageplaypause1.setVisibility(View.GONE);

                }
            });

        }

    }


    private void whenswitchisoff(final int position, final Chats chats, final ImageView seentick, CircleImageView imageenter1, ImageView imagemessage, TextView un, final ImageView imageseentick, final ImageView audioseentick, final TextView time, final TextView imagetime, final CardView musiccard, String user_id, final TextView audiotime) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference66.child(chats.getPushid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Boolean seenstatus = (Boolean) dataSnapshot.child("seen").getValue();

                if (seenstatus) {
                    if (position == mchat.size() - 1) {
                        if (chats.isSeen()) {
                            String messagetype = chats.getType();
                            if ("text".equals(messagetype) || "link".equals(messagetype) || "url".equals(messagetype)) {
                                seentick.setVisibility(View.VISIBLE);
                                imageseentick.setVisibility(View.GONE);
                                time.setVisibility(View.VISIBLE);
                                seentick.setImageResource(R.drawable.seen);
                                time.setText(chats.getTime());
                                audioseentick.setVisibility(View.GONE);
                            } else if ("image".equals(messagetype)) {
                                seentick.setVisibility(View.GONE);
                                imageseentick.setImageResource(R.drawable.seen);
                                imagetime.setText(chats.getTime());
                                imagetime.setVisibility(View.VISIBLE);
                                musiccard.setVisibility(View.GONE);
                                audioseentick.setVisibility(View.GONE);

                            } else {

                                seentick.setVisibility(View.GONE);
                                imageseentick.setVisibility(View.GONE);
                                audiotime.setVisibility(View.VISIBLE);
                                audioseentick.setImageResource(R.drawable.seen);
                                audiotime.setText(chats.getTime());
                            }
                        } else {
                            String messagetype = chats.getType();
                            if ("text".equals(messagetype) || "link".equals(messagetype) || "url".equals(messagetype)) {
                                seentick.setVisibility(View.VISIBLE);
                                imageseentick.setVisibility(View.GONE);
                                seentick.setImageResource(R.drawable.delivered);
                                time.setText(chats.getTime());
                                time.setVisibility(View.VISIBLE);
                                audioseentick.setVisibility(View.GONE);

                            } else if ("image".equals(messagetype)) {
                                seentick.setVisibility(View.GONE);
                                imagetime.setText(chats.getTime());
                                imagetime.setVisibility(View.VISIBLE);
                                musiccard.setVisibility(View.GONE);
                                imageseentick.setImageResource(R.drawable.delivered);
                                audioseentick.setVisibility(View.GONE);

                            } else {

                                audioseentick.setImageResource(R.drawable.delivered);
                                audiotime.setText(chats.getTime());
                                seentick.setVisibility(View.GONE);
                                audiotime.setVisibility(View.VISIBLE);
                                imageseentick.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        String messagetype = chats.getType();
                        if ("text".equals(messagetype) || "url".equals(messagetype) || "link".equals(messagetype)) {
                            seentick.setVisibility(View.GONE);
                            time.setText(chats.getTime());
                            time.setVisibility(View.VISIBLE);
                            seentick.setImageResource(R.drawable.delivered);
                            imageseentick.setVisibility(View.GONE);
                            audioseentick.setVisibility(View.GONE);
                        } else if ("image".equals(messagetype)) {
                            imageseentick.setVisibility(View.GONE);
                            imagetime.setText(chats.getTime());
                            imagetime.setVisibility(View.VISIBLE);
                            seentick.setVisibility(View.GONE);
                            audioseentick.setVisibility(View.GONE);
                        } else {
                            imageseentick.setVisibility(View.GONE);
                            imagetime.setVisibility(View.GONE);
                            seentick.setVisibility(View.GONE);
                            audiotime.setText(chats.getTime());
                            audiotime.setVisibility(View.VISIBLE);
                            musiccard.setVisibility(View.GONE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        private void whenswitchison(final int position, final Chats chats, final ImageView seentick, CircleImageView imageenter1, ImageView imagemessage, TextView un, final ImageView imageseentick, final ImageView audioseentick, final TextView time, final TextView imagetime, final CardView musiccard, final String user_id) {


        reference66 = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference66.keepSynced(true);

        chatseentext = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatseentext.keepSynced(true);


        reference66.child(chats.getPushid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Boolean seenstatus = (Boolean) dataSnapshot.child("seen").getValue();


                if (seenstatus != null) {

                    if (position == mchat.size() - 1) {
                        if (seenstatus) {
                            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                        } else {
                            String messagetype = chats.getType();
                            if ("text".equals(messagetype) || "url".equals(messagetype) || "link".equals(messagetype)) {

                                imageseentick.setVisibility(View.INVISIBLE);
                                seentick.setImageResource(R.drawable.delivered);
                                time.setText(chats.getTime());
                                musiccard.setVisibility(View.GONE);
                                audioseentick.setVisibility(View.INVISIBLE);

                            } else if ("image".equals(messagetype)) {
                                imageseentick.setVisibility(View.VISIBLE);
                                //holder.imageseen.setVisibility(View.VISIBLE);
                                seentick.setVisibility(View.INVISIBLE);
                                musiccard.setVisibility(View.GONE);
                                imagetime.setText(chats.getTime());
                                imageseentick.setImageResource(R.drawable.delivered);
                                audioseentick.setVisibility(View.INVISIBLE);

                            } else {
                                imageseentick.setVisibility(View.INVISIBLE);
                                seentick.setVisibility(View.INVISIBLE);
                                musiccard.setVisibility(View.VISIBLE);
                                imageseentick.setVisibility(View.INVISIBLE);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void updateseekbar(final SeekBar playerseekbar, final int realtimelength, final TextView texttotalduration, final TextView textcurrenttime)
    {
        playerseekbar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
        if(mediaPlayer.isPlaying()){
            updater=new Runnable() {
                @Override
                public void run() {
                     updateseekbar(playerseekbar, realtimelength, texttotalduration, textcurrenttime);
                     ChatpadAdapter.this.realtimelength -=1000;
                     texttotalduration.setText(millisectotime(realtimelength));
                     textcurrenttime.setText(millisectotime(mediaPlayer.getCurrentPosition()));
                    // textcurrenttime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(ChatpadAdapter.this.realtimelength),TimeUnit.MILLISECONDS.toSeconds(ChatpadAdapter.this.realtimelength)- TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ChatpadAdapter.this.realtimelength))));
                }
            };
            handler.postDelayed(updater,1000);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }




    public String millisectotime(long duration)
    {
       String timestring="";
       String secondstring="";

       int hrs=(int)(duration/(1000*60*60));
        int min=(int)(duration%(1000*60*60))/(1000*60);
        int sec=(int)((duration/(1000*60*60))%(1000*60)/1000);

        if(hrs>0){
            timestring=hrs+":";
        }
        if(sec<10){
            secondstring="0"+sec;
        }
        else{
            secondstring=""+sec;
        }
        timestring=timestring+min+":"+secondstring;
        return timestring;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView un,date,time1,imagetime1;
        public TextView time, imagetime,audiotime;
        public CircleImageView imageenter1;
        public ImageView imagemessage,seentick,imageseentick,audioseentick;
        public CardView musiccard,cardimagemessage;


        public ImageView imageplaypause,imageplaypause1;
        public TextView texttotalduration,slidingtext;
        public SeekBar playerseekbar;
        public TextView textcurrenttime;


        public ViewHolder(View itemView) {
            super(itemView);
            time1=itemView.findViewById(R.id.time1);
            imagetime1=itemView.findViewById(R.id.imagetime1);
            un = itemView.findViewById(R.id.un);
            date=itemView.findViewById(R.id.date);
            imageenter1 = itemView.findViewById(R.id.imageenter1);
            time = itemView.findViewById(R.id.time);
            imagetime = itemView.findViewById(R.id.imagetime);
            audiotime = itemView.findViewById(R.id.audiotime);
            imagemessage = itemView.findViewById(R.id.imagemessage);
            seentick = itemView.findViewById(R.id.seentick);
            imageseentick = itemView.findViewById(R.id.imageseentick);
            audioseentick = itemView.findViewById(R.id.audioseentick);
            musiccard = itemView.findViewById(R.id.musiccard);

            imageplaypause=itemView.findViewById(R.id.imageplaypause);
            imageplaypause1=itemView.findViewById(R.id.imageplaypause1);
            textcurrenttime=itemView.findViewById(R.id.textcurrenttime);
            texttotalduration=itemView.findViewById(R.id.texttotalduration);
            playerseekbar=itemView.findViewById(R.id.playerseekbar);
            slidingtext=itemView.findViewById(R.id.slidingtext);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        if (firebaseUser.getUid().equals(mchat.get(position).getSender())) {

            return MSG_RIGHT;

        } else {
            return MSG_LEFT;
        }


    }

    private void dateforgroupchat(String user_id, final TextView date11)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String[] date = new String[1];
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");

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
                    date11.setText("Today");
                }
                else if(s2.equals(date[0]))
                {
                    date11.setText("Yesterday");
                }
                else{
                    date11.setText(date[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
