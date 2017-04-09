package com.example.service.impl;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageBroker {
    private final Map<String, Flux> channels;

    public MessageBroker() {
        channels = new HashMap<>();
    }

    public void createChannel(String name, Flux<? extends Signal> source) {
        channels.put(name, source);
    }

    public Optional<Flux<? extends Signal>> channel(String name) {
        return Optional.ofNullable(channels.get(name));
    }
}
