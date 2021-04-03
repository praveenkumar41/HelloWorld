package com.example.project2;

import android.net.Uri;

public class Stories
{
    public String imageurl;
    public long timestart;
    public long timeend;
    public String storyid;
    public String userid;
    public String type;
    public String description;
    public String bitmap;
    public String postid;
    public String publisher;
    public String cardtype;
    public String options_count;



    public Stories(String imageurl,long timestart,long timeend,String storyid,String userid,String type,String description,String bitmap,String postid,String publisher,String cardtype,String options_count)
    {
        this.imageurl=imageurl;
        this.timestart=timestart;
        this.timeend=timeend;
        this.storyid=storyid;
        this.userid=userid;
        this.type=type;
        this.description=description;
        this.bitmap=bitmap;
        this.postid=postid;
        this.publisher=publisher;
        this.cardtype=cardtype;
        this.options_count=options_count;
    }


    public String getOptions_count() {
        return options_count;
    }

    public void setOptions_count(String options_count) {
        this.options_count = options_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Stories()
    {

    }
}
