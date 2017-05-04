package com.example.service;

import com.example.controller.vm.MessageVM;
import com.example.harness.ChatResponseFactory;
import com.example.repository.MessageRepository;
import com.example.repository.impl.DefaultMessageRepository;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.DefaultMessageService;
import com.example.service.impl.MessageBroker;
import com.example.service.impl.utils.MessageMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Before;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({
        DefaultMessageRepository.class,
        MessageBroker.class
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MessageServiceTest {


    private MessageService messageService;

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private MessageRepository messageRepository;

    @MockBean
    @Autowired
    private ChatService<MessageResponse> chatService;

    @Before
    public void setUp() {
        Mockito.when(chatService.stream()).thenReturn(Flux.fromIterable(ChatResponseFactory.messages(10)));
        messageService = new DefaultMessageService(messageRepository, chatService, messageBroker);
    }

    @Test
    @ExpectedDatabase(value = "chat-messages-expectation.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldReturnAndStoreLatestMessagesFromChat() {
        Flux<MessageVM> messages = messageService.latest();

        StepVerifier.create(messages)
                .expectSubscription()
                .expectNextSequence(MessageMapper.toViewModelUnits(Flux.fromIterable(ChatResponseFactory.messages(10))).collectList().block())
                .expectComplete()
                .verify();

        messageBroker.channel("statisticChanged").blockFirst();
    }
}
