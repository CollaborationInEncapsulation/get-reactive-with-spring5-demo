package com.example.controller.ws;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.MessageService;
import com.example.service.StatisticService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Service
@RequestMapping("/api/v1/ws")
public class ChatChannel implements WebSocketController {

    private final MessageService messageService;
    private final StatisticService statisticService;

    public ChatChannel(MessageService messageService, StatisticService statisticService) {
        this.messageService = messageService;
        this.statisticService = statisticService;
    }

    @RequestMapping
    public Flux<MessageVM> messageStream() {
        return messageService.latest();
    }

    @RequestMapping
    public Flux<UsersStatisticVM> usersStatisticStream() {
        return statisticService.usersStatisticStream();
    }
}
