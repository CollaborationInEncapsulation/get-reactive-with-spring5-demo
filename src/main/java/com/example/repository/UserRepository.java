package com.example.repository;

import com.example.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Flux<User> findAllOrderedByActivityDesc(Pageable pageable);

    Flux<User> findAllOrderedByMentionDesc(Pageable pageable);
}
