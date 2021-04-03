package com.example.project2;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
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
import com.example.project2.PinchZoomImageView;
import com.example.project2.PostViewer_Activity;
import com.example.project2.Post_Timeago;
import com.example.project2.PostbottomSheets;
import com.example.project2.R;
import com.example.project2.Timeago;
import com.example.project2.VideoPlayingActivity;
import com.example.project2.repost_bottomsheet;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class UserFragmentAdapter extends BaseAdapter {
    Context context;
    ArrayList<MediaObject1> arrayList;


    public UserFragmentAdapter(Context context, ArrayList<MediaObject1> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_fragment_item, parent, false);
        }

        SquareImageView imageView;
        imageView = convertView.findViewById(R.id.image);

        MediaObject1 mediaObject = arrayList.get(position);

        if ("image".equals(mediaObject.getType())) {

            Bitmap bitmap =  StringToBitMap(mediaObject.getBitmap());
            imageView.setImageBitmap(bitmap);

        } else if ("video".equals(mediaObject.getType())) {

            Uri uri = Uri.parse(mediaObject.getImageurl());
            Glide.with(context).asBitmap()
                    .load(uri)
                    .into(imageView);

        }
            return convertView;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

