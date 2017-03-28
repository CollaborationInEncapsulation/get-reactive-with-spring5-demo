package com.example.repository;

import com.example.domain.Mention;
import com.example.domain.Message;
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

import java.util.List;

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
        List<Message> messages = messageRepository.findAllEager();

        Assert.assertEquals(messages.size(), 1);
        messages.stream()
                .flatMap(m -> m.getMentions().stream())
                .map(Mention::getUser)
                .forEach(Assert::assertNull);
    }
}
