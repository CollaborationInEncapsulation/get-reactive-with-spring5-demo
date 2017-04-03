package com.example.repository;

import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT user " +
            "FROM Message messages " +
            "INNER JOIN messages.user user " +
            "GROUP BY user " +
            "ORDER BY COUNT(messages) DESC")
    Mono<User> findAllOrderedByActivityDesc();

    @Query("SELECT user " +
            "FROM Mention mentions " +
            "INNER JOIN mentions.user user " +
            "GROUP BY user " +
            "ORDER BY COUNT(mentions) DESC")
    Mono<User> findAllOrderedByMentionDesc();
}
