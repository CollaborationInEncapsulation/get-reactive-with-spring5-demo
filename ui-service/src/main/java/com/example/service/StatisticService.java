package com.example.service;

import com.example.controller.vm.UsersStatisticVM;
import reactor.core.publisher.Flux;

public interface StatisticService {
    Flux<UsersStatisticVM> usersStatisticStream();
}
