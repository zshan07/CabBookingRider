package com.softup.utkarsh.androidriderapp.Remote;

import com.softup.utkarsh.androidriderapp.Model.FCMResponse;
import com.softup.utkarsh.androidriderapp.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAABQj5y1g:APA91bHqJh3a7I3S2IBTKJEgtKoTPpRFvzlD2rnhnSJsdwIECjfJJq93LQ19_B6vs80QnJUHRY2b6xDhcikP3zHR8fEdVoBkzGIPGI262eKX1R862e0l631gIT9rGm1w_0gOLFGJ6rp9"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
