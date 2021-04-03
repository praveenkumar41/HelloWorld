package com.example.project2;

import com.example.project2.Notification.MyResponse;
import com.example.project2.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-s99kPs:APA91bEtVY3NrEV-Nc5gSI-yHAu2kx8MTq5nbXgnILs3ltfjPc7qveQ253wBD1rzdFn0yWy0pGt4JunM1qdgdHy_Blk87jVrNYc7VJ76v5mGG-ZA1N4k3NFr5p38qgibDkPePD9Ar2gW"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
