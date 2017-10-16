package com.example.service.impl;

import com.example.service.ChatService;
import com.example.service.gitter.GitterClient;
import com.example.service.gitter.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@EnableBinding(ChatService.class)
public class GitterService{

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
                                .delaySubscription(Duration.ofMillis(500)));
    }

    @StreamEmitter
    @Output(ChatService.CHAT_OUTPUT)
    public Flux<MessageResponse> stream() {
        return gitterMessageSource;
    }
}
