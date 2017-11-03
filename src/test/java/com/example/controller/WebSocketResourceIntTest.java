package com.example.controller;

import com.example.harness.ChatResponseFactory;
import com.example.harness.GitterMockServerRule;
import com.example.service.gitter.GitterProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@DirtiesContext
public class WebSocketResourceIntTest {

    @Autowired
    private GitterProperties properties;

    @LocalServerPort
    private int port = 0;

    @Rule
    public GitterMockServerRule gitterMockServerRule = new GitterMockServerRule(
            () -> properties,
            Flux
                    .interval(Duration.ofMillis(100))
                    .map(String::valueOf)
                    .map(i -> Mono.just(ChatResponseFactory.message(i)))
    );

    private WebSocketClient testClient;

    @Before
    public void setUp() {
        testClient = new ReactorNettyWebSocketClient();
    }

    @Test
    public void shouldReturnExpectedJson() {
        StepVerifier.<String>create(Flux.create(sink ->
                sink.onCancel(testClient.execute(URI.create("ws://localhost:" + port + "/api/v1/ws"),
                        s -> s.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .doOnNext(sink::next)
                                .doOnCancel(sink::complete)
                                .doOnComplete(sink::complete)
                                .then())
                        .subscribe())))
                .expectSubscription()
                .expectNextCount(10)
                .thenCancel()
                .verify();
    }
}
