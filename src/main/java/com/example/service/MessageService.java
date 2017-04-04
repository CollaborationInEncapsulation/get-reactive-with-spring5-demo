package com.example.service;


import com.example.controller.vm.MessageVM;

import java.util.List;

public interface MessageService {
    List<MessageVM> cursor(String cursor);

    List<MessageVM> latest();
}
