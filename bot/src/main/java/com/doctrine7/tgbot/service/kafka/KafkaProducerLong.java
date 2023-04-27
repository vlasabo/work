package com.doctrine7.tgbot.service.kafka;

import com.doctrine7.tgbot.config.KafkaProducerConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerLong {
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public KafkaProducerLong(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
    }

    public CompletableFuture<SendResult<String, Long>> sendMessage(String topic, String key, Long userId) {
        return this.kafkaTemplate.send(topic, key, userId);
    }
}
