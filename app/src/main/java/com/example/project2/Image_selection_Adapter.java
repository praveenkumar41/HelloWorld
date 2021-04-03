package com.example.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Image_selection_Adapter extends RecyclerView.Adapter<Image_selection_Adapter.ImageSelectionViewHolder>
{
    ArrayList<Uri> imagelist=new ArrayList<>();
    Context mcontext;
    int c=0;
    String remove;


    public Image_selection_Adapter(Context applicationContext, ArrayList<Uri> imagelist) {
        this.mcontext=applicationContext;
        this.imagelist=imagelist;
    }

    @NonNull
    @Override
    public ImageSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.image_selection, parent, false);

        return new Image_selection_Adapter.ImageSelectionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageSelectionViewHolder holder, final int position) {

        holder.cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagelist.remove(imagelist.get(position));
                notifyDataSetChanged();
            }
        });

        for(int i=0;i<imagelist.size();i++)
        {
                holder.zoomimage.setImageURI(imagelist.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    static class ImageSelectionViewHolder extends RecyclerView.ViewHolder{

        PinchZoomImageView zoomimage;
        ImageView cancelimage;

        public ImageSelectionViewHolder(@NonNull View itemView) {
            super(itemView);

            cancelimage=itemView.findViewById(R.id.cancelimage);
            zoomimage=itemView.findViewById(R.id.imageview);

        }

    }
}
