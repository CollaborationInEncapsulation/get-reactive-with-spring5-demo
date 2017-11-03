package com.example.harness;


import com.example.service.gitter.GitterProperties;
import com.example.service.gitter.GitterUriBuilder;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.util.UriComponents;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyPipeline;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class GitterMockServerRule implements MethodRule {
    private final Supplier<GitterProperties> gitterPropertiesSupplier;
    private final Publisher<? extends Publisher> dataSource;

    public GitterMockServerRule(Supplier<GitterProperties> gitterPropertiesSupplier,
                                Publisher<? extends Publisher> dataSource) {
        this.gitterPropertiesSupplier = gitterPropertiesSupplier;
        this.dataSource = dataSource;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                NettyContext context = setUpServer();
                try {
                    base.evaluate();
                } finally {
                    try {
                        context.dispose();
                        context.onClose().block();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    protected NettyContext setUpServer() {
        GitterProperties gitterProperties = gitterPropertiesSupplier.get();
        UriComponents streamComponents = GitterUriBuilder.from(gitterProperties.getStream()).build();
        UriComponents apiComponents = GitterUriBuilder.from(gitterProperties.getStream()).build();

        return HttpServer.create()
                .newRouter((shr) -> shr
                        .get(
                                streamComponents.getPath(),
                                (r, response) -> response
                                        .sse()
                                        .options(NettyPipeline.SendOptions::flushOnEach)
                                        .sendObject(
                                                Flux.from(dataSource)
                                                        .flatMap(countableSource -> new Jackson2JsonEncoder()
                                                                .encode(countableSource,
                                                                        new NettyDataBufferFactory(ByteBufAllocator.DEFAULT),
                                                                        ResolvableType.forType(Object.class),
                                                                        MimeTypeUtils.APPLICATION_JSON,
                                                                        Collections.emptyMap()))
                                                        .map(DataBuffer::asByteBuffer)
                                                        .map(Unpooled::copiedBuffer)
                                        )
                        )
                        .get(
                                apiComponents.getPath(),
                                (r, response) -> response
                                        .sendObject(
                                                Flux.range(1, 30)
                                                        .collectList()
                                                        .map(countableSource -> new Jackson2JsonEncoder()
                                                                .encode(Mono.just(countableSource),
                                                                        new NettyDataBufferFactory(ByteBufAllocator.DEFAULT),
                                                                        ResolvableType.forType(List.class),
                                                                        MimeTypeUtils.APPLICATION_JSON,
                                                                        Collections.emptyMap())
                                                                .map(DataBuffer::asByteBuffer)
                                                                .map(Unpooled::copiedBuffer))

                                        )
                        )
                )
                .block();
    }
}
