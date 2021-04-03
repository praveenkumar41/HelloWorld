package com.example.project2;

public class Saved
{
    public String saverid;
    public String postid;
    public String pushid;
    public String save;

    public String getSaverid() {
        return saverid;
    }

    public void setSaverid(String saverid) {
        this.saverid = saverid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public Saved(String saverid, String postid, String pushid, String save) {
        this.saverid = saverid;
        this.postid = postid;
        this.pushid = pushid;
        this.save = save;
    }

    public Saved() {
    }
}
