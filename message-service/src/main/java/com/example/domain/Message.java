package com.example.domain;

import com.mongodb.annotations.Immutable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Value
@Builder
@Document
@Immutable
@RequiredArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class Message implements Serializable {
    @Id
    @NonNull
    private final String id;
    @NonNull
    private final String text;
    @NonNull
    private final String html;
    @NonNull
    private final Date sent;
    @NonNull
    private final User user;
    @NonNull
    private final Boolean unread;
    @NonNull
    private final Long readBy;
    @NonNull
    private final String[] urls;
    @NonNull
    private final Set<Mention> mentions;
    @NonNull
    private final Set<Issue> issues;
}
