package com.example.service.gitter;

import com.example.harness.ChatResponseFactory;
import com.example.harness.GitterMockServerRule;
import com.example.service.gitter.dto.MessageResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({
        GitterConfiguration.class,
        JacksonAutoConfiguration.class
})
public class GitterClientTest {

    @Autowired
    @Qualifier("ReactorGitterClient")
    private GitterClient gitterClient;

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
    public void shouldExpectRequestWithNoQueryAndResponseWithLatestMessages() {
        Flux<MessageResponse> messages = Flux.from(gitterClient.getMessages(null));

        StepVerifier.create(messages)
                .expectSubscription()
                .expectNextCount(1)
                .thenAwait(Duration.ofMillis(100))
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }
}
