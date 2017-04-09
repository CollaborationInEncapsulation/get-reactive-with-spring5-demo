package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.impl.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Service
public class DefaultStatisticService implements StatisticService {
    public static final UserVM EMPTY_USER = new UserVM("", "");
    private final UserRepository userRepository;
    private final MessageBroker messageBroker;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository, MessageBroker messageBroker) {
        this.userRepository = userRepository;
        this.messageBroker = messageBroker;
    }

    @Override
    public Flux<UsersStatisticVM> usersStatisticStream() {
        return Flux.merge(
                doGetUserStatistic(),
                messageBroker.channel("statisticChanged").orElse(Flux.empty())
                        .filter(Signal::isOnNext)
                        .map(Signal::get)
                        .flatMap(s -> doGetUserStatistic())
        );
    }

    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Flux<UserVM> topActiveUser = userRepository.findAllOrderedByActivityDesc(PageRequest.of(0, 1))
                .map(UserMapper::toViewModelUnits);

        Flux<UserVM> topMentionedUser = userRepository.findAllOrderedByMentionDesc(PageRequest.of(0, 1))
                .map(UserMapper::toViewModelUnits);

        return Flux.zip(
                topActiveUser.single(EMPTY_USER).otherwiseIfEmpty(Mono.just(EMPTY_USER)),
                topMentionedUser.single(EMPTY_USER).otherwiseIfEmpty(Mono.just(EMPTY_USER)),
                UsersStatisticVM::new
        ).single();
    }
}
