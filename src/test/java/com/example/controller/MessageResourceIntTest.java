package com.example.controller;

import com.example.service.ChatService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.utils.ChatResponseFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageResourceIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService<MessageResponse> chatClient;


    @Test
    public void shouldReturnExpectedJson() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(ChatResponseFactory.messages(10));

        mockMvc.perform(get("/api/v1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(hasItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")));
    }

    @Test
    public void shouldReturnExpectedJsonAfterGivenCursor() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(Mockito.anyString())).thenReturn(ChatResponseFactory.messages(10));

        mockMvc.perform(get("/api/v1/messages")
                .param("cursor", "qwerty")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(hasItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")));

        Mockito.verify(chatClient).getMessagesAfter("qwerty");
    }

    @Test
    public void shouldRespondWithNoContent() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(null);

        mockMvc.perform(get("/api/v1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldHandleExternalExceptions() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenThrow(new RuntimeException("Wrong cursor"));


        mockMvc.perform(get("/api/v1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Wrong cursor"));
    }
}
