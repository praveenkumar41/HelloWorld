package com.example.project2;

import android.net.Uri;

public class User_ids
{

    public String id;
    public String pushid;


    public User_ids(String id,String pushid)
    {
        this.pushid=pushid;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public User_ids()
    {
    }
}
