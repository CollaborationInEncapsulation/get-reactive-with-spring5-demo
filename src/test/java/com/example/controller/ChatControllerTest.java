package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.ChatService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.utils.Assertions;
import com.example.utils.ChatResponseFactory;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestExecutionListeners({
        MockitoTestExecutionListener.class,
        SpringBootDependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService<MessageResponse> chatClient;

    @Test
    @DatabaseSetup("user-statistic.xml")
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
                        new UsersStatisticVM(new UserVM("53307734c3599d1de448e192", "suprememoocow"),
                                new UserVM("53316dc47bfc1a000000000f", "oledok"))))
                .andExpect(view().name("chat"));
    }

}
