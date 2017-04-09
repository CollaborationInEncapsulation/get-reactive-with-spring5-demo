package com.example.service;

import com.example.controller.vm.UserVM;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.impl.DefaultStatisticService;
import com.example.service.impl.MessageBroker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@Import({
        DefaultStatisticService.class,
        MessageBroker.class
})
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private MessageBroker messageBroker;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void shouldReturnStatistic() {
        UnicastProcessor processor = UnicastProcessor.create();
        messageBroker.createChannel("statisticChanged", processor.materialize());

        Mockito.when(userRepository.findAllOrderedByActivityDesc(Mockito.any()))
                .thenReturn(Flux.just(User.of("1", "1")));
        Mockito.when(userRepository.findAllOrderedByMentionDesc(Mockito.any()))
                .thenReturn(Flux.just(User.of("1", "1")));

        StepVerifier.withVirtualTime(() -> statisticService.usersStatisticStream())
                .expectSubscription()
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .expectNoEvent(Duration.ofMillis(1))
                .thenAwait(Duration.ofMillis(1))
                .then(() -> processor.onNext(1))
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .expectNoEvent(Duration.ofMillis(1))
                .thenAwait(Duration.ofMillis(1))
                .then(() -> processor.onNext(1))
                .assertNext(us -> {
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostActive());
                    Assert.assertEquals(new UserVM("1", "1"), us.getMostMentioned());
                })
                .then(() -> processor.onComplete())
                .verifyComplete();
    }
}
