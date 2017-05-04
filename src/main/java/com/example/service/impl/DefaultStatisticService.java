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

import javax.jnlp.UnavailableServiceException;

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;
    private final Flux<UsersStatisticVM> statisticPublisher;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository, MessageBroker messageBroker) {
        this.userRepository = userRepository;
        this.statisticPublisher = messageBroker
                .channel("statisticChanged")
                .retry(t -> t instanceof UnavailableServiceException)
                .filter(Signal::isOnNext)
                .map(Signal::get)
                .flatMap(s -> doGetUserStatistic())
                .mergeWith(Mono.defer(this::doGetUserStatistic))
                .cache(1);

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

        return Mono.fromDirect(
                Flux.zip(topActiveUser.single(EMPTY_USER), topMentionedUser.single(EMPTY_USER), UsersStatisticVM::new)
        );
    }
}
