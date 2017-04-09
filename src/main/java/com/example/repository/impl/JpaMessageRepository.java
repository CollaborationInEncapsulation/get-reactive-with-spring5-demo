package com.example.repository.impl;

import com.example.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT messages FROM Message messages")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "load.eager.all")
    List<Message> findAllEager();
}
