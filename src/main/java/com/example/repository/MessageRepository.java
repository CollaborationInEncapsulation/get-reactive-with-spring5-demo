package com.example.repository;

import com.example.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface MessageRepository extends ReactiveCrudRepository<Message, String> {
    @Query("SELECT messages FROM Message messages")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "load.eager.all")
    Flux<Message> findAllEager();
}
