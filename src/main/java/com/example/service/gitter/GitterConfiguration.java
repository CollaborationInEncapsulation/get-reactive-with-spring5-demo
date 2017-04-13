package com.example.service.gitter;

import com.example.service.gitter.dto.MessageResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Configuration
@EnableConfigurationProperties(GitterProperties.class)
public class GitterConfiguration {

    @Bean
    public GitterClient gitterClient(GitterProperties gitterProperties,
                                     RestTemplate restTemplate) {
        return (query) -> {
            ResponseEntity<List<MessageResponse>> response = restTemplate.exchange(
                   GitterUriBuilder.from(gitterProperties)
                            .queryParams(query)
                            .build()
                            .toUri(),
                    HttpMethod.GET,
                    new HttpEntity<>(WebUtils.parseMatrixVariables(
                            "Authorization=Bearer " + gitterProperties.getAuth().getToken()
                    )),
                    new ParameterizedTypeReference<List<MessageResponse>>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                //TODO replace with Custom Exception
                throw new RuntimeException(response.getBody().toString());
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
