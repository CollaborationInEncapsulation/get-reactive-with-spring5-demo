package com.example.service.impl.utils;

import com.example.controller.vm.MessageVM;
import com.example.domain.Issue;
import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.gitter.dto.Url;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

public final class MessageMapper {
    private MessageMapper() {
    }

    public static Flux<MessageVM> toViewModelUnits(Flux<MessageResponse> messages) {
        if (messages == null) {
            return null;
        }


        return messages.map(MessageMapper::toViewModelUnit);

    }

    public static Flux<Message> toDomainUnits(Flux<MessageResponse> messages) {
        if (messages == null) {
            return null;
        }

        return messages.map(MessageMapper::toDomainUnit);
    }

    private static Message toDomainUnit(MessageResponse message) {
        if (message == null) {
            return null;
        }

        return Message.builder()
                .id(message.getId())
                .html(message.getHtml())
                .text(message.getText())
                .sent(message.getSent())
                .readBy(message.getReadBy())
                .unread(message.getUnRead())
                .user(User.of(
                        message.getFromUser().getId(),
                        message.getFromUser().getUsername(),
                        message.getFromUser().getDisplayName()
                ))
                .urls(message.getUrls().stream().map(Url::getUrl).toArray(String[]::new))
                .mentions(message.getMentions().stream().filter(m -> m.getUserId() != null)
                        .map(m -> Mention.of(m.getUserId(), m.getScreenName()))
                        .collect(Collectors.toSet()))
                .issues(message.getIssues().stream()
                        .map(i -> Issue.of(Long.valueOf(i.getNumber()))).collect(Collectors.toSet()))
                .build();
    }

    private static MessageVM toViewModelUnit(MessageResponse message) {
        if (message == null) {
            return null;
        }

        return new MessageVM(message.getId(),
                message.getText(),
                message.getHtml(),
                message.getFromUser().getUsername(),
                message.getFromUser().getAvatarUrl(),
                message.getSent());
    }
}
