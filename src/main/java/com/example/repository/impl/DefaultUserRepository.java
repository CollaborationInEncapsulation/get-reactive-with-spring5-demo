package com.example.repository.impl;

import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.repository.support.SimpleReactiveJpaRepositoryDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.repository.support.JpaBoundsOperators.mono;

@Service
public class DefaultUserRepository extends SimpleReactiveJpaRepositoryDecorator<User, String>
        implements UserRepository {

    private final JpaUserRepository jpaRepository;

    @Autowired
    public DefaultUserRepository(JpaUserRepository jpaRepository) {
        super(jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<User> findAllOrderedByActivityDesc(Pageable pageable) {
        return mono(
                Mono.just(pageable),
                mono -> mono.map(jpaRepository::findAllOrderedByActivityDesc).map(Page::getContent)
        ).flatMapIterable(v -> v);
    }

    @Override
    public Flux<User> findAllOrderedByMentionDesc(Pageable pageable) {
        return mono(
                Mono.just(pageable),
                mono -> mono.map(jpaRepository::findAllOrderedByMentionDesc).map(Page::getContent)
        ).flatMapIterable(v -> v);
    }
}
