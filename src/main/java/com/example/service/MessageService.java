package com.example.service;


import com.example.controller.vm.MessageVM;

import reactor.core.publisher.Flux;

public interface MessageService {

    Flux<MessageVM> latest();
}
