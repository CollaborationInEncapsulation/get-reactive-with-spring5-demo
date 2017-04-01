package com.example.service.gitter;

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
    public GitterApi gitterApi(ObjectMapper objectMapper) {
        return new Retrofit.Builder()
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
}
