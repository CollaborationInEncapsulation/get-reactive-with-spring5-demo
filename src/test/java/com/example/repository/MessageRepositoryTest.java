package com.example.repository;

import com.example.domain.Mention;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import reactor.test.StepVerifier;

import java.util.Objects;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DatabaseSetup("no-mentioned-user-info.xml")
    public void userShouldBeNullInMentions() {
        StepVerifier.create(messageRepository.findAllEager())
                .expectNextCount(1)
                .expectNextMatches(m -> Objects.isNull(m.getUser()))
                .expectComplete();
    }
}
