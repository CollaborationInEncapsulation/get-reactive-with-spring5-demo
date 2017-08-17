package com.example.controller.ws;

import org.reactivestreams.Publisher;

public interface SimpleReactiveWebSocketHandler extends SimpleReactiveBiDirectionalWebSocketHandler<Object> {

    @Override
    default Publisher<?> handle(Publisher<Object> in) {
        return handle();
    }

    Publisher<?> handle();
}
