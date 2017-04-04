package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.service.ChatClient;
import com.example.service.gitter.MessageResponse;
import com.example.utils.ChatResponseFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.hasItems;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@EnableWebFlux
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageResourceIntTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ChatClient<MessageResponse> chatClient;


    @Test
    public void shouldReturnExpectedJson() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(ChatResponseFactory.messages(10));

        StepVerifier.create(webTestClient.get()
                .uri("/api/v1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageVM.class)
                .returnResult()
                .getResponseBody()
                .cast(MessageVM.class)
                .map(MessageVM::getId))
                .expectNextCount(10)
                .expectRecordedMatches(c -> hasItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9").matches(c))
                .expectComplete();
    }

    @Test
    public void shouldReturnExpectedJsonAfterGivenCursor() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(Mockito.anyString())).thenReturn(ChatResponseFactory.messages(10));

        StepVerifier.create(webTestClient.get()
                .uri(ub -> ub.path("/api/v1/messages").queryParam("cursor", "qwerty").build())
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageVM.class)
                .returnResult()
                .getResponseBody()
                .cast(MessageVM.class)
                .map(MessageVM::getId))
                .expectNextCount(10)
                .expectRecordedMatches(c -> hasItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9").matches(c))
                .expectComplete();

        Mockito.verify(chatClient).getMessagesAfter("qwerty");
    }

    @Test
    public void shouldRespondWithNoContent() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(null);

        webTestClient.get()
                .uri("/api/v1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void shouldHandleExternalExceptions() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenThrow(new RuntimeException("Wrong cursor"));

        webTestClient.get()
                .uri(ub -> ub.path("/api/v1/messages").queryParam("cursor", "qwerty").build())
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .map(String.class, String.class)
                .contains("message", "Wrong cursor");
    }
}
