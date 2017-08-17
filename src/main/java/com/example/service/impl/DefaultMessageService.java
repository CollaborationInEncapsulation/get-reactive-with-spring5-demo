package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.repository.MessageRepository;
import com.example.service.ChatService;
import com.example.service.MessageService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class DefaultMessageService implements MessageService {
    private final ChatService<MessageResponse> chatClient;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository,
                                 ChatService<MessageResponse> chatClient) {
        this.chatClient = chatClient;

        chatClient
                .stream()
                .transform(MessageMapper::toDomainUnits)
                .transform(messageRepository::saveAll)
                .retryWhen(t -> Flux.range(0, Integer.MAX_VALUE).delaySubscription(Duration.ofMillis(500)))
                .subscribe();
    }

    @Override
    public Flux<MessageVM> latest() {

        return chatClient
                .stream()
                .transform(MessageMapper::toViewModelUnits);
    }
}
