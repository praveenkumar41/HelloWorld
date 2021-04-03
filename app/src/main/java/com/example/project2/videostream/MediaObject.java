package com.example.project2.videostream;

public class MediaObject {

    private String date;
    private String videourl;
    private String videoname;
    private String time;
    private String pushid;
    private String count;
    private String thumbnail;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public MediaObject(String date, String videourl, String videoname, String time, String pushid,String count,String thumbnail,String type) {

        this.date = date;
        this.videourl = videourl;
        this.videoname = videoname;
        this.time = time;
        this.pushid = pushid;
        this.count=count;
        this.thumbnail=thumbnail;
        this.type=type;
    }

    public MediaObject() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

}