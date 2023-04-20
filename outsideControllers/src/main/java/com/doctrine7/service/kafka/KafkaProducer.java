package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfig;
import com.doctrine7.model.SheduleChangeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducer {
    private final KafkaTemplate<String, SheduleChangeDto> kafkaTemplate;

    public KafkaProducer(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
    }

    public void sendMessage(String topic, String key, SheduleChangeDto sheduleChangeDto) {
        this.kafkaTemplate.send(topic, key, sheduleChangeDto);
    }
}
