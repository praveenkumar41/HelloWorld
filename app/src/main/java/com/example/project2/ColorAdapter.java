package com.example.project2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>
{
    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context,ColorAdapterListener listener)
    {
        this.context=context;
        this.colorList=genColorList();
        this.listener=listener;
    }


    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemview= LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
         holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder
    {

        CardView color_section;


        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);

            color_section=itemView.findViewById(R.id.color_section);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList()
    {
        List<Integer> colorList=new ArrayList<>();
        colorList.add(Color.parseColor("#131722"));
        colorList.add(Color.parseColor("#ff545e"));
        colorList.add(Color.parseColor("#57bb82"));
        colorList.add(Color.parseColor("#dbeeff"));
        colorList.add(Color.parseColor("#ba5796"));
        colorList.add(Color.parseColor("#bb349b"));
        colorList.add(Color.parseColor("#6e557e"));
        colorList.add(Color.parseColor("#5e40b2"));
        colorList.add(Color.parseColor("#8051ef"));
        colorList.add(Color.parseColor("#895adc"));
        colorList.add(Color.parseColor("#935da0"));
        colorList.add(Color.parseColor("#7a5e93"));
        colorList.add(Color.parseColor("#6c4475"));
        colorList.add(Color.parseColor("#428fb9"));

        colorList.add(Color.parseColor("#a183b3"));
        colorList.add(Color.parseColor("#210333"));
        colorList.add(Color.parseColor("#99ffcc"));
        colorList.add(Color.parseColor("#b2b2b2"));
        colorList.add(Color.parseColor("#c0fff4"));
        colorList.add(Color.parseColor("#97ffff"));
        colorList.add(Color.parseColor("#ff1493"));
        colorList.add(Color.parseColor("#caff70"));
        colorList.add(Color.parseColor("#dab420"));
        colorList.add(Color.parseColor("#aa5511"));
        colorList.add(Color.parseColor("#314159"));
        colorList.add(Color.parseColor("#420dab"));
        colorList.add(Color.parseColor("#420420"));

        colorList.add(Color.parseColor("#112263"));
        colorList.add(Color.parseColor("#b0bb1e"));
        colorList.add(Color.parseColor("#fff68f"));
        colorList.add(Color.parseColor("#8b3a62"));
        colorList.add(Color.parseColor("#ff1493"));
        colorList.add(Color.parseColor("#b4eeb4"));
        colorList.add(Color.parseColor("#97ffff"));
        return colorList;
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
