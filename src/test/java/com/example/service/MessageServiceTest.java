package com.example.service;

import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.service.gitter.MessageResponse;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private ChatClient<MessageResponse> chatClient;

    @Test
    @ExpectedDatabase("no-mentioned-user-info.xml")
    public void shouldReturnAndStoreLatestMessagesFromChat() {
//        Mockito.when(chatClient.getMessagesAfter(null))
//                .thenReturn(Arrays.asList(
//                MessageResponse.
//                ));
//        List<Message> messages = messageService.latest();
//
//        Assert.assertEquals(messages.size(), 1);
//        messages.stream()
//                .flatMap(m -> m.getMentions().stream())
//                .map(Mention::getUser)
//                .forEach(Assert::assertNull);
    }
}
