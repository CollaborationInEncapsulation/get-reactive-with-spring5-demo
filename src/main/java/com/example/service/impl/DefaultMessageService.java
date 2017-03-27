package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.repository.MessageRepository;
import com.example.service.ChatService;
import com.example.service.MessageService;
import com.example.service.gitter.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.service.impl.utils.MessageMapper.toDomainUnits;
import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@Service
public class DefaultMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatService<MessageResponse> chatClient;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository, ChatService<MessageResponse> chatClient) {
        this.messageRepository = messageRepository;
        this.chatClient = chatClient;
    }

    @Override
    public List<MessageVM> cursor(String cursor) {
        Iterable<MessageResponse> messages = chatClient.getMessagesAfter(cursor);

        messageRepository.save(toDomainUnits(messages));

        return toViewModelUnits(messages);
    }

    @Override
    public List<MessageVM> latest() {
        return cursor(null);
    }
}
