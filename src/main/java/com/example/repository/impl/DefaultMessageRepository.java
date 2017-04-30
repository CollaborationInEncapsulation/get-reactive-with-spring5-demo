package com.example.repository.impl;

import com.example.domain.Message;
import com.example.repository.MessageRepository;
import com.example.repository.support.SimpleReactiveJpaRepositoryDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.repository.support.JpaBoundsOperators.mono;

@Service
public class DefaultMessageRepository extends SimpleReactiveJpaRepositoryDecorator<Message, String>
        implements MessageRepository {

    private final JpaMessageRepository jpaRepository;

    @Autowired
    public DefaultMessageRepository(JpaMessageRepository jpaRepository) {
        super(jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<Message> findAllEager() {
        return mono(Mono.empty(), mono -> Mono.just(jpaRepository.findAllEager()))
                .flatMapIterable(v -> v);
    }
}
