package com.example.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Value
@Document(collection = "mentions")
@RequiredArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class Mention implements Serializable {

    @NonNull
    private final String userId;

    @NonNull
    private final String screenName;
}
