package com.example.service;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.impl.DefaultStatisticService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@Import(DefaultStatisticService.class)
public class StatisticServiceTest {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    @Autowired
    private StatisticService statisticService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void shouldReturnEmptyStatisticOnEmptyDataBase() {
        Mockito.when(userRepository.findAllOrderedByActivityDesc(Mockito.any()))
                .then(a -> new PageImpl<User>(Collections.emptyList(), (Pageable) a.getArguments()[0], 0));
        Mockito.when(userRepository.findAllOrderedByMentionDesc(Mockito.any()))
                .then(a -> new PageImpl<User>(Collections.emptyList(), (Pageable) a.getArguments()[0], 0));
        UsersStatisticVM usersStatistic = statisticService.getUsersStatistic();

        Assert.assertNotNull(usersStatistic);
        Assert.assertEquals(usersStatistic.getMostActive(), EMPTY_USER);
        Assert.assertEquals(usersStatistic.getMostMentioned(), EMPTY_USER);
    }

    @Test
    public void shouldReturnStatistic() {
        Mockito.when(userRepository.findAllOrderedByActivityDesc(Mockito.any()))
                .then(a -> new PageImpl<>(Collections.singletonList(User.of("1", "1")),
                        (Pageable) a.getArguments()[0], 1));
        Mockito.when(userRepository.findAllOrderedByMentionDesc(Mockito.any()))
                .then(a -> new PageImpl<>(Collections.singletonList(User.of("1", "1")),
                        (Pageable) a.getArguments()[0], 1));
        UsersStatisticVM usersStatistic = statisticService.getUsersStatistic();

        Assert.assertNotNull(usersStatistic);
        Assert.assertEquals(new UserVM("1", "1"), usersStatistic.getMostActive());
        Assert.assertEquals(new UserVM("1", "1"), usersStatistic.getMostMentioned());
    }
}
