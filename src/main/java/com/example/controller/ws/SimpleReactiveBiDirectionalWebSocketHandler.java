package com.example.controller.ws;

import org.reactivestreams.Publisher;

public interface SimpleReactiveBiDirectionalWebSocketHandler<T> {

    Publisher<?> handle(Publisher<T> in);
}
