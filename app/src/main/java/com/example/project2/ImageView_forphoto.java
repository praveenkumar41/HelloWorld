package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.zolad.zoominimageview.ZoomInImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageView_forphoto extends AppCompatActivity {

    ZoomInImageView imageviewer;
    CircleImageView save_photo,backintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_forphoto);

        save_photo=findViewById(R.id.save_photo);
        backintent=findViewById(R.id.backintent);

        String uri=getIntent().getStringExtra("uri");
        final String bitmaps=getIntent().getStringExtra("bitmaps");

        Uri s=Uri.parse(uri);
        imageviewer=findViewById(R.id.imageviewer);
        Glide.with(getApplicationContext()).load(s).into(imageviewer);

        backintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


       save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popup=new PopupMenu(getApplicationContext(),save_photo);
                popup.getMenuInflater().inflate(R.menu.save_image,popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId())
                        {
                            case R.id.save:
                                saveimage(bitmaps);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
       });
    }


    private void saveimage(String bitmaps)
    {
        Bitmap bmp=StringToBitMap(bitmaps);

       // imageviewer.buildDrawingCache();
       // Bitmap bmp = imageviewer.getDrawingCache();

        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File file = new File(storageLoc, System.currentTimeMillis()+ ".jpg");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            scanFile(getApplicationContext(), Uri.fromFile(file));

            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

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
