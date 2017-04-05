package com.example.service;

import com.example.controller.vm.MessageVM;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.DefaultMessageService;
import com.example.utils.Assertions;
import com.example.utils.ChatResponseFactory;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(DefaultMessageService.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    @Autowired
    private ChatService<MessageResponse> chatClient;

    @Test
    @ExpectedDatabase(value = "chat-messages-expectation.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldReturnAndStoreLatestMessagesFromChat() {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(ChatResponseFactory.messages(10));
        List<MessageVM> messages = messageService.latest();

        Assert.assertEquals(messages.size(), 10);
        Assertions.assertMessages(messages);
    }

    @Test
    @ExpectedDatabase(value = "chat-messages-expectation.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldReturnAndStoreMessagesFromChatAfterGivenCursor() {
        Mockito.when(chatClient.getMessagesAfter(Mockito.anyString())).thenReturn(ChatResponseFactory.messages(10));
        List<MessageVM> messages = messageService.cursor("qwerty");

        Mockito.verify(chatClient).getMessagesAfter("qwerty");
        Assert.assertEquals(messages.size(), 10);
        Assertions.assertMessages(messages);
    }

}
