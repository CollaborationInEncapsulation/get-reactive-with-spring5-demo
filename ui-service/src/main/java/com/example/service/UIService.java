package com.example.service;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface UIService {
    String UI_INPUT = "ui-service";
    String STATISTIC_INPUT = "statistic-service";


    @Input(UI_INPUT)
    SubscribableChannel uiInput();

    @Input(STATISTIC_INPUT)
    SubscribableChannel statisticInput();
}
