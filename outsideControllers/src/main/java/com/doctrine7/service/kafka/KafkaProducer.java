package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfig;
import com.doctrine7.model.SheduleDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, List<SheduleDto>> kafkaTemplate;

    public KafkaProducer(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
    }

    public void sendMessage(String topic, String key, List<SheduleDto> sheduleDtoList) {
        this.kafkaTemplate.send(topic, key, sheduleDtoList);
    }
}
