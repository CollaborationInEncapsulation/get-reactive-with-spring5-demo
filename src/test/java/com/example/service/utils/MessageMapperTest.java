package com.example.service.utils;


import com.example.controller.vm.MessageVM;
import com.example.domain.Issue;
import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.service.impl.utils.MessageMapper;
import com.example.utils.ChatResponseFactory;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import static java.util.Collections.singletonList;

public class MessageMapperTest {

    @Test
    public void shouldCorrectlyMapChatModelToViewModel() {
        Assert.assertEquals(
                singletonList(new MessageVM("0", "0", "0", "0", "0", Date.from(Instant.ofEpochSecond(1395748292L)))),
                MessageMapper.toViewModelUnits(ChatResponseFactory.messages(1))
        );
    }

    @Test
    public void shouldReturnNullViewModelOnNullableChatModelList() {
        Assert.assertEquals(
                null,
                MessageMapper.toViewModelUnits(null)
        );
    }

    @Test
    public void shouldReturnCollectionWithNullsViewModelOnCollectionWithNullableChatModels() {
        Assert.assertEquals(
                singletonList(null),
                MessageMapper.toViewModelUnits(singletonList(null))
        );
    }

    @Test
    public void shouldCorrectlyMapChatModelToDomainModel() {
        Assert.assertEquals(
                singletonList(Message.of("0", "0", "0",
                        Date.from(Instant.ofEpochSecond(1395748292L)),
                        User.of("0", "0"), true, 0L, new String[]{"0", "0", "0"},
                        Collections.singleton(Mention.of(Mention.Key.of("0", "0"))),
                        Collections.singleton(Issue.of(0L)))),
                MessageMapper.toDomainUnits(ChatResponseFactory.messages(1))
        );
    }

    @Test
    public void shouldReturnNullDomainModelOnNullableChatModelList() {
        Assert.assertEquals(
                null,
                MessageMapper.toDomainUnits(null)
        );
    }

    @Test
    public void shouldReturnCollectionWithNullsDomainModelOnCollectionWithNullableChatModels() {
        Assert.assertEquals(
                singletonList(null),
                MessageMapper.toDomainUnits(singletonList(null))
        );
    }
}
