package com.example.repository;

import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.repository.impl.DefaultMessageRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(DefaultMessageRepository.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DatabaseSetup("no-mentioned-user-info.xml")
    public void userShouldBeNullInMentions() {
        Flux<Message> messages = messageRepository.findAllEager();

        StepVerifier.create(messages)
                .expectSubscription()
                .expectNextMatches(m -> m.getMentions().stream().map(Mention::getUser).allMatch(Objects::isNull))
                .expectComplete()
                .verify();
    }
}
