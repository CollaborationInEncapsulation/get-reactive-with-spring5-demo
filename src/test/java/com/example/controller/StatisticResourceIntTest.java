package com.example.controller;

import com.example.harness.ChatResponseFactory;
import com.example.harness.TestUserRepository;
import com.example.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class StatisticResourceIntTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUserRepository testUserRepository;
    @Autowired
    private MessageRepository messageRepository;


    @Before
    public void setUp() {
        testUserRepository.deleteAll();
        messageRepository.deleteAll();
        ChatResponseFactory.insertUsers(testUserRepository);
        ChatResponseFactory.insertMessages(messageRepository);
    }

    @Test
    public void shouldReturnExpectedJson() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/users")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mostActive.id").value("53307734c3599d1de448e192"))
                .andExpect(jsonPath("$.mostMentioned.id").value("53316dc47bfc1a000000000f"));
    }
}
