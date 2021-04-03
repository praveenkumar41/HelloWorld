package com.example.project2;

public class Displaysongs
{
    public String songname;
    public String songurl;
    public String pushid;

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongurl() {
        return songurl;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }


    public Displaysongs(String songname,String songurl,String pushid)
    {

        this.songname=songname;
        this.songurl=songurl;
        this.pushid=pushid;
    }
    public Displaysongs()
    {

    }
}
