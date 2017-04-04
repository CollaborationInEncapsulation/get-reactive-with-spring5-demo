package com.example.service.gitter;

import com.amatkivskiy.gitter.sdk.rx.api.RxGitterStreamingApi;
import com.amatkivskiy.gitter.sdk.rx.client.RxGitterStreamingApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class GitterConfiguration {
    private final static String GITTER_STREAMING_API_ENDPOINT = "https://api.gitter.im";
    private final static String GITTER_STREAMING_API_VERSION = "v1";

    @Bean
    public RxGitterStreamingApiClient gitterApi(ObjectMapper objectMapper) {
        return new RxGitterStreamingApiClient.Builder()
                .withAccountToken("3cd4820adf59b6a7116f99d92f68a1b786895ce7")
                .withApiVersion(GITTER_STREAMING_API_VERSION)
                .build();
    }
}
