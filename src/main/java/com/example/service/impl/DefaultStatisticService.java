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

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;
    private final Flux<UsersStatisticVM> statisticPublisher;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository, MessageBroker messageBroker) {
        this.userRepository = userRepository;

        //TODO: provide interaction with statistic publisher channel, using dematerialization + retry + mergeWith + cache(1)
        throw new UnsupportedOperationException();
    }

    @Override
    public Flux<UsersStatisticVM> usersStatisticStream() {
        return statisticPublisher;
    }

    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Flux<UserVM> topActiveUser = userRepository.findAllOrderedByActivityDesc(PageRequest.of(0, 1))
                .map(UserMapper::toViewModelUnits);

        Flux<UserVM> topMentionedUser = userRepository.findAllOrderedByMentionDesc(PageRequest.of(0, 1))
                .map(UserMapper::toViewModelUnits);

        //TODO: provide zipping and of two publishers
        //TODO: provide default fallback for top active user and for top mentioned user
        //TODO: provide mono#fromDirect simplification since flux produce just 1 value
        throw new UnsupportedOperationException();
    }
}
