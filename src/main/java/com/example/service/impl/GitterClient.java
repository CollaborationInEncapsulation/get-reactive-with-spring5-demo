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
    private final static String GITTER_STREAMING_API_ENDPOINT = "https://api.gitter.im";
    private final static String GITTER_STREAMING_API_VERSION = "v1";

    private final GitterApi gitterApi;

    @Autowired
    public GitterClient(ObjectMapper objectMapper) {
        gitterApi = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer 3cd4820adf59b6a7116f99d92f68a1b786895ce7")
                                .build()))
                        .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(HttpUrl.parse(GITTER_STREAMING_API_ENDPOINT + "/" + GITTER_STREAMING_API_VERSION + "/"))
                .build()
                .create(GitterApi.class);
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
