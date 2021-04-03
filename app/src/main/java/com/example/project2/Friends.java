package com.example.project2;

import com.google.firebase.database.Exclude;

public class Friends
{
    public String follower;
    public String following;
    public String id;


    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Friends(String follower, String following, String id) {
        this.follower = follower;
        this.following = following;
        this.id = id;
    }

    public Friends()
    {

    }
}
