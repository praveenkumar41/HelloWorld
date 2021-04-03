package com.example.project2;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Image_Posted_Adapter extends RecyclerView.Adapter<Image_Posted_Adapter.ImagePostedViewHolder>
{
    ArrayList<Uri> imagelist=new ArrayList<>();
    Context mcontext;
    int c=0;
    String remove;
    String image1,image2,image3,image4;


    public Image_Posted_Adapter(Context mcontext, String imageurl1, String imageurl2, String imageurl3, String imageurl4) {

        this.mcontext=mcontext;
        this.image1=imageurl1;
        this.image2=imageurl2;
        this.image3=imageurl3;
        this.image4=imageurl4;
    }

    @NonNull
    @Override
    public ImagePostedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.image_posted, parent, false);

        return new Image_Posted_Adapter.ImagePostedViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostedViewHolder holder, final int position) {



        Uri b1=Uri.parse(image1);
        Uri b2=Uri.parse(image2);
        Uri b3=Uri.parse(image1);
        Uri b4=Uri.parse(image2);


        imagelist.add(b1);
        imagelist.add(b2);
        imagelist.add(b3);
        imagelist.add(b4);

        for(int i=0;i<imagelist.size();i++)
        {
            holder.zoomimage.setImageURI(imagelist.get(position));
        }

        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    static class ImagePostedViewHolder extends RecyclerView.ViewHolder{

        PinchZoomImageView zoomimage;

        public ImagePostedViewHolder(@NonNull View itemView) {
            super(itemView);

            zoomimage=itemView.findViewById(R.id.imageview);

        }

    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
