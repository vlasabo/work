package com.doctrine7.service;

import com.doctrine7.model.AppointmentsDocument;
import com.doctrine7.service.kafka.KafkaProducerAppointment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final KafkaProducerAppointment kafkaProducer;
    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    @Value("${KAFKA_TOPIC_APPOINTMENTS}")
    private String appTopic;
    public void writeDocumentChange(AppointmentsDocument document) {
        logger.info("write document update {}", document);
        kafkaProducer.sendMessage(appTopic,"key", document);
    }
}
