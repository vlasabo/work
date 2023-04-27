package com.doctrine7.tgbot.service.kafka;

import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.tgbot.config.KafkaProducerConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerInputMessage {
    private final KafkaTemplate<String, InputMessageDto> kafkaTemplate;

    public KafkaProducerInputMessage(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplateInputMessageDto();
    }

    public void sendMessage(String topic, String key, InputMessageDto inputMessageDto) {
        this.kafkaTemplate.send(topic, key, inputMessageDto);
    }
}
