package com.example.service.utils;


import com.example.service.gitter.MessageResponse;
import com.example.service.gitter.Role;
import com.example.service.gitter.UserResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class MessageResponseFactory {
    private MessageResponseFactory() {
    }

//    public static Iterable<MessageResponse> generate(int amount) {
//        IntStream.range(0, amount)
//                .mapToObj(String::valueOf)
//                .map(id->new MessageResponse(
//                        id,
//                        id,
//                        id,
//                        Instant.ofEpochSecond(1395748292L),
//                        null,
//                        new UserResponse(id,null, id, id, id, id, id, Role.STANDARD, false, id, id),
//                        true,
//                        0,
//                        Arrays.asList(id, id,id),
//                        Arrays.asList()
//                ))
//    }
}
