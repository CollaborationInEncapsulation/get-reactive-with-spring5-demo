package com.example.service.gitter;

import com.example.harness.ChatResponseFactory;
import com.example.harness.GitterMockServerRule;
import com.example.service.gitter.dto.MessageResponse;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({
        GitterConfiguration.class,
        JacksonAutoConfiguration.class
})
@Ignore
public class GitterClientTest {

    @Autowired
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
        Flux<MessageResponse> messages = Flux.from(gitterClient.getLatestMessages());

        List<MessageResponse> block = messages.collectList().block();
        block.toArray();

        StepVerifier.create(messages)
                .expectSubscription()
                .thenAwait(Duration.ofMillis(3000))
                .expectNextCount(30)
                .expectComplete()
                .verify();
    }


    @Test
    public void shouldExpectRequestWithNoQueryAndResponseWithMessagesStream() {
        Flux<MessageResponse> messages = Flux.from(gitterClient.getMessagesStream(null));

        StepVerifier.create(messages)
                .expectSubscription()
                .expectNextCount(1)
                .thenAwait(Duration.ofMillis(100))
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }
}
