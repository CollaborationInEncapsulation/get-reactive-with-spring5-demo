package com.example.service.impl;

import com.example.service.ChatClient;
import com.example.service.gitter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class GitterClient implements ChatClient<MessageResponse> {

    private ConnectableFlux<MessageResponse> livePublisher;

    @Autowired
    public GitterClient() {
//        livePublisher = Flux.<MessageResponse>create(s -> gitterApi.getRoomMessagesStream("54f9e9a215522ed4b3dce824")


        livePublisher.connect();
    }

    @Override
    public Flux<MessageResponse> stream() {
        return livePublisher;
    }
}
