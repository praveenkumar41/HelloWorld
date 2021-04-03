package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Newsignin.Getting_Username;

import java.util.List;

public class Suggestion_list_Adapter extends RecyclerView.Adapter<Suggestion_list_Adapter.MyViewHolder> {

    private List<String> list11;
    AdapterListener listener;

    public Suggestion_list_Adapter(List<String> list12, Getting_Username getting_username)
    {
        this.list11=list12;
        this.listener=getting_username;
    }

    @NonNull
    @Override
    public Suggestion_list_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_list_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.text.setText(list11.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.applycodes(list11.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list11.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text=itemView.findViewById(R.id.text);
        }
    }


    public interface AdapterListener
    {
        void applycodes(String codes);
    }

}
