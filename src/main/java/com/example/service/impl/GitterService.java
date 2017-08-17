package com.example.service.impl;

import com.example.service.ChatService;
import com.example.service.gitter.GitterClient;
import com.example.service.gitter.dto.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
public class GitterService implements ChatService<MessageResponse> {

    private final Flux<MessageResponse> gitterMessageSource;

    @Autowired
    public GitterService(GitterClient gitterClient) {
        gitterMessageSource = Flux
                .mergeSequential(
                        gitterClient.getLatestMessages(),
                        gitterClient.getMessagesStream(null)
                )
                .retryWhen(t ->
                        Flux
                                .range(0, Integer.MAX_VALUE)
                                .publishOn(Schedulers.elastic())
                                .delaySubscription(Duration.ofMillis(500)))
                .replay(30)
                .autoConnect(0);
    }

    @Override
    @SneakyThrows
    public Flux<MessageResponse> stream() {
        return gitterMessageSource;
    }
}
