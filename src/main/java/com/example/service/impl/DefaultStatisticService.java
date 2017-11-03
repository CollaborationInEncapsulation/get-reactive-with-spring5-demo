package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.impl.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;
    private final Flux<UsersStatisticVM> statisticPublisher;

    @Autowired
    public DefaultStatisticService(ConfigurableApplicationContext context, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.statisticPublisher = Flux
                .create(
                        s -> context.addApplicationListener((MessageSavedEvent e) -> s.next(e.getSource())),
                        FluxSink.OverflowStrategy.LATEST
                )
                .flatMap(s -> doGetUserStatistic())
                .mergeWith(Mono.defer(this::doGetUserStatistic))
                .cache(1);
    }

    @Override
    public Flux<UsersStatisticVM> usersStatisticStream() {
        return statisticPublisher;
    }

    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Mono<UserVM> topActiveUser = userRepository.findMostActive()
                .map(UserMapper::toViewModelUnits);

        Mono<UserVM> topMentionedUser = userRepository.findMostPopular()
                .map(UserMapper::toViewModelUnits);

        return Mono.zip(
                ar -> new UsersStatisticVM((UserVM) ar[0], (UserVM) ar[1]),
                topActiveUser.transform(this::addResilience),
                topMentionedUser.transform(this::addResilience)
        );
    }

    private Mono<UserVM> addResilience(Mono<UserVM> input) {
        return input
                .timeout(Duration.ofSeconds(2))
                .defaultIfEmpty(EMPTY_USER)
                .onErrorReturn(EMPTY_USER);
    }
}
