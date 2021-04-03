package com.example.project2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.service.voice.VoiceInteractionSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.project2.Interface.EditImage_Fragment_Listener;


public class Edit_story_Image_Fragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImage_Fragment_Listener listener;
    SeekBar seekBar_brightness,seekBar_constraint,seekBar_saturation;

    public void setListener(EditImage_Fragment_Listener listener) {
        this.listener = listener;
    }

    public Edit_story_Image_Fragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemview= inflater.inflate(R.layout.fragment_edit_story__image_, container, false);

        seekBar_brightness=itemview.findViewById(R.id.seekbar_brightness);
        seekBar_constraint=itemview.findViewById(R.id.seekbar_constraint);
        seekBar_saturation=itemview.findViewById(R.id.seekbar_saturation);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_constraint.setMax(20);
        seekBar_constraint.setProgress(0);

        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);


        seekBar_saturation.setOnSeekBarChangeListener(this);
        seekBar_constraint.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);

        return itemview;
    }




    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(listener!=null){
            if(seekBar.getId()==R.id.seekbar_brightness){
                listener.onBrightnessChanged(i-100);
            }
            else if(seekBar.getId()==R.id.seekbar_constraint){
                i+=10;
                float value= .10f*i;
                listener.onConstraintChanged((int) value);
            }
            else if(seekBar.getId()==R.id.seekbar_saturation) {
                float value= .10f*i;
                listener.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if(listener!=null){
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener!=null){
            listener.onEditStarted();
        }
    }



    public void resetControls(){
        seekBar_brightness.setProgress(100);
        seekBar_constraint.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}
