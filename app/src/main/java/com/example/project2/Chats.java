package com.example.project2;

import android.net.Uri;

public class Chats
{
    public String message;
    public String pushid;
    public String receiver;
    public boolean seen;
    public String sender;
    public String time;
    public String type;
    public String receiverid;
    public String date;
    public boolean unreadtoggleon;
    public String songname;
    public String bitmap;

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public boolean isUnreadtoggleon() {
        return unreadtoggleon;
    }

    public void setUnreadtoggleon(boolean unreadtoggleon) {
        this.unreadtoggleon = unreadtoggleon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        receiver = receiver;
    }

    public void setSeen(boolean seen) {
        seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Chats(String message,String pushid,String receiver,boolean seen,String sender,String type,String receiverid,String time,String date,boolean unreadtoggleon,String bitmap)
    {
        this.message=message;
        this.pushid=pushid;
        this.receiver=receiver;
        this.seen=seen;
        this.sender = sender;
        this.type=type;
        this.receiverid=receiverid;
        this.time=time;
        this.date=date;
        this.unreadtoggleon=unreadtoggleon;
        this.bitmap=bitmap;
    }
    public Chats()
    {

    }

}
