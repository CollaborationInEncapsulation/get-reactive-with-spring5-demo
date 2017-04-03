package com.example.repository;

import com.example.domain.Message;
import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT messages FROM Message messages")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "load.eager.all")
    Flux<Message> findAllEager();

    /**
     * Saves all given entities.
     *
     * @param entityStream must not be {@literal null}.
     * @return the saved entities
     * @throws IllegalArgumentException in case the given {@code Publisher} is {@literal null}.
     */
     Flux<Message> save(Publisher<Message> entityStream);
}
