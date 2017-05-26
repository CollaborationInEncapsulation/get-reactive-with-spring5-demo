package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.ipc.netty.ByteBufFlux;
import reactor.ipc.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(GitterProperties.class)
public class GitterConfiguration {

    @Bean
    @Primary
    @Qualifier("Spring5GitterClient")
    public GitterClient spring5GitterClient(GitterProperties gitterProperties) {
        return query -> WebClient.create()
                .get()
                .uri(GitterUriBuilder.from(gitterProperties).queryParams(query).build().toUri())
                .header("Authorization", "Bearer " + gitterProperties.getAuth().getToken())
                .exchange()
                .log()
                .retryWhen(fe -> Flux.range(0, 10).delaySubscription(Duration.ofMillis(100)))
                .checkpoint("received")
                .flatMapMany(r -> r.bodyToFlux(MessageResponse.class));
    }

    @Bean
    @Qualifier("ReactorGitterClient")
    public GitterClient reactorGitterClient(GitterProperties gitterProperties,
                                            ObjectMapper mapper) {
        return (query) -> HttpClient.create()
                .get(
                        GitterUriBuilder.from(gitterProperties).queryParams(query).build().toUriString(),
                        r -> r.header("Authorization", "Bearer " + gitterProperties.getAuth().getToken()))
                .map(hcr -> hcr.addHandler(new JsonObjectDecoder()))
                .retry()
                .flatMapMany(hc -> hc.receive().asInputStream())
                .map(is -> {
                    try {
                        return mapper.readValue(is, MessageResponse.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Bean
    @Qualifier("RxJavaGitterClient")
    @SneakyThrows
    public GitterClient rxJavaGitterClient(GitterProperties gitterProperties,
                                           ObjectMapper mapper) {
        UriComponentsBuilder gitterUriBuilder = GitterUriBuilder.from(gitterProperties);
        UriComponents gitterUriComponent = gitterUriBuilder.build();

        SSLContext sslCtx = SSLContext.getDefault();
        SSLEngine sslEngine = sslCtx.createSSLEngine(gitterUriComponent.getHost(), gitterUriComponent.getPort());
        sslEngine.setUseClientMode(true);

        return (query) -> ByteBufFlux.fromInbound(Flux.create(sink -> io.reactivex.netty.protocol.http.client.HttpClient
                .newClient(gitterUriComponent.getHost(), gitterUriComponent.getPort())
                .secure(sslEngine)
                .createGet(gitterUriBuilder.queryParams(query).build().getPath())
                .addHeader("Authorization", "Bearer " + gitterProperties.getAuth().getToken())
                .retry()
                .flatMap(HttpClientResponse::getContent)
                .filter(bb -> bb.capacity() > 2)
                .subscribe(sink::next, sink::error, sink::complete)))
                .asInputStream()
                .map(is -> {
                    try {
                        return mapper.readValue(is, MessageResponse.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
