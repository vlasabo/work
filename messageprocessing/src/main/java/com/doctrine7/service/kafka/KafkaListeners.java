package com.doctrine7.service.kafka;

import com.doctrine7.model.AppointmentsDocument;
import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.service.AppointmentsListService;
import com.doctrine7.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);
    private final MessageService messageService;
    private final AppointmentsListService appointmentsListService;

    @KafkaListener(
            topics = "toMessage",
            groupId = "groupId")
    public void consumeShedule(final ConsumerRecord<String, SheduleChangeDto> record,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        messageService.prepareMessage(record.value());
    }

    @KafkaListener(
            topics = "appointments",
            groupId = "groupId")
    public void consumeAppointments(final ConsumerRecord<String, AppointmentsDocument> record,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        appointmentsListService.prepareNotifications(record.value());
    }
}

