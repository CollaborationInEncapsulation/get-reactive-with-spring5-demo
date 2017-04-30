package com.example.repository.support;


import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public final class JpaBoundsOperators {
    private JpaBoundsOperators() {
    }

    /**
     * Create async bounds around blocking persistence action for mono input
     *
     * @param input data stream
     * @param transformer bounded transformation operation. Actually, all persistence action should be caused here
     * @param <S> in
     * @param <V> out
     *
     * @return mono result
     */
    public static <S, V> Mono<V> mono(Mono<S> input, Function<? super Mono<S>, ? extends Publisher<V>> transformer) {

        MonoProcessor<V> monoProcessor = MonoProcessor.create();

        input
                .publishOn(Schedulers.elastic())
                .transform(transformer)
                .subscribe(monoProcessor);

        return monoProcessor.subscribeOn(Schedulers.elastic());
    }

    /**
     * Create async bounds around blocking persistence action for flux input
     *
     * @param input data stream
     * @param transformer bounded transformation operation. Actually, all persistence action should be caused here
     * @param <S> in
     * @param <V> out
     *
     * @return flux result
     */
    public static <S, V> Flux<V> flux(Flux<S> input,
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
