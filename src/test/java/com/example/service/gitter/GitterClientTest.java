package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import com.example.harness.Assertions;
import com.example.harness.ChatResponseFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(GitterConfiguration.class)
public class GitterClientTest {

    @Autowired
    private GitterClient gitterClient;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GitterProperties properties;

    @Test
    public void shouldExpectRequestWithNoQueryAndResponseWithLatestMessages() throws JsonProcessingException {
        server.expect(once(), requestTo(
                UriComponentsBuilder.fromUri(properties.getEndpoint())
                        .pathSegment(properties.getVersion(), properties.getMessagesResource().toASCIIString())
                        .build()
                        .toUriString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(ChatResponseFactory.messages(5)),
                        MediaType.APPLICATION_JSON));

        List<MessageResponse> messages = gitterClient.getMessages(null);

        server.verify();
        Assert.assertEquals(5, messages.size());
        Assertions.assertMessages(messages);
    }


    @Test
    public void shouldExpectRequestWithAfterIdQueryAndResponseWithOneMessage() throws JsonProcessingException {
        server.expect(once(), requestTo(startsWith(
                UriComponentsBuilder.fromUri(properties.getEndpoint())
                        .pathSegment(properties.getVersion(), properties.getMessagesResource().toASCIIString())
                        .build()
                        .toUriString())))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("afterId", "qwerty"))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(ChatResponseFactory.messages(1)),
                        MediaType.APPLICATION_JSON));

        List<MessageResponse> messages = gitterClient.getMessages(WebUtils.parseMatrixVariables("afterId=qwerty"));

        server.verify();
        Assert.assertEquals(1, messages.size());
        Assertions.assertMessages(messages);
    }
}
