package com.example.project2;


import android.app.Application;
import android.content.Context;

public class Post_Timeago extends Application
{
    private static final int SECOND_MILLIS=1000;
    private static final int MINUTE_MILLIS=60*SECOND_MILLIS;
    private static final int HOUR_MILLIS=60*MINUTE_MILLIS;
    private static final int DAY_MILLIS=24*HOUR_MILLIS;

    public static String Post_Timeago(long time, Context ctx)
    {
        if(time < 1000000000000L)
        {
            time*=1000;
        }

        long now=System.currentTimeMillis();
        if(time>now || time <=0)
        {
            return null;
        }

        final long diff=now-time;
        if(diff<MINUTE_MILLIS){
            return "now";
        }
        else if(diff<2*MINUTE_MILLIS){
            return "1m";
        }
        else if(diff<50*MINUTE_MILLIS){
            return diff/MINUTE_MILLIS+"m";
        }
        else if(diff<90*MINUTE_MILLIS){
            return "1hr";
        }
        else if(diff<24*HOUR_MILLIS){
            return diff/HOUR_MILLIS+"hr";
        }
        else if(diff<48*HOUR_MILLIS){
            return "1d";
        }
        else {
            return diff/DAY_MILLIS+"d";
        }
    }
}
