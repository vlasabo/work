package com.doctrine7.service;

import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.model.bot.MyBotStatus;
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
    private final InputMessageService inputMessageService;

    @KafkaListener(
            topics = "InputMessageDto",
            groupId = "groupId")
    public void consume(
            final ConsumerRecord<String, InputMessageDto> record,
            final @Header(KafkaHeaders.OFFSET) Integer offset,
            final @Header(KafkaHeaders.RECEIVED_KEY) String key,
            final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        System.out.println("test!!!!!!!!!!!!!!!!!");
        logger.info((String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, record.value().toString(), offset, key, partition, topic)));
        //inputMessageService.test(new UserBotStatus(record.value().getChatId(), MyBotStatus.STANDARD));
    }
}

