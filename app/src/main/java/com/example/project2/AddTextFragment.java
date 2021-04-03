package com.example.project2;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.project2.Interface.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener
{

    int colorSelected= Color.parseColor("#000000");

    AddTextFragmentListener listener;

    EditText edt_add_text;
    RecyclerView recycler_color;
    Button btn_done;

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    static AddTextFragment instance;

    public static AddTextFragment getInstance()
    {
        if(instance==null)
        {
            instance=new AddTextFragment();
        }
        return instance;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_add_text, container, false);

        edt_add_text=itemview.findViewById(R.id.edt_add_text);
        btn_done=itemview.findViewById(R.id.btn_done);
        recycler_color=itemview.findViewById(R.id.recycler_color);

        recycler_color.setHasFixedSize(true);

        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ColorAdapter colorAdapter=new ColorAdapter(getContext(),this);
        recycler_color.setAdapter(colorAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddTextButtonClick(edt_add_text.getText().toString(),colorSelected);
            }
        });



        return itemview;
    }


    @Override
    public void onColorSelected(int color) {
        colorSelected=color;
    }
}
