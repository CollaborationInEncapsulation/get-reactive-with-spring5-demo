package com.example.controller.ws;

import com.example.service.MessageService;
import com.example.service.StatisticService;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Service
@RequestMapping("/api/v1/ws")
public class ChatChannel implements SimpleReactiveWebSocketHandler {

    private final MessageService messageService;
    private final StatisticService statisticService;

    public ChatChannel(MessageService messageService, StatisticService statisticService) {
        this.messageService = messageService;
        this.statisticService = statisticService;
    }

    @Override
    public Publisher<?> handle() {
        return Flux.merge(messageService.latest(), statisticService.usersStatisticStream());
    }
}
