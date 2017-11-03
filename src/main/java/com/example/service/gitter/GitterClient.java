package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;


public interface GitterClient {

    Flux<MessageResponse> getMessagesStream(MultiValueMap<String, String> query);

    Flux<MessageResponse> getLatestMessages();
}
