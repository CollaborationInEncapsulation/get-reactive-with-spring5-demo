package com.example.controller;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticResource {
    private final StatisticService statisticService;

    @Autowired
    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsersStatisticVM> getUsersStatistic() {
        return statisticService.usersStatisticStream();
    }
}
