package com.example.project2;

import com.google.firebase.database.Exclude;

public class Friends_request
{
    public String requested_type;
    public String sender;
    public String reciever;
    public String id;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequested_type() {
        return requested_type;
    }

    public void setRequested_type(String requested_type) {
        this.requested_type = requested_type;
    }

    public Friends_request(String requested_type,String sender,String reciever,String id)
    {
        this.requested_type=requested_type;
        this.sender=sender;
        this.reciever=reciever;
        this.id=id;
    }


    public Friends_request(){

    }
}
