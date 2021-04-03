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

import com.example.project2.videostream.MediaObject1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserPresenter extends RecyclerViewPresenter<Details> {

    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;
    List<Details> alls;

    @SuppressWarnings("WeakerAccess")
    public UserPresenter(@NonNull Context context) {
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
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                alls.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Details details = ds.getValue(Details.class);
                    alls.add(details);
                }
                fetching(query);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetching(CharSequence query)
    {
        if (TextUtils.isEmpty(query)) {
            adapter.setData(alls);
        }
        else {
            query = query.toString().toLowerCase();
            List<Details> list = new ArrayList<>();
            for (Details u : alls) {
                if (u.getUSERNAME().toLowerCase().contains(query) ||
                        u.getUSERNAME_ID().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Details> data;

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            private TextView username;
            CircleImageView image;
            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.fullname);
                username = itemView.findViewById(R.id.username);
                image = itemView.findViewById(R.id.image);
            }
        }

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<Details> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No user here!");
                holder.username.setText("Sorry!");
                Picasso.with(getContext()).load(R.drawable.pro).into(holder.image);
                holder.root.setOnClickListener(null);
                return;
            }
            final Details user11 = data.get(position);
            holder.fullname.setText(user11.getUSERNAME());
            holder.username.setText("@" +user11.getUSERNAME_ID());
            Picasso.with(getContext()).load(user11.getIMAGEURL()).placeholder(R.drawable.pro).into(holder.image);
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