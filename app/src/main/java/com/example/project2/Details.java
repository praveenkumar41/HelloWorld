package com.example.project2;

import android.widget.ImageButton;

import com.google.firebase.database.Exclude;

public class Details
{
    public String DOB;
    public String GENDER;
    public String ID;
    public String IMAGEURL;
    public String USERNAME_ID;
    public String USERNAME;
    public String DEVICETOKEN;

    public Details()
    {
        this.DOB=DOB;
        this.GENDER=GENDER;
        this.ID=ID;
        this.IMAGEURL=IMAGEURL;
        this.USERNAME=USERNAME;
        this.USERNAME_ID=USERNAME_ID;
        this.DEVICETOKEN=DEVICETOKEN;

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
