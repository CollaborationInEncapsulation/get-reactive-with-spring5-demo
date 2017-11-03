package com.example.harness;


import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.MessageRepository;
import com.example.service.gitter.dto.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ChatResponseFactory {
    private ChatResponseFactory() {
    }

    public static void insertUsers(TestUserRepository userRepository) {
        userRepository.insert(Arrays.asList(
                User.of("53307734c3599d1de448e192", "suprememoocow", "suprememoocow"),
                User.of("53316dc47bfc1a000000000f", "oledok", "oledok"),
                User.of("53307831c3599d1de448e19a", "macpi", "macpi")
        ));
    }

    public static void insertMessages(MessageRepository messageRepository) {
        messageRepository.insert(Arrays.asList(
                Message.of("53316dc47bfc1a000000000f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53307734c3599d1de448e192", "suprememoocow", "suprememoocow"),
                        false, 0L, new String[0], new HashSet<com.example.domain.Mention>(Arrays.asList(
                                com.example.domain.Mention.of("53307734c3599d1de448e192", "suprememoocow"),
                                com.example.domain.Mention.of("53316dc47bfc1a000000000f", "oledok")
                        )), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000001f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53316dc47bfc1a000000000f", "oledok", "oledok"),
                        false, 0L, new String[0], new HashSet<com.example.domain.Mention>(Arrays.asList(
                                com.example.domain.Mention.of("53307831c3599d1de448e19a", "macpi"),
                                com.example.domain.Mention.of("53316dc47bfc1a000000000f", "oledok")
                        )), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000002f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53316dc47bfc1a000000000f", "oledok", "oledok"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000003f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53316dc47bfc1a000000000f", "oledok", "oledok"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000004f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53307831c3599d1de448e19a", "macpi", "macpi"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000005f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53307831c3599d1de448e19a", "macpi", "macpi"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000006f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53307831c3599d1de448e19a", "macpi", "macpi"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000008f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53307831c3599d1de448e19a", "macpi", "macpi"),
                        false, 0L, new String[0], new HashSet<com.example.domain.Mention>(Arrays.asList(
                                com.example.domain.Mention.of("53307831c3599d1de448e19a", "macpi"),
                                com.example.domain.Mention.of("53316dc47bfc1a000000000f", "oledok"),
                                com.example.domain.Mention.of("53307734c3599d1de448e192", "suprememoocow")
                        )), Collections.emptySet()),
                Message.of("53316dc47bfc1a000000007f", "Hi @suprememoocow !", "Hi @suprememoocow !", new Date(),
                        User.of("53316dc47bfc1a000000000f", "oledok", "oledok"),
                        false, 0L, new String[0], Collections.emptySet(), Collections.emptySet())
        ));
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
