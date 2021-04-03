package com.example.project2;


public class Blockedlist
{
    public String friendssince;
    public String firstrequested;
    public String accepted;
    public String id;
    public String acceptedname;
    public String blockeduser;

    public String getBlockeduser() {
        return blockeduser;
    }

    public void setBlockeduser(String blockeduser) {
        this.blockeduser = blockeduser;
    }

    public String getAcceptedname() {
        return acceptedname;
    }

    public void setAcceptedname(String acceptedname) {
        this.acceptedname = acceptedname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstrequested() {
        return firstrequested;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public void setFirstrequested(String firstRequested) {
        firstrequested = firstRequested;
    }


    public String getFriendssince() {
        return friendssince;
    }

    public void setFriendssince(String friendssince) {
        this.friendssince = friendssince;
    }


    public Blockedlist(String acceptedname,String accepted,String friendssince,String firstrequested,String id,String blockeduser)
    {

        this.acceptedname=acceptedname;
        this.accepted=accepted;
        this.friendssince = friendssince;
        this.firstrequested=firstrequested;
        this.id=id;
        this.acceptedname=acceptedname;
        this.blockeduser=blockeduser;
    }
    public Blockedlist()
    {

    }
}
