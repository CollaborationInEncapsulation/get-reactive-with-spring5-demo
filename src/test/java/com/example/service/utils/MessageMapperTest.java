package com.example.service.utils;


import com.example.controller.vm.MessageVM;
import com.example.domain.Issue;
import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.harness.ChatResponseFactory;
import com.example.service.impl.utils.MessageMapper;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

public class MessageMapperTest {

    @Test
    public void shouldCorrectlyMapChatModelToViewModel() {
        StepVerifier.create(MessageMapper.toViewModelUnits(Flux.fromIterable(ChatResponseFactory.messages(1))))
                .expectSubscription()
                .assertNext((m) -> Assert.assertEquals(
                        new MessageVM("0", "0", "0", "0", "0", Date.from(Instant.ofEpochSecond(1395748292L))),
                        m))
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldReturnNullViewModelOnNullableChatModelList() {
        Assert.assertEquals(
                null,
                MessageMapper.toViewModelUnits(null)
        );
    }

    @Test
    public void shouldCorrectlyMapChatModelToDomainModel() {
        StepVerifier.create(MessageMapper.toDomainUnits(Flux.fromIterable(ChatResponseFactory.messages(1))))
                .expectSubscription()
                .assertNext((m) -> Assert.assertEquals(
                        Message.of("0", "0", "0",
                                Date.from(Instant.ofEpochSecond(1395748292L)),
                                User.of("0", "0", "0"), true, 0L, new String[]{"0", "0", "0"},
                                Collections.singleton(Mention.of("0", "0")),
                                Collections.singleton(Issue.of(0L))),
                        m))
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldReturnNullDomainModelOnNullableChatModelList() {
        Assert.assertEquals(
                null,
                MessageMapper.toDomainUnits(null)
        );
    }
}
