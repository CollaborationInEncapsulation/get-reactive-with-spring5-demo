package com.example.service.impl.utils;

import com.example.controller.vm.MessageVM;
import com.example.domain.Issue;
import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.service.gitter.MessageResponse;
import com.example.service.gitter.Url;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageMapper {
    private MessageMapper() {
    }

    public static List<MessageVM> toViewModelUnits(Iterable<MessageResponse> messages) {
        List<MessageVM> vms = new ArrayList<>();

        for (MessageResponse message : messages) {
            vms.add(new MessageVM(message.getId(),
                    message.getText(),
                    message.getHtml(),
                    message.getFromUser().getUsername(),
                    message.getFromUser().getAvatarUrl(),
                    message.getSent()));
        }

        return vms;

    }

    public static List<Message> toDomainUnits(Iterable<MessageResponse> messages) {
        List<Message> persistable = new ArrayList<>();

        for (MessageResponse message : messages) {
            persistable.add(Message.builder()
                    .id(message.getId())
                    .html(message.getHtml())
                    .text(message.getText())
                    .sent(message.getSent())
                    .readBy(message.getReadBy())
                    .unread(message.getUnRead())
                    .user(User.of(message.getFromUser().getId(), message.getFromUser().getUsername()))
                    .urls(message.getUrls().stream().map(Url::getUrl).toArray(String[]::new))
                    .mentions(message.getMentions().stream().filter(m -> m.getUserId() != null)
                            .map(m -> Mention.of(Mention.Key.of(message.getId(), m.getUserId())))
                            .collect(Collectors.toSet()))
                    .issues(message.getIssues().stream()
                            .map(i -> Issue.of(Long.valueOf(i.getNumber()))).collect(Collectors.toSet()))
                    .build());
        }

        return persistable;
    }
}
