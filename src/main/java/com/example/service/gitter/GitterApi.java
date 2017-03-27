package com.example.service.gitter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;


public interface GitterApi {

    @GET("rooms/{roomId}/chatMessages")
    Call<List<MessageResponse>> getRoomMessages(@Path("roomId") String roomId, @QueryMap Map<String, String> options);
}
