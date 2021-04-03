package com.example.project2.GalleryPick;

import android.os.Environment;

public class FilePaths
{
    public String ROOT_DIR= Environment.getExternalStorageDirectory().getPath();

    public String PICTURES=ROOT_DIR+"/Pictures";
    public String CAMERA=ROOT_DIR+"/DCIM/Camera";
    public String PICTURES1=ROOT_DIR+"/WhatsApp/Media";

}
