package com.example.service;

import com.example.controller.vm.MessageVM;
import com.example.harness.ChatResponseFactory;
import com.example.repository.MessageRepository;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.DefaultMessageService;
import com.example.service.impl.utils.MessageMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MessageServiceTest {


    private MessageService messageService;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private MessageRepository messageRepository;

    @MockBean
    private ChatService<MessageResponse> chatService;

    @Before
    public void setUp() {
        Mockito.when(chatService.stream()).thenReturn(Flux.fromIterable(ChatResponseFactory.messages(10)));
        messageService = new DefaultMessageService(messageRepository, chatService, context);
    }

    @Test
    public void shouldReturnAndStoreLatestMessagesFromChat() {
        Flux<MessageVM> messages = messageService.latest();

        Flux<Object> objectFlux = Flux
                .create(s -> context.addApplicationListener(s::next))
                .replay(10)
                .autoConnect(0);

        StepVerifier.create(messages)
                .expectSubscription()
                .expectNextSequence(MessageMapper.toViewModelUnits(Flux.fromIterable(ChatResponseFactory.messages(10))).collectList().block())
                .expectComplete()
                .verify();

        StepVerifier
                .create(
                        objectFlux
                )
                .expectNextCount(10)
                .thenCancel()
                .verify();

        StepVerifier
                .create(messageRepository.count())
                .expectNext(10L)
                .expectComplete()
                .verify();
    }
}
