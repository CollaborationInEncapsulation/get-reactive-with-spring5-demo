package com.example.service;


import com.example.controller.vm.MessageVM;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessageService {
    Flux<MessageVM> stream();
}
