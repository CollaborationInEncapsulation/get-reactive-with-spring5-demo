package com.example.service.impl;

import com.example.controller.vm.MessageVM;
import com.example.service.MessageService;
import com.example.service.UIService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import reactor.core.publisher.Flux;

@EnableBinding(UIService.class)
public class DefaultMessageService implements MessageService {

    private Flux<MessageVM> messageInput = Flux.empty();

    @Override
    public Flux<MessageVM> messageStream() {
        return messageInput;
    }

    @StreamListener
    public void setMessageInput(@Input(UIService.UI_INPUT) Flux<MessageVM> messageInput) {
        this.messageInput = messageInput.replay(30).autoConnect(0);
    }
}
