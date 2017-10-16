package com.example.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ChatService {

    String CHAT_OUTPUT = "chat-service";

    @Output(CHAT_OUTPUT)
    MessageChannel chatOutput();
}
