package com.example.service.impl;

import com.example.service.ChatService;
import com.example.service.gitter.GitterClient;
import com.example.service.gitter.dto.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GitterService implements ChatService<MessageResponse> {

    private final Flux<MessageResponse> gitterMessageSource;

    @Autowired
    public GitterService(@Qualifier("ReactorGitterClient") GitterClient gitterClient) {
        //TODO: provide direct connection to gitter client with subsequent source publishing and auto-connection at once

        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public Flux<MessageResponse> stream() {
        return gitterMessageSource;
    }
}
