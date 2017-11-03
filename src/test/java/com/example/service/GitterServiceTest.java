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

        when(gitterClient.getMessagesStream(any())).thenReturn(
                Flux
                        .interval(Duration.ofHours(1), VirtualTimeScheduler.getOrSet())
                        .map(String::valueOf)
                        .map(ChatResponseFactory::message)
        );

        when(gitterClient.getLatestMessages()).thenReturn(Flux.empty());

        gitterService = new GitterService(gitterClient);
    }

    @Test
    public void shouldReturnMessagesFromGitter() {

        StepVerifier.withVirtualTime(() -> gitterService.stream())
                .expectSubscription()
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }
}
