package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.impl.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UsersStatisticVM getUsersStatistic() {
        UserVM topActiveUser = userRepository.findMostActive()
                .map(UserMapper::toViewModelUnits)
                .orElse(EMPTY_USER);

        UserVM topMentionedUser = userRepository.findMostPopular()
                .map(UserMapper::toViewModelUnits)
                .orElse(EMPTY_USER);

        return new UsersStatisticVM(topActiveUser, topMentionedUser);
    }
}
