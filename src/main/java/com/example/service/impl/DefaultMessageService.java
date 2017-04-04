package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.repository.MessageRepository;
import com.example.service.ChatClient;
import com.example.service.MessageService;
import com.example.service.gitter.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static com.example.service.impl.utils.MessageMapper.toDomainUnits;
import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@Service
public class DefaultMessageService implements MessageService {
    private final ChatClient<MessageResponse> chatClient;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository, ChatClient<MessageResponse> chatClient) {
        this.chatClient = chatClient;

//        messageRepository.save(toDomainUnits(chatClient.stream()));
    }

    @Override
    public Flux<MessageVM> stream() {
        return toViewModelUnits(chatClient.stream());
    }
}
