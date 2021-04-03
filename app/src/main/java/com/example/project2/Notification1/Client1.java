package com.example.project2.Notification1;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client1
{
    private static Retrofit retrofit=null;

    public static Retrofit getClient(String url)
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
