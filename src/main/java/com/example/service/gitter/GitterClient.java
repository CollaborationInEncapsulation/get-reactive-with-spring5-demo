package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.reactivestreams.Publisher;
import org.springframework.util.MultiValueMap;


public interface GitterClient {

    Publisher<MessageResponse> getMessages(MultiValueMap<String, String> query);
}
