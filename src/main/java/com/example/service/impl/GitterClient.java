package com.example.service.impl;

import com.amatkivskiy.gitter.sdk.rx.client.RxGitterStreamingApiClient;
import com.example.service.ChatClient;
import com.example.service.gitter.Issue;
import com.example.service.gitter.Mention;
import com.example.service.gitter.MessageResponse;
import com.example.service.gitter.Role;
import com.example.service.gitter.Url;
import com.example.service.gitter.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class GitterClient implements ChatClient<MessageResponse> {

    private ConnectableFlux<MessageResponse> livePublisher;
    private final RxGitterStreamingApiClient gitterApi;

    @Autowired
    public GitterClient(RxGitterStreamingApiClient gitterApi) {
//        livePublisher = Flux.<MessageResponse>create(s -> gitterApi.getRoomMessagesStream("54f9e9a215522ed4b3dce824")
        livePublisher = Flux.<MessageResponse>create(s -> gitterApi.getRoomMessagesStream("55e55b9f0fc9f982beaf4213")
                .doOnNext(m -> s.next(new ObjectMapper().convertValue(m, MessageResponse.class)))
                .doOnCompleted(s::complete)
                .doOnError(s::error)
                .doOnTerminate(s::complete)
                .subscribe())
                .publish(10);
        this.gitterApi = gitterApi;
    }

    @Override
    public Flux<MessageResponse> stream() {
        return Flux.create(s -> gitterApi.getRoomMessagesStream("55e55b9f0fc9f982beaf4213")
                .doOnNext(m -> s.next(new MessageResponse(
                        m.id,
                        m.text,
                        m.html,
                        new Date(),
                        m.editedAt,
                        new UserResponse(m.fromUser.id, 1, m.fromUser.username, m.fromUser.displayName, m.fromUser
                                .avatarUrl,
                                m.fromUser.avatarUrlSmall, m.fromUser.avatarUrlMedium, Role.STANDARD, m.fromUser
                                .staff, m.fromUser.gv, m.fromUser.url),
                        m.unRead,
                        (long) m.readBy,
                        m.urls.stream().map(u -> new Url(u.url)).collect(Collectors.toList()),
                        m.mentions.stream().map(ms -> new Mention(ms.screenName, ms.userId, Collections.emptyList()))
                                .collect(Collectors.toList()),
                        m.issues.stream().map(i -> new Issue(i.number)).collect(Collectors.toList()),
                        Collections.emptyList(),
                        1
                )))
                .doOnCompleted(s::complete)
                .doOnError(s::error)
                .doOnTerminate(s::complete)
                .subscribe());
    }
}
