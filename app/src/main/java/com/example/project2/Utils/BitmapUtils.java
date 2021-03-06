package com.example.project2.Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.project2.Chatfragment;
import com.example.project2.Story_Image_View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {
  /*  public static Bitmap getbitmapFromChatfragment(Context context, Bitmap filename, int width, int height) {
      Chatfragment chatfragment;//=context.getApplicationContext();


        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            bitmap= chatfragment.
            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
       //     return (Bitmap) BitmapFactory.decodeByteArray(bitmap, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
  public static class BitmapScalingHelper
  {
      public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight)
      {
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeResource(res, resId, options);
          options.inJustDecodeBounds = false;

          options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                  dstHeight);

          Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

          return unscaledBitmap;
      }

      public static Bitmap decodeFile(String filePath, int dstWidth, int dstHeight)
      {
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeFile(filePath, options);

          options.inJustDecodeBounds = false;
          options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                  dstHeight);

          Bitmap unscaledBitmap = BitmapFactory.decodeFile(filePath, options);

          return unscaledBitmap;
      }


      public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight)
      {
          final float srcAspect = (float)srcWidth / (float)srcHeight;
          final float dstAspect = (float)dstWidth / (float)dstHeight;

          if (srcAspect > dstAspect)
          {
              return srcWidth / dstWidth;
          }
          else
          {
              return srcHeight / dstHeight;
          }
      }

      public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight)
      {
          Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight());

          Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                  dstWidth, dstHeight);

          Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                  Bitmap.Config.ARGB_8888);

          Canvas canvas = new Canvas(scaledBitmap);
          canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

          return scaledBitmap;
      }

      public static Rect calculateSrcRect(int srcWidth, int srcHeight)
      {
          System.out.print("Scr" + srcWidth + " " + srcHeight);
          return new Rect(0, 0, srcWidth, srcHeight);
      }

      public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight)
      {
          final float srcAspect = (float)srcWidth / (float)srcHeight;
          final float dstAspect = (float)dstWidth / (float)dstHeight;

          if (srcAspect > dstAspect)
          {
              return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
          }
          else
          {
              return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
          }
      }
  }

    public static Bitmap getbitmapFromGallery(Context context, Uri uri, int width, int height) {
        String[] filepathcolumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filepathcolumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filepathcolumn[0]);
        String picturepath = cursor.getString(columnIndex);
        cursor.close();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturepath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturepath, options);
    }

    public static Bitmap applyOverlay(Context context, Bitmap sourceImage, int overlayDrawableResourceId) {
        Bitmap bitmap = null;
        try {
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();
            Resources r = context.getResources();

            Drawable imageAsDrawable = new BitmapDrawable(r, sourceImage);
            Drawable[] layers = new Drawable[2];

            layers[0] = imageAsDrawable;
            layers[1] = new BitmapDrawable(r, BitmapUtils.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height));
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            bitmap = BitmapUtils.drawableToBitmap(layerDrawable);
        } catch (Exception ex) {
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri uri = null;
        String stringurl = null;

        try {
            uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (source != null)
            {
                OutputStream outputStream = cr.openOutputStream(uri);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                } finally {
                    outputStream.close();
                }

                long id = ContentUris.parseId(uri);

                Bitmap minithumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                storeThumbnail(cr, minithumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND);
            }
            else {
                cr.delete(uri,null,null);
                uri=null;
            }

        } catch (IOException e)
        {
            if(uri!=null){
                cr.delete(uri,null,null);
                uri=null;
            }
            e.printStackTrace();
        }
        if(uri!=null){
            stringurl=uri.toString();
        }
        return stringurl;

    }

    private static void storeThumbnail(ContentResolver cr, Bitmap minithumb, long id, float v, float v1, int microKind)
    {
        Matrix matrix=new Matrix();

        float scalex=v/minithumb.getWidth();
        float scaley=v1/minithumb.getHeight();

        matrix.setScale(scalex,scaley);

        Bitmap thumb=Bitmap.createBitmap(minithumb,0,0,minithumb.getWidth(),minithumb.getHeight(),matrix,true);

        ContentValues contentValues=new ContentValues(4);
        contentValues.put(MediaStore.Images.Thumbnails.KIND,microKind);
        contentValues.put(MediaStore.Images.Thumbnails.IMAGE_ID,id);
        contentValues.put(MediaStore.Images.Thumbnails.HEIGHT,v1);
        contentValues.put(MediaStore.Images.Thumbnails.WIDTH,v);

        Uri uri=cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,contentValues);

        try {
            OutputStream outputStream=cr.openOutputStream(uri);
            thumb.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}
