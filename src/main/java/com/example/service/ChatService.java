package com.example.service;

import reactor.core.publisher.Flux;

public interface ChatService<T> {

    Flux<T> stream();
}
