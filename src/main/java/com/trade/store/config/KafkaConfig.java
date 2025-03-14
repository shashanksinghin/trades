package com.trade.store.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic tradeTopic() {
        return TopicBuilder.name("trade-store-topic").partitions(3).replicas(1).build();
    }
}

