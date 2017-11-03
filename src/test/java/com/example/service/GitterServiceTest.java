package com.example.service;


import com.example.harness.Assertions;
import com.example.harness.ChatResponseFactory;
import com.example.service.gitter.GitterClient;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.GitterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.util.WebUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

public class GitterServiceTest {
    private GitterService gitterService;
    private GitterClient gitterClient;

    @Before
    public void setUp() {
        gitterClient = Mockito.mock(GitterClient.class);
        gitterService = new GitterService(gitterClient);
    }

    @Test
    public void shouldReturnMessagesFromGitter() {
        when(gitterClient.getMessages(any())).thenReturn(ChatResponseFactory.messages(10));
        Iterable<MessageResponse> response = gitterService.getMessagesAfter(null);

        Assertions.assertMessages(response);
    }

    @Test
    public void shouldReturnMessagesFromGitterAfterGivenCursor() {
        when(gitterClient.getMessages(any())).thenReturn(ChatResponseFactory.messages(1));
        Iterable<MessageResponse> response = gitterService.getMessagesAfter("qwerty");

        Mockito.verify(gitterClient).getMessages(eq(WebUtils.parseMatrixVariables("afterId=qwerty")));
        Assertions.assertMessages(response);
    }
}
