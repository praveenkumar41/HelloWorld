package com.example.project2.GroupChat;

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

import com.example.project2.Chats;
import com.example.project2.ImageViewHelperCorner;
import com.example.project2.ImageView_forphoto;
import com.example.project2.R;
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

public class GroupChatpadAdapter extends RecyclerView.Adapter<GroupChatpadAdapter.ViewHolder>
{

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

    public GroupChatpadAdapter(Context mcontext, List<Chats> mchat) {
        this.mcontext = mcontext;
        this.mchat = mchat;
    }

    @NonNull
    @Override
    public GroupChatpadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.single_chatpad1, parent, false);
            return new GroupChatpadAdapter.ViewHolder(view);

        } else
        {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.single_chatpad, parent, false);
            return new GroupChatpadAdapter.ViewHolder(view);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final GroupChatpadAdapter.ViewHolder holder, final int position) {
        final Chats chats = mchat.get(position);


        final String pushkey = chats.getPushid();

        final String user_id = chats.getReceiverid();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        chatref = FirebaseDatabase.getInstance().getReference().child("GroupChats");
        chatref.keepSynced(true);

        chatref1 = FirebaseDatabase.getInstance().getReference().child("GroupChats");
        chatref1.keepSynced(true);

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


        reference = FirebaseDatabase.getInstance().getReference().child("GroupChats");
        reference.keepSynced(true);

        holder.imagemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, ImageView_forphoto.class);
                intent.putExtra("uri",chats.getMessage());
                mcontext.startActivity(intent);

            }
        });



  /*
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
*/

        setuserimage(chats.getSender(),holder.imageenter1);

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
    }

    private void setuserimage(String sender, final CircleImageView imageenter1)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("ID").equalTo(sender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String images=""+ds.child("IMAGEURL").getValue(String.class);

                    Picasso.with(mcontext).load(images).into(imageenter1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
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
}
