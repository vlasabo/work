package com.doctrine7.config;

import com.doctrine7.model.AppointmentsDocument;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigAppointment {
    @Value("${KAFKA_URL_INSIDE}")
    private String kafkaAddress;

    @Bean
    public Map<String, Object> producerConfigsAppointment() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
    @Bean
    public ProducerFactory<String, AppointmentsDocument> producerFactoryAppointment() {
        return new DefaultKafkaProducerFactory<>(producerConfigsAppointment());
    }

    @Bean
    public KafkaTemplate<String, AppointmentsDocument> kafkaTemplateAppointment() {
        return new KafkaTemplate<>(producerFactoryAppointment());
    }
}
