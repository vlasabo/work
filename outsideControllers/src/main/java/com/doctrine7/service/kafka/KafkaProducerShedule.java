package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfigShedule;
import com.doctrine7.model.SheduleChangeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerShedule {
    private final KafkaTemplate<String, SheduleChangeDto> kafkaTemplate;

    public KafkaProducerShedule(KafkaProducerConfigShedule kafkaProducerConfigShedule) {
        this.kafkaTemplate = kafkaProducerConfigShedule.kafkaTemplate();
    }

    public void sendMessage(String topic, String key, SheduleChangeDto sheduleChangeDto) {
        this.kafkaTemplate.send(topic, key, sheduleChangeDto);
    }
}
