package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;


public class HashPresenter extends RecyclerViewPresenter<String> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;
    List<String> alls;

    @SuppressWarnings("WeakerAccess")
    public HashPresenter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable final CharSequence query) {

        alls=new ArrayList<>();
        alls.add("Media");
        alls.add("school");
        alls.add("college");
        alls.add("biggboss");
        alls.add("GTA");
        fetching(query);
    }

    public void fetching(CharSequence query)
    {
        if (TextUtils.isEmpty(query)) {
            adapter.setData(alls);
        }
        else {
            query = query.toString().toLowerCase();
            List<String> list = new ArrayList<>();
            for (String u : alls) {
                if (u.toLowerCase().contains(query))
                {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("HashPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<String> data;

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.fullname);
            }
        }

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.hash, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No Hashtag!");
                holder.root.setOnClickListener(null);
                return;
            }
            final String user11 = data.get(position);
            holder.fullname.setText(user11);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dispatchClick(user11);
                }
            });
        }
    }
}