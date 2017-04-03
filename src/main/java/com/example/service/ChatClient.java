package com.example.service;

import reactor.core.publisher.Flux;

public interface ChatClient<T> {
    Flux<T> stream();
}
