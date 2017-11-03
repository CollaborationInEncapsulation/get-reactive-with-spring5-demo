package com.example.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Value
@Document(collection = "issues")
@RequiredArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class Issue implements Serializable {

    @NonNull
    private final Long id;
}
