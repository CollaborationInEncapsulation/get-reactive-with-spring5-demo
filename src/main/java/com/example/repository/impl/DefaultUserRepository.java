package com.example.repository.impl;

import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.repository.support.SimpleReactiveJpaRepositoryDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
        return applyCommonOperations(Flux.<User>empty(),
                flux -> flux
                        .concatWith(Flux.just(jpaRepository.findAllOrderedByActivityDesc(pageable).getContent()))
                        .flatMap(Flux::fromIterable));
    }

    @Override
    public Flux<User> findAllOrderedByMentionDesc(Pageable pageable) {
        return  applyCommonOperations(Flux.<User>empty(),
                flux -> flux
                        .concatWith(Flux.just(jpaRepository.findAllOrderedByMentionDesc(pageable).getContent()))
                        .flatMap(Flux::fromIterable));
    }
}
