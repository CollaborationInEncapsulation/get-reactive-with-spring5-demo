package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.repository.MessageRepository;
import com.example.service.ChatService;
import com.example.service.MessageService;
import com.example.service.gitter.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DefaultMessageService implements MessageService {
    private final ChatService<MessageResponse> chatClient;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository,
                                 ChatService<MessageResponse> chatClient,
                                 MessageBroker messageBroker) {
        this.chatClient = chatClient;

        //TODO: provide messages saving;
        //TODO: provide channel with name "statisticChanged" and emmit materialized events when messages has been saved;
    }

    @Override
    public Flux<MessageVM> latest() {
        //TODO: provide message emmiting to downstream
        throw new UnsupportedOperationException();
    }
}
