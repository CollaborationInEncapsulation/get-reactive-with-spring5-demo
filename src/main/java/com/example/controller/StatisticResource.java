package com.example.controller;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticResource {
    private final StatisticService statisticService;

    @Autowired
    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/users")
    public ResponseEntity<UsersStatisticVM> getUsersStatistic() {
        return ResponseEntity.ok(statisticService.getUsersStatistic());
    }
}
