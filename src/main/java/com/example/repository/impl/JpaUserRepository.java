package com.example.repository.impl;

import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, String> {
    @Query("SELECT user " +
            "FROM Message messages " +
            "INNER JOIN messages.user user " +
            "GROUP BY user " +
            "ORDER BY COUNT(messages) DESC")
    Page<User> findAllOrderedByActivityDesc(Pageable pageable);

    @Query("SELECT user " +
            "FROM Mention mentions " +
            "INNER JOIN mentions.user user " +
            "GROUP BY user " +
            "ORDER BY COUNT(mentions) DESC")
    Page<User> findAllOrderedByMentionDesc(Pageable pageable);
}
