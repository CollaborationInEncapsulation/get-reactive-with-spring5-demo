package com.example.service;


import com.example.service.gitter.GitterApi;
import com.example.service.gitter.MessageResponse;
import com.example.service.impl.GitterClient;
import com.example.utils.Assertions;
import com.example.utils.ChatResponseFactory;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static retrofit2.mock.Calls.response;

public class GitterClientTest {
    private GitterClient gitterClient;
    private GitterApi gitterApi;

    @Before
    public void setUp() {
        gitterApi = Mockito.mock(GitterApi.class);
        gitterClient = new GitterClient(gitterApi);
    }

    @Test
    public void shouldReturnMessagesFromGitter() {
        when(gitterApi.getRoomMessages(anyString(), anyMap())).thenReturn(response(ChatResponseFactory.messages(10)));
        Iterable<MessageResponse> response = gitterClient.getMessagesAfter(null);

        Assertions.assertMessages(response);
    }

    @Test
    public void shouldReturnMessagesFromGitterAfterGivenCursor() {
        when(gitterApi.getRoomMessages(anyString(), anyMap())).thenReturn(response(ChatResponseFactory.messages(1)));
        Iterable<MessageResponse> response = gitterClient.getMessagesAfter("qwerty");

        Mockito.verify(gitterApi).getRoomMessages(anyString(), eq(Maps.newHashMap("afterId", "qwerty")));
        Assertions.assertMessages(response);
    }
}
