package com.example.service;

import com.example.controller.vm.UsersStatisticVM;
import reactor.core.publisher.Mono;

public interface StatisticService {
    Mono<UsersStatisticVM> getUsersStatistic();
}
