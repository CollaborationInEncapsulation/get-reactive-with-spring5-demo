package com.example.domain;

import com.mongodb.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Value
@Document(collection = "users")
@Immutable
@AllArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class User implements Serializable {

    @Id
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String displayName;
}
