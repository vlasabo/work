package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfig;
import com.doctrine7.model.OutputMessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerOutputMessage {
    private final KafkaTemplate<String, OutputMessageDto> kafkaTemplate;

    public KafkaProducerOutputMessage(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplateOutputMessage();
    }

    public void sendMessage(String topic, String key, OutputMessageDto outputMessageDto) {
        this.kafkaTemplate.send(topic, key, outputMessageDto);
    }
}
