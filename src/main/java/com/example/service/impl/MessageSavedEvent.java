package com.example.service.impl;

import com.example.domain.Message;
import org.springframework.context.ApplicationEvent;

public class MessageSavedEvent extends ApplicationEvent {
    /**
     * Create a new MessageSavedEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageSavedEvent(Message source) {
        super(source);
    }

    @Override
    public Message getSource() {
        return (Message) super.getSource();
    }
}
