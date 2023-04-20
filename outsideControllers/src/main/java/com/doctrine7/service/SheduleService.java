package com.doctrine7.service;

import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SheduleService {
    private final Logger logger = LoggerFactory.getLogger(SheduleService.class);
    private final KafkaProducer kafkaProducer;
    @Value("${KAFKA_TOPIC_SHEDULE_CHANGE}")
    private String topicSheduleChange;
    @Value("${KAFKA_TOPIC_SHEDULE_DELETE}")
    private String topicSheduleDelete;
    @Value("${KAFKA_KEY_SHEDULE}")
    private String key;

    public void writeProcedureChange(SheduleChangeDto sheduleChangeDto) {
        logger.info("write schedule changes to Kafka.\n{}", sheduleChangeDto);
        kafkaProducer.sendMessage(topicSheduleChange, key, sheduleChangeDto);
    }

    public void writeProcedureDelete(SheduleChangeDto sheduleChangeDto) {
        logger.info("write schedule deleting to Kafka.\n{}", sheduleChangeDto);
        kafkaProducer.sendMessage(topicSheduleDelete, key, sheduleChangeDto);
    }
}
