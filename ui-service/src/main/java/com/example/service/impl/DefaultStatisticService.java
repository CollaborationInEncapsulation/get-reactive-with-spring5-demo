package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.StatisticService;
import com.example.service.UIService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import reactor.core.publisher.Flux;

@EnableBinding(UIService.class)
public class DefaultStatisticService implements StatisticService {
    private Flux<UsersStatisticVM> statisticInput = Flux.just(new UsersStatisticVM(
            new UserVM("", ""),
            new UserVM("", "")
    ));

    @Override
    public Flux<UsersStatisticVM> usersStatisticStream() {
        return statisticInput;
    }

    @StreamListener
    public void setStatisticInput(@Input(UIService.STATISTIC_INPUT) Flux<UsersStatisticVM> statisticInput) {
        this.statisticInput = Flux
                .merge(
                        statisticInput,
                        this.statisticInput
                )
                .replay(1)
                .autoConnect(0);
    }
}
