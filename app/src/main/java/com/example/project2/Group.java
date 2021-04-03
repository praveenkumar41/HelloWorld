package com.example.project2;

public class Group
{
    String createdby;
    String date;
    String time;
    String groupdesc;
    String groupid;
    String groupimage;
    String grouptitle;

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGroupdesc() {
        return groupdesc;
    }

    public void setGroupdesc(String groupdesc) {
        this.groupdesc = groupdesc;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupimage() {
        return groupimage;
    }

    public void setGroupimage(String groupimage) {
        this.groupimage = groupimage;
    }

    public String getGrouptitle() {
        return grouptitle;
    }

    public void setGrouptitle(String grouptitle) {
        this.grouptitle = grouptitle;
    }

    public Group() {
    }

    public Group(String createdby, String date, String time, String groupdesc, String groupid, String groupimage, String grouptitle) {
        this.createdby = createdby;
        this.date = date;
        this.time = time;
        this.groupdesc = groupdesc;
        this.groupid = groupid;
        this.groupimage = groupimage;
        this.grouptitle = grouptitle;
    }
}
