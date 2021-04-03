package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.shape.InterpolateOnScrollPositionChangeHelper;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;

public class ViewImageActivity extends AppCompatActivity {

    ImageView imagedownload, imageviewer;
    OutputStream outputStream;
    FileOutputStream ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);


        //ViewGroup.LayoutParams params=imageviewer.getLayoutParams();


        String imageurl = getIntent().getStringExtra("imageurl");

        imagedownload = findViewById(R.id.imagedownload);
        imageviewer = findViewById(R.id.imageviewer);

        Picasso.with(getApplicationContext()).load(imageurl).into(imageviewer);

        scaleimage(imageviewer);

/*
        imagedownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     BitmapDrawable drawable = (BitmapDrawable) imageviewer.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File file = Environment.getExternalStorageDirectory();
                File dir = new File(file.getAbsolutePath() + "/DEMO/");
                dir.mkdir();
                File file1 = new File(dir, System.currentTimeMillis() + ".jpg");
                try {
                    outputStream = new FileOutputStream(file1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                  bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

                //bitmap = ((BitmapDrawable) imageviewer.getDrawable()).getBitmap();

                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                try{
                    outputStream.flush();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    outputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }



            }


        });

    }
*/


        imagedownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) imageviewer.getDrawable();
                Bitmap bitmap = drawable.getBitmap();


                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath() + "/DEMO/");
                dir.mkdir();

                File file = new File(dir, System.currentTimeMillis() + ".jpg");


                try {
                    ss=new FileOutputStream(filepath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,ss);

                try{
                    ss.flush();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    ss.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void scaleimage(ImageView imageviewer) throws NoSuchElementException {
        Bitmap bitmap = null;


        try {
            Drawable drawable = imageviewer.getDrawable();
            bitmap = ((BitmapDrawable) drawable).getBitmap();

        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {

        }

        int width = 0;
        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);

        Log.i("Test", "Original width=" + Integer.toString(width));
        Log.i("Test", "Original height=" + Integer.toString(height));
        Log.i("Test", "bounding=" + Integer.toString(bounding));


        float xscale = ((float) bounding) / width;
        float yscale = ((float) bounding) / height;
        float scale = (xscale <= yscale) ? xscale : yscale;
        Log.i("Test", "xscale=" + Float.toString(xscale));
        Log.i("Test", "yscale=" + Float.toString(yscale));
        Log.i("Test", "scale=" + Float.toString(scale));


        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap ScaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = ScaledBitmap.getWidth();
        height = ScaledBitmap.getHeight();

        BitmapDrawable result = new BitmapDrawable(ScaledBitmap);
        Log.i("Test", "scaled width=" + Integer.toString(width));
        Log.i("Test", "scaled height=" + Integer.toString(height));

        imageviewer.setImageDrawable(result);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageviewer.getLayoutParams();

        params.width = width;
        params.height = height;
        imageviewer.setLayoutParams(params);


        Log.i("Test", "done");

    }

    private int dpToPx(int i) {
        float density=getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)i*density);
    }

}
