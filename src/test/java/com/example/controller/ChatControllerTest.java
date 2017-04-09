package com.example.controller;

import com.example.harness.ChatResponseFactory;
import com.example.harness.GitterMockServerRule;
import com.example.service.gitter.GitterProperties;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext
public class ChatControllerTest {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private GitterProperties properties;

    @Rule
    public GitterMockServerRule gitterMockServerRule = new GitterMockServerRule(
            () -> properties,
            Flux
                    .interval(Duration.ofMillis(100))
                    .map(String::valueOf)
                    .map(i -> Mono.just(ChatResponseFactory.message(i)))
    );

    @Test
    public void shouldRespondOnRootUrlWithCorrectModel() throws Exception {
        StepVerifier.create(
                testClient
                        .get()
                        .uri("/")
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().valueMatches("Content-Type", MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
                .expectBody(String.class)
                .returnResult()
                .getResponseBody())
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

}
