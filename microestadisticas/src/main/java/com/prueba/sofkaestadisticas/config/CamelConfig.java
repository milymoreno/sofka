package com.prueba.sofkaestadisticas.config;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.camel.impl.DefaultCamelContext;

@Configuration
public class CamelConfig {

    @Bean
    public CamelContext camelContext() {
        return new DefaultCamelContext();
    }

    @Bean
    public ConsumerTemplate consumerTemplate(CamelContext camelContext) {
        return camelContext.createConsumerTemplate();
    }
}

