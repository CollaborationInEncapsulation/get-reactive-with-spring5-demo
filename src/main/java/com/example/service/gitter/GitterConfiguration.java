package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(GitterProperties.class)
public class GitterConfiguration {

    @Bean
    @Primary
    @Qualifier("Spring5GitterClient")
    public GitterClient spring5GitterClient(GitterProperties gitterProperties) {
        return new GitterClient() {
            @Override
            public Flux<MessageResponse> getMessagesStream(MultiValueMap<String, String> query) {
                return WebClient.create()
                        .get()
                        .uri(GitterUriBuilder.from(gitterProperties.getStream()).queryParams(query).build().toUri())
                        .header("Authorization", "Bearer " + gitterProperties.getStream().getAuth().getToken())
                        .exchange()
                        .retryWhen(fe -> Flux.range(0, Integer.MAX_VALUE).delaySubscription(Duration.ofMillis(100)))
                        .flatMapMany(r -> r.bodyToFlux(MessageResponse.class));
            }

            @Override
            public Flux<MessageResponse> getLatestMessages() {
                return WebClient.create()
                        .get()
                        .uri(GitterUriBuilder.from(gitterProperties.getApi()).build().toUri())
                        .header("Authorization", "Bearer " + gitterProperties.getApi().getAuth().getToken())
                        .exchange()
                        .retryWhen(fe -> Flux.range(0, Integer.MAX_VALUE).delaySubscription(Duration.ofMillis(100)))
                        .flatMapMany(r -> r.bodyToFlux(MessageResponse.class));
            }
        };
    }
}
