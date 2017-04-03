package com.example.service.impl;

import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
public class DefaultStatisticService implements StatisticService {
    private final UserRepository userRepository;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Mono<UsersStatisticVM> getUsersStatistic() {
//        UserVM topActiveUser = userRepository.findAllOrderedByActivityDesc()
//                .map(UserMapper::toViewModelUnits)
//                .getContent()
//                .stream()
//                .findFirst()
//                .orElse(null);
//
//        UserVM topMentionedUser = userRepository.findAllOrderedByMentionDesc(new PageRequest(0, 1))
//                .map(UserMapper::toViewModelUnits)
//                .getContent()
//                .stream()
//                .findFirst()
//                .orElse(null);

//        return new UsersStatisticVM(topActiveUser, topMentionedUser);
        return Mono.empty();
    }

}
