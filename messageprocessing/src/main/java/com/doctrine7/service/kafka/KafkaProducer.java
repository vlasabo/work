package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfig;
import com.doctrine7.model.OutputMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducer {
    private final KafkaTemplate<String, OutputMessageDto> kafkaTemplate;
    @Value("${BOT_MESSAGE_TOPIC}")
    private String addId;
    @Value("${KAFKA_KEY_SHEDULE}")
    private String key;

    public KafkaProducer(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
    }

    public void sendMessage(OutputMessageDto outputMessageDto) {
        this.kafkaTemplate.send(this.addId, this.key, outputMessageDto);
    }
}
