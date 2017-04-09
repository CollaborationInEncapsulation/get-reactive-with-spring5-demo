package com.example.controller;

import com.example.controller.vm.MessageVM;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageResource {
    private final MessageService messageService;

    @Autowired
    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageVM> stream() {
        return messageService.latest();
    }

    @ResponseStatus
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> fallback(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Object() {
            public String message = e.getMessage();
        });
    }
}
