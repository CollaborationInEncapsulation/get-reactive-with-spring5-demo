package com.example.repository.support;

import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
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

    private final JpaRepository<T, ID> decoratedRepository;

    public SimpleReactiveJpaRepositoryDecorator(JpaRepository<T, ID> jpaRepository) {
        decoratedRepository = jpaRepository;
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        return applyCommonOperations(Mono.just(entity), flux -> flux.map(decoratedRepository::save)).single();

    }

    @Override
    public <S extends T> Flux<S> save(Iterable<S> entities) {
        return applyCommonOperations(Mono.just(entities),
                flux -> flux.map(decoratedRepository::save).flatMap(Flux::fromIterable));
    }

    @Override
    public <S extends T> Flux<S> save(Publisher<S> entityStream) {
        return applyCommonOperations(Flux.from(entityStream),
                flux -> flux.map(decoratedRepository::save).flatMap(Flux::fromIterable));
    }

    @Override
    public Mono<T> findOne(ID id) {
        return applyCommonOperations(Mono.just(id),
                flux -> flux.map(decoratedRepository::findOne).map(Optional::get).onErrorResumeWith(t -> Mono.empty()))
                .single();
    }

    @Override
    public Mono<T> findOne(Mono<ID> id) {
        return applyCommonOperations(id,
                flux -> flux.map(decoratedRepository::findOne).map(Optional::get).onErrorResumeWith(t -> Mono.empty()))
                .single();
    }

    @Override
    public Mono<Boolean> exists(ID id) {
        return applyCommonOperations(Mono.just(id),
                flux -> flux.map(decoratedRepository::exists).onErrorResumeWith(t -> Mono.just(false)))
                .single();
    }

    @Override
    public Mono<Boolean> exists(Mono<ID> id) {
        return applyCommonOperations(id,
                flux -> flux.map(decoratedRepository::exists).onErrorResumeWith(t -> Mono.just(false)))
                .single();
    }

    @Override
    public Flux<T> findAll() {
        return applyCommonOperations(Mono.<T>empty(),
                flux -> flux
                        .concatWith(Flux.defer(() -> Flux.fromIterable(decoratedRepository.findAll()))));
    }

    @Override
    public Flux<T> findAll(Iterable<ID> ids) {
        return applyCommonOperations(Mono.<T>empty(),
                flux -> flux
                        .concatWith((Flux.defer(() -> Flux.fromIterable(decoratedRepository.findAll(ids))))));
    }

    @Override
    public Flux<T> findAll(Publisher<ID> idStream) {
        return applyCommonOperations(Flux.from(idStream),
                flux -> flux.flatMap(ids -> Flux.fromIterable(decoratedRepository.findAll(ids))));
    }

    @Override
    public Mono<Long> count() {
        return applyCommonOperations(Mono.<Long>empty(),
                flux -> flux.concatWith(Flux.defer(() -> Flux.just(decoratedRepository.count()))))
                .single();
    }

    @Override
    public Mono<Void> delete(ID id) {
        return applyCommonOperations(Mono.<Void>empty(),
                flux -> flux.concatWith(Flux.defer(() -> Mono.fromRunnable(() -> decoratedRepository.delete(id)))))
                .single();
    }

    @Override
    public Mono<Void> delete(T entity) {
        return applyCommonOperations(Mono.<Void>empty(),
                flux -> flux.concatWith(Flux.defer(() -> Mono.fromRunnable(() -> decoratedRepository.delete(entity)))))
                .single();
    }

    @Override
    public Mono<Void> delete(Iterable<? extends T> entities) {
        return applyCommonOperations(Mono.<Void>empty(),
                flux -> flux.concatWith(Flux.defer(() -> Mono.fromRunnable(() -> decoratedRepository.delete(entities)
                ))))
                .single();
    }

    @Override
    public Mono<Void> delete(Publisher<? extends T> entityStream) {
        return applyCommonOperations(Flux.from(entityStream),
                flux -> flux.doOnNext(decoratedRepository::delete))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return applyCommonOperations(Mono.<Void>empty(),
                flux -> flux.concatWith(Flux.defer(() -> Mono.fromRunnable(decoratedRepository::deleteAll))))
                .single();
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
