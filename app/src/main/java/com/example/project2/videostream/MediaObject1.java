package com.example.project2.videostream;


public class MediaObject1 {

    private String date;
    private String imageurl;
    private String time;
    private String pushid;
    private String type;
    private String description;
    private String random;
    private String bitmap;
    private String publisher;
    private long ago;
    private String type1;
    private String repost_publisher;
    private String repost_pushid;
    private String tempdate;
    private String perdate;

    public String getTempdate() {
        return tempdate;
    }

    public void setTempdate(String tempdate) {
        this.tempdate = tempdate;
    }

    public String getPerdate() {
        return perdate;
    }

    public void setPerdate(String perdate) {
        this.perdate = perdate;
    }

    public String getRepost_publisher() {
        return repost_publisher;
    }

    public void setRepost_publisher(String repost_publisher) {
        this.repost_publisher = repost_publisher;
    }

    public String getRepost_pushid() {
        return repost_pushid;
    }

    public void setRepost_pushid(String repost_pushid) {
        this.repost_pushid = repost_pushid;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getAgo() {
        return ago;
    }

    public void setAgo(long ago) {
        this.ago = ago;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    public MediaObject1(String date, String imageurl1, String imageurl2, String imageurl3, String imageurl4, String time, String pushid, String type, String description, String random, String bitmap, String publisher, long ago,String type1,String repost_publisher,String repost_pushid,String tempdate,String perdate) {

        this.date = date;
        this.imageurl = imageurl;
        this.time = time;
        this.pushid = pushid;
        this.type=type;
        this.description=description;
        this.random=random;
        this.bitmap=bitmap;
        this.publisher=publisher;
        this.ago=ago;
        this.type1=type1;
        this.repost_publisher=repost_publisher;
        this.repost_pushid=repost_pushid;
        this.tempdate=tempdate;
        this.perdate=perdate;

    }

    public MediaObject1() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}