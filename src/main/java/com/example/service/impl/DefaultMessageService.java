package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.repository.MessageRepository;
import com.example.service.ChatClient;
import com.example.service.MessageService;
import com.example.service.gitter.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.service.impl.utils.MessageMapper.toDomainUnits;
import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@Service
public class DefaultMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final DefaultStatisticService usersStatisticService;
    private final ChatClient<MessageResponse> chatClient;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository,
                                 DefaultStatisticService usersStatisticService, ChatClient<MessageResponse>
                                             chatClient) {
        this.messageRepository = messageRepository;
        this.usersStatisticService = usersStatisticService;
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
