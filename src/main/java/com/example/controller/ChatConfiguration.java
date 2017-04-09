package com.example.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ChatConfiguration {
    @Bean
    public ISpringWebFluxTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
        // We override here the SpringTemplateEngine instance that would otherwise be instantiated by
        // Spring Boot because we want to apply the SpringWebFlux-specific context factory, link builder...
        final SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    @Bean
    public ThymeleafReactiveViewResolver thymeleafChunkedAndDataDrivenViewResolver(
            ISpringWebFluxTemplateEngine templateEngine) {
        final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();

        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);
        viewResolver.setResponseMaxChunkSizeBytes(8192); // OUTPUT BUFFER size limit

        return viewResolver;
    }
}
