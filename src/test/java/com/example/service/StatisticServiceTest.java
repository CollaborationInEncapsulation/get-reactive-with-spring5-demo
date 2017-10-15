package com.example.service;

import com.example.controller.vm.UserVM;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.impl.DefaultStatisticService;
import com.example.service.impl.MessageSavedEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;

@RunWith(SpringRunner.class)
@Import({
        DefaultStatisticService.class,
})
public class StatisticServiceTest {
    private static final Message MESSAGE_STUB = Message.of(
            "1",
            "1",
            "1",
            new Date(),
            User.of("1", "1", "1"),
            true,
            0L,
            new String[]{},
            Collections.emptySet(),
            Collections.emptySet()
    );

    private StatisticService statisticService;

    @Autowired
    private ConfigurableApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        statisticService = new DefaultStatisticService(context, userRepository);
    }

    @Test
    public void shouldReturnStatistic() {

        Mockito.when(userRepository.findMostActive())
                .thenReturn(Mono.just(User.of("1", "1", "1")));
        Mockito.when(userRepository.findMostPopular())
                .thenReturn(Mono.just(User.of("1", "1", "1")));

        StepVerifier.withVirtualTime(() -> statisticService.usersStatisticStream())
                .expectSubscription()
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .expectNoEvent(Duration.ofHours(1))
                .thenAwait(Duration.ofHours(1))
                .then(() -> context.publishEvent(new MessageSavedEvent(MESSAGE_STUB)))
                .then(() -> context.publishEvent(new MessageSavedEvent(MESSAGE_STUB)))
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .expectNoEvent(Duration.ofHours(1))
                .thenAwait(Duration.ofHours(1))
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .thenCancel()
                .verify();
    }
}
