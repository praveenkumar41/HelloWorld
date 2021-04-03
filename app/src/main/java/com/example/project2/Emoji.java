package com.example.project2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*
import com.example.project2.Adapter.EmojiAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ja.burhanrashid52.photoeditor.PhotoEditor;


public class Emoji extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener
{
    RecyclerView recycler_emoji;
    static Emoji instance;

    EmojiFragmentListener listener;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    public static Emoji getInstance(){
        if(instance==null)
        {
            instance=new Emoji();

        }
        return instance;
    }



    public Emoji() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_emoji, container, false);

        recycler_emoji=itemview.findViewById(R.id.recycler_emoji);
        recycler_emoji.setHasFixedSize(true);
        recycler_emoji.setLayoutManager(new GridLayoutManager(getActivity(),5));

        EmojiAdapter adapter=new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
        recycler_emoji.setAdapter(adapter);
        return itemview;
    }



    @Override
    public void onEmojiItemSelected(String emoji) {
        listener.onEmojiSelected(emoji);
    }


}

 */