package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.springframework.util.MultiValueMap;

import java.util.List;


public interface GitterClient {

    List<MessageResponse> getMessages(MultiValueMap<String, String> query);
}
