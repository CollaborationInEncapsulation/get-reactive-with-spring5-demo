package com.example.service;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageService {
    String CHAT_INPUT = "chat-service";
    String UI_OUTPUT = "ui-service";
    String STATISTIC_OUTPUT = "message-service";


    @Input(CHAT_INPUT)
    SubscribableChannel chatInput();

    @Output(STATISTIC_OUTPUT)
    MessageChannel statisticOutput();

    @Output(UI_OUTPUT)
    MessageChannel uiOutput();
}
