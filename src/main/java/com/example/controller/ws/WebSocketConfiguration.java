package com.example.controller.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@SuppressWarnings("unchecked")
public class WebSocketConfiguration {

    private static final Predicate<SimpleReactiveBiDirectionalWebSocketHandler> HANDLERS_FILTER =
            h -> AnnotationUtils.isAnnotationDeclaredLocally(RequestMapping.class, h.getClass())
                    || AnnotationUtils.isAnnotationInherited(RequestMapping.class, h.getClass());
    private static final Function<AbstractMap.SimpleEntry<String[], WebSocketHandler>, Stream<? extends Map.Entry<String, WebSocketHandler>>> FLAT_ENTRY_TRANSFORMER =
            e -> Arrays
                    .stream(e.getKey())
                    .collect(Collectors.toMap(Function.identity(), p -> e.getValue()))
                    .entrySet()
                    .stream();

    @Bean
    public HandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public HandlerMapping webSocketHandler(ConfigurableListableBeanFactory beanFactory,
                                           ObjectMapper mapper) {

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        mapping.setUrlMap(scanForMappings(beanFactory, mapper));
        mapping.setOrder(0);

        return mapping;
    }

    private Map<String, WebSocketHandler> scanForMappings(ConfigurableListableBeanFactory beanFactory,
                                                          ObjectMapper mapper) {
        Jackson2JsonEncoder encoder = new Jackson2JsonEncoder(mapper);
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(mapper);

        return BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, SimpleReactiveBiDirectionalWebSocketHandler.class, true, false)
                .values()
                .stream()
                .filter(HANDLERS_FILTER)
                .map(h -> new HashMap.SimpleEntry<>(findRequestMappings(h), adaptToHandler(h, decoder, encoder)))
                .flatMap(FLAT_ENTRY_TRANSFORMER)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private String[] findRequestMappings(SimpleReactiveBiDirectionalWebSocketHandler adapter) {
        return AnnotationUtils.findAnnotation(adapter.getClass(), RequestMapping.class).path();
    }

    private WebSocketHandler adaptToHandler(SimpleReactiveBiDirectionalWebSocketHandler<?> adapter,
                                            Jackson2JsonDecoder decoder,
                                            Jackson2JsonEncoder encoder) {
        return s -> s.send(transform(Flux.from(adapter.handle(transform(
                s.receive(),
                decoder,
                ResolvableType.forInstance(adapter).getGeneric(0)
        ))), encoder, s.bufferFactory())).then();
    }

    private Flux<WebSocketMessage> transform(Flux<?> fromSource, Jackson2JsonEncoder encoder, DataBufferFactory factory) {
        return fromSource
                .map(i -> encoder.encode(Mono.just(i), factory, ResolvableType.forType(Object.class),
                        MediaType.APPLICATION_JSON, Collections.emptyMap()))
                .flatMap(f -> f.map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db)));
    }

    private Flux transform(Flux<WebSocketMessage> in, Jackson2JsonDecoder decoder, ResolvableType resolvableType) {
        return in
                .map(WebSocketMessage::getPayload)
                .flatMap(p -> decoder.decode(Mono.just(p), resolvableType,
                        MediaType.APPLICATION_JSON, Collections.emptyMap()));
    }


}
