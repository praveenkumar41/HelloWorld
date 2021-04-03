package com.example.project2.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Chatfragment;
import com.example.project2.Interface.FilterListFragmentListener;
import com.example.project2.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder>
{
    private List<ThumbnailItem> thumbnailItems;
    private FilterListFragmentListener listener;
    private Context context;
    private int selectedindex=0;

    public ThumbnailAdapter(List<ThumbnailItem> thumbnailItems,FilterListFragmentListener listener,Context context){
        this.thumbnailItems=thumbnailItems;
        this.listener=listener;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;

        View itemview1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_story__image__filters, parent, false);
        MyViewHolder ss=new MyViewHolder(itemview1);
        return ss;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ThumbnailItem thumbnailItem=thumbnailItems.get(position);

        //Bitmap image11=Chatfragment.bundle.getParcelable("bitmap");

        holder.thumbnail2.setImageBitmap(thumbnailItem.image);
        holder.thumbnail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onfilterselected(thumbnailItem.filter);
                selectedindex=position;
                notifyDataSetChanged();
            }
        });

        holder.filter_name.setText(thumbnailItem.filterName);

        if(selectedindex==position){
            holder.filter_name.setTextColor(ContextCompat.getColor(context, R.color.selected_filter));
        }
        else {
            holder.filter_name.setTextColor(ContextCompat.getColor(context,R.color.normal_filter));
        }
    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView thumbnail2;
        TextView filter_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail2=(ImageView) itemView.findViewById(R.id.thumbnail);
            filter_name=itemView.findViewById(R.id.filter_name);
        }
    }

}
