package com.doctrine7.service.kafka;

import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.config.KafkaProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerSheduleDto {
    private final KafkaTemplate<String, SheduleChangeDto> kafkaTemplate;
    @Value("${ADD_ID}")
    private String addId;
    @Value("${KAFKA_KEY_SHEDULE}")
    private String key;

    public KafkaProducerSheduleDto(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
    }

    public void sendMessage(SheduleChangeDto sheduleChangeDto) {
        this.kafkaTemplate.send(this.addId, this.key, sheduleChangeDto);
    }
}
