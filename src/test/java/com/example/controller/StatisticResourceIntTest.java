package com.example.controller;

import com.example.controller.vm.UsersStatisticVM;
import com.example.harness.ChatResponseFactory;
import com.example.harness.GitterMockServerRule;
import com.example.service.gitter.GitterProperties;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({
        SpringBootDependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
public class StatisticResourceIntTest {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private GitterProperties properties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public GitterMockServerRule gitterMockServerRule = new GitterMockServerRule(
            () -> properties,
            Flux
                    .interval(Duration.ofSeconds(2))
                    .map(String::valueOf)
                    .map(i -> Mono.just(ChatResponseFactory.message(i)))
    );


    @After
    public void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "issue", "mention", "message", "user");
    }

    @Test
    @DatabaseSetup("user-statistic.xml")
    public void shouldReturnExpectedJson() throws Exception {
        StepVerifier.create(
                testClient
                        .get()
                        .uri("/api/v1/statistics/users")
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(UsersStatisticVM.class)
                        .getResponseBody())
                .expectSubscription()
                .expectNextMatches(us ->
                        us.getMostActive().getId().equals("53307734c3599d1de448e192")
                                && us.getMostMentioned().getId().equals("53316dc47bfc1a000000000f"))
                .thenCancel()
                .verify();
    }
}
