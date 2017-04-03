package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.MessageService;
import com.example.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import java.util.List;

@Controller
@RequestMapping
public class ChatController {

    private final MessageService messageService;
    private final StatisticService statisticService;

    @Autowired
    public ChatController(MessageService messageService, StatisticService statisticService) {
        this.messageService = messageService;
        this.statisticService = statisticService;
    }

    @GetMapping
    public String index(Model model) {
        final IReactiveDataDriverContextVariable messages =
                new ReactiveDataDriverContextVariable(messageService.stream(), 1);

        model.addAttribute("messages", messages);
//        model.addAttribute("statistic", statistic);

        return "chat";
    }
}
