package com.doctrine7.service.kafka;

import com.doctrine7.config.KafkaProducerConfigAppointment;
import com.doctrine7.model.AppointmentsDocument;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerAppointment {
    private final KafkaTemplate<String, AppointmentsDocument> kafkaTemplateAppointment;

    public KafkaProducerAppointment(KafkaProducerConfigAppointment kafkaProducerConfigAppointment) {
        this.kafkaTemplateAppointment = kafkaProducerConfigAppointment.kafkaTemplate2();
    }

    public void sendMessage(String topic, String key, AppointmentsDocument appointmentsDocument) {
        this.kafkaTemplateAppointment.send(topic, key, appointmentsDocument);
    }
}
