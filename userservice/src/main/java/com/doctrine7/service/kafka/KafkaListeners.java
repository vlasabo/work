package com.doctrine7.service.kafka;

import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.service.InputMessageService;
import com.doctrine7.service.UserService;
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
    private final UserService userService;
    private final InputMessageService inputMessageService;

    @KafkaListener(
            topics = {"shedulechange",
                    "sheduledelete"},
            groupId = "groupId")
    public void consume(final ConsumerRecord<String, SheduleChangeDto> record,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        userService.setEmployees(record.value());
    }

    @KafkaListener(
            topics = {"blockedByUser"},
            groupId = "groupId")
    public void blockFromUser(final ConsumerRecord<String, Long> record,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        userService.blockUser(record.value());
    }

    @KafkaListener(
            topics = {"filling"},
            groupId = "groupId")
    public void fillingInputMessageDto(final ConsumerRecord<String, InputMessageDto> record,
                              final @Header(KafkaHeaders.OFFSET) Integer offset,
                              final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                              final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                              final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                              final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        inputMessageService.fill(record.value());
    }
}

