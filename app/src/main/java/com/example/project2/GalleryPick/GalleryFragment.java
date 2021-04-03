package com.example.project2.GalleryPick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.R;

import java.util.ArrayList;

public class GalleryFragment extends AppCompatActivity {
    private static final String TAG = "GalleryFragment";

    private ArrayList<String> directories;

    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    private static final int NUM_GRID_COLUMNS=3;
    private static final String mappend="file:/";

    ArrayList<String> imgurls;
    String not;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);

        directories=new ArrayList<>();

        not=getIntent().getStringExtra("groupprofile");

      //  galleryImage = (ImageView) findViewById(R.id.galleryImageView);
        gridView = (GridView) findViewById(R.id.gridView);
        directorySpinner = (Spinner) findViewById(R.id.spinnerDirectory);
       // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onCreateView: started.");

        ImageView shareClose = (ImageView) findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                finish();
            }
        });

       init();

    }

    private void init()
    {
        FilePaths filePaths=new FilePaths();
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES)!=null)
        {
            directories=FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        directories.add(filePaths.CAMERA);

/*
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES1)!=null)
        {
            directories=FileSearch.getDirectoryPaths(filePaths.PICTURES1);
        }
        */

        ArrayList<String> folderNames=new ArrayList<>();
        for(int i=0;i<directories.size();i++)
        {
            String s=directories.get(i).replace("/storage/emulated/0/DCIM/","");
            String s1=s.replace("/storage/emulated/0/Pictures/","");
            String s2=s1.replace("/storage/emulated/0/WhatsApp/Media/","");
            folderNames.add(s2);
        }


        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,folderNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);


        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"selected"+directories.get(i));

                setupgridview(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String imguri=imgurls.get(i);

                    Intent resultintent=new Intent();
                    resultintent.putExtra("uri",imguri);

                    setResult(RESULT_OK,resultintent);
                    finish();
            }
        });

    }



    private void setupgridview(String selecteddirectory)
    {
         imgurls=FileSearch.getFilePaths(selecteddirectory);

        int gridwidth=getResources().getDisplayMetrics().widthPixels;
        int imagewidth=gridwidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imagewidth);

        GridImageAdapter adapter=new GridImageAdapter(getApplicationContext(),R.layout.layout_grid_imageview,mappend,imgurls);
        gridView.setAdapter(adapter);
    }



}