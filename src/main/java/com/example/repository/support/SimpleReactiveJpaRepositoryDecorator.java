package com.example.repository.support;

import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

        return applyCommonOperations(Mono.just(entity), flux -> flux.map(decoratedRepository::save)).single();

    }

    @Override
    public <S extends T> Flux<S> save(Iterable<S> entities) {
        Assert.notNull(entities, ITERABLE_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.just(entities),
                flux -> flux.map(decoratedRepository::save).flatMap(Flux::fromIterable));
    }

    @Override
    public <S extends T> Flux<S> save(Publisher<S> entityStream) {
        Assert.notNull(entityStream, PUBLISHER_MUST_NOT_BE_NULL);

        return applyCommonOperations(Flux.from(entityStream),
                flux -> flux.map(decoratedRepository::save).flatMap(Flux::fromIterable));
    }

    @Override
    public Mono<T> findOne(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.just(id),
                flux -> flux.map(decoratedRepository::findOne).map(Optional::get).onErrorResume(t -> Mono.empty()))
                .single();
    }

    @Override
    public Mono<T> findOne(Mono<ID> id) {
        Assert.notNull(id, PUBLISHER_MUST_NOT_BE_NULL);

        return applyCommonOperations(id,
                flux -> flux.map(decoratedRepository::findOne).map(Optional::get).onErrorResume(t -> Mono.empty()))
                .single();
    }

    @Override
    public Mono<Boolean> exists(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.just(id),
                flux -> flux.map(decoratedRepository::exists).onErrorResume(t -> Mono.just(false)))
                .single();
    }

    @Override
    public Mono<Boolean> exists(Mono<ID> id) {
        Assert.notNull(id, PUBLISHER_MUST_NOT_BE_NULL);

        return applyCommonOperations(id,
                flux -> flux.map(decoratedRepository::exists).onErrorResume(t -> Mono.just(false)))
                .single();
    }

    @Override
    public Flux<T> findAll() {
        return applyCommonOperations(Mono.<T>empty(),
                flux -> flux
                        .concatWith(Flux.fromIterable(decoratedRepository.findAll())));
    }

    @Override
    public Flux<T> findAll(Iterable<ID> ids) {
        Assert.notNull(ids, ITERABLE_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.<T>empty(),
                flux -> flux.concatWith(Flux.fromIterable(decoratedRepository.findAll(ids))));
    }

    @Override
    public Flux<T> findAll(Publisher<ID> idStream) {
        Assert.notNull(idStream, PUBLISHER_MUST_NOT_BE_NULL);

        return applyCommonOperations(Flux.from(idStream),
                flux -> flux.flatMap(ids -> Flux.fromIterable(decoratedRepository.findAll(ids))));
    }

    @Override
    public Mono<Long> count() {
        return applyCommonOperations(Mono.<Long>empty(),
                flux -> flux.concatWith(Flux.just(decoratedRepository.count())))
                .single();
    }

    @Override
    public Mono<Void> delete(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.empty(),
                flux -> flux.concatWith(Mono.fromRunnable(() -> decoratedRepository.delete(id))))
                .then();
    }

    @Override
    public Mono<Void> delete(T entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.empty(),
                flux -> flux.concatWith(Mono.fromRunnable(() -> decoratedRepository.delete(entity))))
                .then();
    }

    @Override
    public Mono<Void> delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, ITERABLE_MUST_NOT_BE_NULL);

        return applyCommonOperations(Mono.empty(),
                flux -> flux.concatWith(Mono.fromRunnable(() -> decoratedRepository.delete(entities))))
                .then();
    }

    @Override
    public Mono<Void> delete(Publisher<? extends T> entityStream) {
        Assert.notNull(entityStream, PUBLISHER_MUST_NOT_BE_NULL);

        return applyCommonOperations(Flux.from(entityStream),
                flux -> flux.doOnNext(decoratedRepository::delete))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return applyCommonOperations(Mono.empty(),
                flux -> flux.concatWith(Mono.fromRunnable(decoratedRepository::deleteAll)))
                .then();
    }

    protected static <S, V> Flux<V> applyCommonOperations(
            Mono<S> input,
            Function<? super Flux<S>, ? extends Publisher<V>> transformer) {

        TopicProcessor<V> emitterProcessor = TopicProcessor.create();

        input
                .flux()
                .publishOn(Schedulers.elastic())
                .transform(transformer)
                .subscribe(emitterProcessor);

        return emitterProcessor;
    }

    protected static <S, V> Flux<V> applyCommonOperations(
            Flux<S> input,
            Function<? super Flux<List<S>>, ? extends Publisher<V>> transformer) {

        TopicProcessor<V> emitterProcessor = TopicProcessor.create();

        input
                .buffer(Duration.ofMillis(100))
                .publishOn(Schedulers.elastic())
                .transform(transformer)
                .subscribe(emitterProcessor);

        return emitterProcessor;
    }
}
