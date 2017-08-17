package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.reactivestreams.Publisher;
import org.springframework.util.MultiValueMap;


public interface GitterClient {

    Publisher<MessageResponse> getMessagesStream(MultiValueMap<String, String> query);

    Publisher<MessageResponse> getLatestMessages();
}
