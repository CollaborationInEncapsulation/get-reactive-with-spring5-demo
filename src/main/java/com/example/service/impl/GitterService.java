package com.example.service.impl;

import com.example.service.ChatService;
import com.example.service.gitter.GitterClient;
import com.example.service.gitter.dto.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Service
public class GitterService implements ChatService<MessageResponse> {

    private final GitterClient gitterClient;

    @Autowired
    public GitterService(GitterClient gitterClient) {
        this.gitterClient = gitterClient;
    }

    @Override
    @SneakyThrows
    public Iterable<MessageResponse> getMessagesAfter(String messageId) {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();

        Optional.ofNullable(messageId).ifPresent(v -> query.add("afterId", v));

        return gitterClient.getMessages(query);
    }
}
