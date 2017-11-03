package com.example.repository.impl;

import com.example.domain.User;
import com.example.harness.ChatResponseFactory;
import com.example.harness.TestUserRepository;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

@RunWith(SpringRunner.class)
@Import(DefaultUserRepository.class)
@DataMongoTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUserRepository testUserRepository;
    @Autowired
    private MessageRepository messageRepository;


    @Before
    public void setUp() {
        testUserRepository.deleteAll();
        messageRepository.deleteAll();
        ChatResponseFactory.insertUsers(testUserRepository);
    }

    @Test
    public void shouldFindExpectedMostActiveUser() {
        ChatResponseFactory.insertMessages(messageRepository);
        Mono<User> mostActiveUsers = userRepository.findMostActive();

        StepVerifier.create(mostActiveUsers)
                .expectSubscription()
                .expectNext(User.of("53307831c3599d1de448e19a", "macpi", "macpi"))
                .expectComplete();
    }

    @Test
    public void shouldFindExpectedMostPopularUser() {
        ChatResponseFactory.insertMessages(messageRepository);
        Mono<User> mostActiveUsers = userRepository.findMostPopular();

        StepVerifier.create(mostActiveUsers)
                .expectSubscription()
                .expectNext(User.of("53316dc47bfc1a000000000f", "oledok", "oledok"))
                .expectComplete();
    }
}
