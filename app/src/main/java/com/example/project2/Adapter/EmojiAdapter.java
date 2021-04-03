package com.example.project2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;

import java.util.ArrayList;
import java.util.List;
/*
public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>
{
    Context context;
    List<String> emojiList;
    EmojiAdapterListener listener;

    public EmojiAdapter(Context context, ArrayList<String> emojis, Emoji emoji)
    {
        this.context=context;
        this.emojiList=emojis;
        this.listener=emoji;
    }

    public interface EmojiAdapterListener{
        void onEmojiItemSelected(String emoji);
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemview= LayoutInflater.from(context).inflate(R.layout.emoji_items,parent,false);
        return new EmojiViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.emoji_text_View.setText(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }


    public class EmojiViewHolder extends RecyclerView.ViewHolder
    {
        EmojiconTextView emoji_text_View;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);

            emoji_text_View=itemView.findViewById(R.id.emoji_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEmojiItemSelected(emojiList.get(getAdapterPosition()));
                }
            });

        }
    }

}
*/