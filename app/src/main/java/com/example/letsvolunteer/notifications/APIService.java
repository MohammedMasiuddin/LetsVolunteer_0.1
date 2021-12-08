package com.example.letsvolunteer.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA-hn2aLk:APA91bEOfp5Gq87X9I4TNMD-IxXGTPtrHkAn016CV_9PgBmycjlQkfs9uIFAMM5rR_PWffnVU1VEnytE3u6YZMPf5xTCwSIldDKYvC8vWdzxa33LJe9B5k6wuLxEWKbxHjsWnvBBNlLr"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
