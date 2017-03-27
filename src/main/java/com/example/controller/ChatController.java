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
        List<MessageVM> messages = messageService.latest();
        UsersStatisticVM statistic = statisticService.getUsersStatistic();

        model.addAttribute("messages", messages);
        model.addAttribute("statistic", statistic);

        if (messages != null && messages.size() > 0) {
            model.addAttribute("cursor", messages.get(messages.size() - 1).getId());
        }

        return "chat";
    }
}
