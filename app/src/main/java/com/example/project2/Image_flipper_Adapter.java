package com.example.project2;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Image_flipper_Adapter extends RecyclerView.Adapter<Image_flipper_Adapter.ImageflipperViewHolder>
{
    ArrayList<Uri> imagelist=new ArrayList<>();
    Context mcontext;
    int c=0;
    String remove;

    public Image_flipper_Adapter(Context applicationContext, ArrayList<Uri> imagelist) {

        this.mcontext=applicationContext;

    }

    @NonNull
    @Override
    public ImageflipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.image_flipper_item, parent, false);

        return new Image_flipper_Adapter.ImageflipperViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageflipperViewHolder holder, final int position) {


    }



    @Override
    public int getItemCount() {
        return 0;
    }

    static class ImageflipperViewHolder extends RecyclerView.ViewHolder{


        public ImageflipperViewHolder(@NonNull View itemView) {
            super(itemView);


        }

    }

}
