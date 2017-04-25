package com.example.controller.ws;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public HandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public HandlerMapping webSocketHandler(ConfigurableListableBeanFactory beanFactory) {
        Map<String, WebSocketHandler> handlerMap = new HashMap<>();
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Arrays.stream(beanFactory.getBeanNamesForType(WebSocketController.class))
        .map(beanFactory::getMergedBeanDefinition)
        .map(bd->bd.)
        ;
        handlerMap.put("/api/v1/ws", (session -> session.send(session.receive().map(WebSocketMessage::retain))));
        mapping.setUrlMap(handlerMap);
        mapping.setOrder(0);

        return mapping;
    }
}
