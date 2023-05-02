package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfig;
import com.doctrine7.model.bot.InputMessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerInputMessage {
    private final KafkaTemplate<String, InputMessageDto> kafkaTemplate;

    public KafkaProducerInputMessage(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplateInputMessage();
    }

    public void sendMessage(String topic, String key, InputMessageDto inputMessage) {
        this.kafkaTemplate.send(topic, key, inputMessage);
    }
}
