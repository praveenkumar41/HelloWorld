package com.example.project2;

import android.Manifest;
import android.app.Application;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

import static android.app.Activity.RESULT_OK;

public class PostbottomSheets extends RoundedBottomSheetDialogFragment {

    CardView unfollow,notinterested,mutenotification,reportpost,blockpost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.postbottomsheet, container, false);

        final String postid=getArguments().getString("postid");
        final String publisherid=getArguments().getString("publisherid");


        unfollow=view.findViewById(R.id.unfollow);
        notinterested=view.findViewById(R.id.notinterested);
        mutenotification=view.findViewById(R.id.mutenotification);
        reportpost=view.findViewById(R.id.reportpost);
        blockpost=view.findViewById(R.id.blockpost);

        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "unfollow", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        notinterested.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("not_interested_post").child(firebaseUser.getUid()).push();
                mref.keepSynced(true);
                String pushid=mref.getKey();

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("notinterestedby",firebaseUser.getUid());
                hashMap.put("postid",postid);
                hashMap.put("pushid",pushid);
                hashMap.put("postpublisher",publisherid);

                mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {

                            Toast.makeText(view.getContext(), "We will tune your Recommendation", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(view.getContext().getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dismiss();
            }
        });


        mutenotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        reportpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("report_post").child(firebaseUser.getUid()).push();
                mref.keepSynced(true);
                String pushid=mref.getKey();

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("reportedby",firebaseUser.getUid());
                hashMap.put("postid",postid);
                hashMap.put("pushid",pushid);
                hashMap.put("postpublisher",publisherid);

                mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Reported succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Error Reporting..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dismiss();
            }
        });


        blockpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("Friends");
                mref.keepSynced(true);
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                final AlertDialog.Builder alertdialog =new AlertDialog.Builder(view.getRootView().getContext());
                alertdialog.setTitle("Block User");
                alertdialog.setIcon(R.drawable.ic_block_black_24dp);
                alertdialog.setMessage("Do you want to block this user");

                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {


                   /*     mref.child(firebaseUser.getUid()).child(userid).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mref.child(userid).child(firebaseUser.getUid()).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "User Blocked", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        });
*/
                    }
                });

                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertdialog.create();
                alertdialog.show();
                dismiss();
            }
        });

        return view;

    }
}