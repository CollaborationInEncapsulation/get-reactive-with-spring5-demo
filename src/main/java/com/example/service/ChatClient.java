package com.example.service;

public interface ChatClient<T> {
    Iterable<T> getMessagesAfter(String messageId);
}
