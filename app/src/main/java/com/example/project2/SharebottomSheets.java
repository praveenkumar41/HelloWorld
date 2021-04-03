package com.example.project2;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class SharebottomSheets extends RoundedBottomSheetDialogFragment {

    CardView copylink,shareviamessage,sharepostvia,shareviastory;
    String imageurl;
    TextView d;
    Bitmap bitmap;
    BitmapDrawable bd;
    String name_id,desc,image_id;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sharebottomsheet, container, false);

        final String postid=getArguments().getString("postid");
        final String publisherid=getArguments().getString("publisherid");
    //    final BitmapDrawable bitmapdrawable= (BitmapDrawable) getArguments().getSerializable("bitmapdrawable");
        final String text=getArguments().getString("desc");
        bitmap=getArguments().getParcelable("bit");
        final String flag=getArguments().getString("flag");
        final String uri=getArguments().getString("uri");

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference("users").child(publisherid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_id=dataSnapshot.child("USERNAME").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference mref11= FirebaseDatabase.getInstance().getReference("posts").child(postid);
        mref11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                desc=dataSnapshot.child("description").getValue(String.class);
                image_id=dataSnapshot.child("imageurl").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        copylink=view.findViewById(R.id.copylink);
        shareviamessage=view.findViewById(R.id.shareviamessage);
        sharepostvia=view.findViewById(R.id.sharepostvia);
        shareviastory=view.findViewById(R.id.shareviastory);

        copylink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createlink(view,name_id,desc,image_id,postid,publisherid);
                dismiss();
            }
        });


        sharepostvia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               if("true".equals(flag))
               {
                   ShareImageandText(uri,text);
               }
               else
               {
                   shareTextonly(text);
               }
                dismiss();
            }
        });






        shareviastory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(),Alltype_Story.class);
                intent.putExtra("postid",postid);
                intent.putExtra("set","true");
                startActivity(intent);
                dismiss();
            }
        });

        shareviamessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        return view;

    }

    private void createlink(final View view, String name_id, String desc, String image_id, String postid, String publisherid)
    {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://com.example.project2/"))
                .setDomainUriPrefix("https://blueberry.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.e("Main",""+dynamicLink.getUri());

        createreferlink(view,name_id,desc,image_id,postid,publisherid);

    }

    public void createreferlink(final View view, String name_id, String desc, String image_id, String postid, String publisherid)
    {

        String sharelinktext="https://blueberry.page.link/?"+
                "link=http://com.example.project2/postid="+postid+"__"+publisherid+
                "&apn="+view.getContext().getPackageName()+
                "&st="+ name_id +" "+"on Blueberry"+
                "&sd="+desc+
                "&si="+ image_id;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(sharelinktext))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("Main1","shortlink"+shortLink);


                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");

                            view.getContext().startActivity(intent);

                        } else {
                            // Error
                            // ...
                            Log.e("Main1","error");
                        }
                    }
                });

    }

    private void ShareImageandText(String bitmap, String text) {

        String sharebody=""+"\n"+text;
        Uri uri=saveImagetoshare(bitmap);

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,sharebody);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        intent.setType("image/*");
        getContext().startActivity(Intent.createChooser(intent,"Share via"));

    }

    private Uri saveImagetoshare(String bitmap) {

        Uri uri1= Uri.parse(bitmap);

        return uri1;
    }

    private void shareTextonly(String d)
    {
        String sharebody=""+"\n"+d;

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT,sharebody);
        getContext().startActivity(Intent.createChooser(intent,"Share via"));
    }
}