package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.harness.Assertions;
import com.example.harness.ChatResponseFactory;
import com.example.harness.TestUserRepository;
import com.example.repository.MessageRepository;
import com.example.service.ChatService;
import com.example.service.gitter.dto.MessageResponse;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureDataMongo
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService<MessageResponse> chatClient;
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
    public void shouldRespondOnRootUrlWithCorrectModel() throws Exception {
        Mockito.when(chatClient.getMessagesAfter(null)).thenReturn(ChatResponseFactory.messages(10));

        mockMvc.perform(get("")
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(model().attribute("messages", new BaseMatcher<List<MessageVM>>() {
                    @Override
                    public void describeTo(Description description) {

                    }

                    @Override
                    public boolean matches(Object item) {
                        Assertions.assertMessages((List<MessageVM>) item);
                        return true;
                    }
                }))
                .andExpect(model().attribute("statistic",
                        new UsersStatisticVM(new UserVM("53307831c3599d1de448e19a", "macpi"),
                                new UserVM("53316dc47bfc1a000000000f", "oledok"))))
                .andExpect(view().name("chat"));
    }

}
