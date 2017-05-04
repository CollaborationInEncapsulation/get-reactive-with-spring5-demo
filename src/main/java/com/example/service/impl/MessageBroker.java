package com.example.service.impl;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import javax.jnlp.UnavailableServiceException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageBroker {
    private final Map<String, Flux> channels;

    public MessageBroker() {
        channels = new HashMap<>();
    }

    public void createChannel(String name, Flux<? extends Signal> source) {
        channels.put(name, source);
    }

    @SuppressWarnings("unchecked")
    public Flux<? extends Signal> channel(String name) {
        return Flux.defer(() -> channels.getOrDefault(name, Flux.error(new UnavailableServiceException())));
    }
}
