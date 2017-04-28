package com.example.service;


import com.example.harness.ChatResponseFactory;
import com.example.service.gitter.GitterClient;
import com.example.service.impl.GitterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GitterServiceTest {
    private GitterService gitterService;
    private GitterClient gitterClient;

    @Before
    public void setUp() {
        gitterClient = Mockito.mock(GitterClient.class);

        when(gitterClient.getMessages(any())).thenReturn(
                Flux
                        .interval(Duration.ofHours(1), VirtualTimeScheduler.getOrSet())
                        .map(String::valueOf)
                        .map(ChatResponseFactory::message)
        );

        gitterService = new GitterService(gitterClient);
    }

    @Test
    public void shouldReturnMessagesFromGitter() {

       //TODO Write a StepVerifier test with VirtualTimeScheduler usage
    }
}
