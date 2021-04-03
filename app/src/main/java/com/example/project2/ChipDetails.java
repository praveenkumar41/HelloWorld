package com.example.project2;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.tylersuehr.chips.Chip;

public class ChipDetails extends Chip
{
    public String DOB;
    public String GENDER;
    public String ID;
    public String IMAGEURL;
    public String USERNAME_ID;
    public String USERNAME;
    public String DEVICETOKEN;

    public ChipDetails()
    {
        this.DOB=DOB;
        this.GENDER=GENDER;
        this.ID=ID;
        this.IMAGEURL=IMAGEURL;
        this.USERNAME=USERNAME;
        this.USERNAME_ID=USERNAME_ID;
        this.DEVICETOKEN=DEVICETOKEN;

    }

    @Nullable
    @Override
    public String getId() {
        return ID;
    }

    @NonNull
    @Override
    public String getTitle() {
        return USERNAME;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return USERNAME_ID;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }


    @Exclude
    public String getDOB()
    {
        return DOB;
    }
    @Exclude
    public String getGENDER()
    {
        return GENDER;
    }
    @Exclude
    public String getID()
    {
        return ID;
    }
    @Exclude
    public String getIMAGEURL()
    {
        return IMAGEURL;
    }
    @Exclude
    public String getUSERNAME()
    {
        return USERNAME;
    }
    @Exclude
    public String getUSERNAME_ID()
    {
        return USERNAME_ID;
    }
    @Exclude
    public String getDEVICETOKEN() {
        return DEVICETOKEN;
    }

}
