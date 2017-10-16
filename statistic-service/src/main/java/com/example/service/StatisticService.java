package com.example.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StatisticService {

    String STATISTIC_OUTPUT = "statistic-service";
    String MESSAGE_INPUT = "message-service";

    @Input(MESSAGE_INPUT)
    SubscribableChannel messageInput();

    @Output(STATISTIC_OUTPUT)
    MessageChannel statisticOutput();
}
