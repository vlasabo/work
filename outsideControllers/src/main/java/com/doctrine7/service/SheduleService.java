package com.doctrine7.service;

import com.doctrine7.model.SheduleDto;
import com.doctrine7.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void writeProcedureChange(List<SheduleDto> sheduleList) {
        logger.info("write schedule changes to Kafka.\n{}", sheduleList);
        kafkaProducer.sendMessage(topicSheduleChange, key, sheduleList);
    }

    public void writeProcedureDelete(List<SheduleDto> sheduleList) {
        logger.info("write schedule deleting to Kafka.\n{}", sheduleList);
        kafkaProducer.sendMessage(topicSheduleDelete, key, sheduleList);
    }
}
