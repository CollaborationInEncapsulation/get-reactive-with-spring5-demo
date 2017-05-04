package com.example.repository.support;

import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.example.repository.support.JpaBoundsOperators.flux;
import static com.example.repository.support.JpaBoundsOperators.mono;

@NoRepositoryBean
public class SimpleReactiveJpaRepositoryDecorator<T, ID extends Serializable> implements ReactiveCrudRepository<T, ID> {
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private static final String ITERABLE_MUST_NOT_BE_NULL = "The given Iterable of entities must not be null!";
    private static final String ENTITY_MUST_NOT_BE_NULL = "The entity must not be null!";
    public static final String PUBLISHER_MUST_NOT_BE_NULL = "The given Publisher must not be null!";

    private final JpaRepository<T, ID> decoratedRepository;

    public SimpleReactiveJpaRepositoryDecorator(JpaRepository<T, ID> jpaRepository) {
        decoratedRepository = jpaRepository;
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        return mono(Mono.just(entity), mono -> mono.map(decoratedRepository::save));
    }

    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, ITERABLE_MUST_NOT_BE_NULL);

        return mono(Mono.just(entities), mono -> mono.map(decoratedRepository::saveAll))
                .flatMapIterable(v -> v);
    }

    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> entityStream) {
        Assert.notNull(entityStream, PUBLISHER_MUST_NOT_BE_NULL);

        return flux(Flux.from(entityStream), flux -> flux.flatMapIterable(decoratedRepository::saveAll));
    }

    @Override
    public Mono<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return mono(
                Mono.just(id),
                mono -> mono.map(decoratedRepository::findById).flatMap(o -> o.map(Mono::just).orElse(Mono.empty()))
        );
    }

    @Override
    public Mono<T> findById(Mono<ID> id) {
        Assert.notNull(id, PUBLISHER_MUST_NOT_BE_NULL);

        return mono(
                id,
                mono -> mono.map(decoratedRepository::findById).flatMap(o -> o.map(Mono::just).orElse(Mono.empty()))
        );
    }

    @Override
    public Mono<Boolean> existsById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return mono(Mono.just(id), mono -> mono.map(decoratedRepository::existsById));
    }

    @Override
    public Mono<Boolean> existsById(Mono<ID> id) {
        Assert.notNull(id, PUBLISHER_MUST_NOT_BE_NULL);

        return mono(id, mono -> mono.map(decoratedRepository::existsById));
    }

    @Override
    public Flux<T> findAll() {
        return mono(Mono.empty(), mono -> Mono.just(decoratedRepository.findAll()))
                .flatMapIterable(v -> v);
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, ITERABLE_MUST_NOT_BE_NULL);

        return mono(Mono.just(ids), mono -> mono.map(decoratedRepository::findAllById))
                .flatMapIterable(v -> v);
    }

    @Override
    public Flux<T> findAllById(Publisher<ID> idStream) {
        Assert.notNull(idStream, PUBLISHER_MUST_NOT_BE_NULL);

        return flux(Flux.from(idStream), flux -> flux.flatMapIterable(decoratedRepository::findAllById));
    }

    @Override
    public Mono<Long> count() {
        return mono(Mono.empty(), mono -> Mono.just(decoratedRepository.count()));
    }

    @Override
    public Mono<Void> deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return mono(Mono.just(id), mono -> mono.doOnNext(decoratedRepository::deleteById))
                .then();
    }

    @Override
    public Mono<Void> delete(T entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        return mono(Mono.just(entity), mono -> mono.doOnNext(decoratedRepository::delete))
                .then();
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends T> entities) {
        Assert.notNull(entities, ITERABLE_MUST_NOT_BE_NULL);

        return mono(Mono.just(entities), mono -> mono.doOnNext(decoratedRepository::deleteAll))
                .then();
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {
        Assert.notNull(entityStream, PUBLISHER_MUST_NOT_BE_NULL);

        return flux(Flux.from(entityStream), flux -> flux.doOnNext(decoratedRepository::deleteAll))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return mono(Mono.empty(), mono -> Mono.fromRunnable(decoratedRepository::deleteAll))
                .then();
    }
}
