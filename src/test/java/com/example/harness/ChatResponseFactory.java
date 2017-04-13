package com.example.harness;


import com.example.service.gitter.dto.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ChatResponseFactory {
    private ChatResponseFactory() {
    }

    public static List<MessageResponse> messages(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(String::valueOf)
                .map(ChatResponseFactory::message)
                .collect(Collectors.toList());
    }

    public static MessageResponse message(String id) {
        return new MessageResponse(
                id,
                id,
                id,
                Date.from(Instant.ofEpochSecond(1395748292L)),
                null,
                user(id),
                true,
                0L,
                Arrays.asList(new Url(id), new Url(id), new Url(id)),
                Collections.singletonList(mention(id)),
                Collections.singletonList(issue(id)),
                Collections.emptyList(),
                1
        );
    }

    public static Issue issue(String id) {
        return new Issue(id);
    }

    public static Mention mention(String id) {
        return new Mention(id, id, Collections.emptyList());
    }

    public static UserResponse user(String id) {
        return new UserResponse(id, null, id, id, id, id, id, Role.STANDARD, false, id, id);
    }
}
