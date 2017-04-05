package com.example.service;

public interface ChatService<T> {
    Iterable<T> getMessagesAfter(String messageId);
}
