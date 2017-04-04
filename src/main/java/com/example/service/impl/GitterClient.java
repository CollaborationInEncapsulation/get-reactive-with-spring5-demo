package com.example.service.impl;

import com.example.service.ChatClient;
import com.example.service.gitter.GitterApi;
import com.example.service.gitter.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GitterClient implements ChatClient<MessageResponse> {

    private final GitterApi gitterApi;

    @Autowired
    public GitterClient(GitterApi gitterApi) {
        this.gitterApi = gitterApi;
    }

    @Override
    @SneakyThrows
    public Iterable<MessageResponse> getMessagesAfter(String messageId) {
        Map<String, String> query = new HashMap<>();

        Optional.ofNullable(messageId).ifPresent(v -> query.put("afterId", v));

        Response<List<MessageResponse>> response = gitterApi.getRoomMessages("54f9e9a215522ed4b3dce824", query)
                .execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            //TODO replace with Custom Exception
            throw new RuntimeException(response.errorBody().string());
        }
    }
}
