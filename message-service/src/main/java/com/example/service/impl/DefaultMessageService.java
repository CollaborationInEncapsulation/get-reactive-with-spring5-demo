package com.example.service.impl;

import com.example.repository.MessageRepository;
import com.example.service.MessageService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@EnableBinding(MessageService.class)
public class DefaultMessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public DefaultMessageService(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
    }

    @StreamListener
    public void process(
            @Input(MessageService.CHAT_INPUT) Flux<MessageResponse> messageInput,
            @Output(MessageService.STATISTIC_OUTPUT) FluxSender statisticSender,
            @Output(MessageService.UI_OUTPUT) FluxSender uiSender) {
        Flux
                .merge(
                        messageInput
                                .transform(MessageMapper::toDomainUnits)
                                .transform(messageRepository::saveAll)
                                .transform(statisticSender::send),

                        messageInput
                                .transform(MessageMapper::toViewModelUnits)
                                .transform(uiSender::send)
                )
                .subscribe();
    }
}
