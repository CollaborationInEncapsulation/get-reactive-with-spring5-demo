package com.example.service;

import com.example.controller.vm.MessageVM;
import com.example.harness.Assertions;
import com.example.harness.ChatResponseFactory;
import com.example.repository.MessageRepository;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.DefaultMessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import(DefaultMessageService.class)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private ChatService<MessageResponse> chatClient;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void shouldReturnAndStoreLatestMessagesFromChat() {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(ChatResponseFactory.messages(10));
        List<MessageVM> messages = messageService.latest();

        Assert.assertEquals(messages.size(), 10);
        Assert.assertEquals(messageRepository.findAll().size(), 10);
        Assertions.assertMessages(messages);
    }

    @Test
    public void shouldReturnAndStoreMessagesFromChatAfterGivenCursor() {
        Mockito.when(chatClient.getMessagesAfter(Mockito.anyString())).thenReturn(ChatResponseFactory.messages(10));
        List<MessageVM> messages = messageService.cursor("qwerty");

        Mockito.verify(chatClient).getMessagesAfter("qwerty");
        Assert.assertEquals(messages.size(), 10);
        Assert.assertEquals(messageRepository.findAll().size(), 10);
        Assertions.assertMessages(messages);
    }

}
