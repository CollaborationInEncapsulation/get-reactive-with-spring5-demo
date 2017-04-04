//package com.example.controller;
//
//import com.example.controller.vm.MessageVM;
//import com.example.service.MessageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/messages")
//public class MessageResource {
//    private final MessageService messageService;
//
//    @Autowired
//    public MessageResource(MessageService messageService) {
//        this.messageService = messageService;
//    }
//
//    @GetMapping
//    public Flux<MessageVM> list(@RequestParam(value = "cursor", required = false) String cursor) {
//        Flux<MessageVM> messages = messageService.cursor(cursor);
//
//        if (messages != null && messages.size() > 0) {
//            return ResponseEntity.ok()
//                    .header("cursor", messages.get(messages.size() - 1).getId())
//                    .body(messages);
//        } else {
//            ResponseEntity.HeadersBuilder<?> headersBuilder = ResponseEntity.noContent();
//
//            if (cursor != null) {
//                headersBuilder.header("cursor", cursor);
//            }
//
//            return headersBuilder.build();
//        }
//    }
//
//    @ResponseStatus
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> fallback(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Object() {
//            public String message = e.getMessage();
//        });
//    }
//}
