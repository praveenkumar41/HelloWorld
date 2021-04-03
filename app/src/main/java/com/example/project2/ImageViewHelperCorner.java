package com.example.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.http.Url;

public class ImageViewHelperCorner {

    public static Bitmap imagefromDrawable(Context context,String image)
    {
        Bitmap bitmap;

        try {
            byte[] encodebyte=Base64.decode(image,Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(encodebyte,0,encodebyte.length);

        }
        catch (Exception e)
        {
            e.getMessage();
            return null;
        }
        return roundBitmap(bitmap);
    }

    private static Bitmap roundBitmap(Bitmap bitmap) {

        Bitmap roundBitmap=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        Canvas canvas=new Canvas(roundBitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));
        RectF rectF=new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawRoundRect(rectF,10,10,paint);
        return roundBitmap;
    }
}
