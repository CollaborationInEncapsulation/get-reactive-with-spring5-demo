package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.domain.Message;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.impl.utils.UserMapper;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(StatisticService.class)
public class DefaultStatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;

    public DefaultStatisticService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @StreamListener
    @Output(StatisticService.STATISTIC_OUTPUT)
    public Flux<UsersStatisticVM> usersStatisticStream(@Input(StatisticService.MESSAGE_INPUT) Flux<Message> messagesStream) {
        return messagesStream
                .onBackpressureLatest()
                .flatMap(m -> doGetUserStatistic());
    }

    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Mono<UserVM> topActiveUser = userRepository.findMostActive().map(UserMapper::toViewModelUnits);

        Mono<UserVM> topMentionedUser = userRepository.findMostPopular().map(UserMapper::toViewModelUnits);

        return Mono.zip(
                ar -> new UsersStatisticVM((UserVM) ar[0], (UserVM) ar[1]),
                topActiveUser.defaultIfEmpty(EMPTY_USER),
                topMentionedUser.defaultIfEmpty(EMPTY_USER)
        );
    }
}
