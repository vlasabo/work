package com.doctrine7.tgbot.service;

import com.doctrine7.model.MessageDto;
import com.doctrine7.tgbot.model.DeliveryStatus;
import com.doctrine7.tgbot.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageProcessing {
    private final TelegramBot telegramBot;
    private final KafkaProducer kafkaProducer;
    private final Logger logger = LoggerFactory.getLogger(MessageProcessing.class);

    public void sendMessage(MessageDto message) {
        var text = message.getMessage();
        Map<Long, DeliveryStatus> result = new HashMap<>();
        for (Long userId : message.getUsersId()) {
            var resultMessageSending = telegramBot.sendMessageToId(userId, text);
            result.put(userId, resultMessageSending);
            System.out.println(result);
        }

        for (Map.Entry<Long, DeliveryStatus> entryset : result.entrySet()) {
            if (DeliveryStatus.BLOCKED.equals(entryset.getValue())) {
                markUserAsUnavailable(entryset.getKey());
            }
        }
    }

    private void markUserAsUnavailable(Long userId) {
        try {
            SendResult<String, Long> result = kafkaProducer.sendMessage("blockedByUser", "key2", userId).get();
            logger.info(String.format("blocked user %s, offset %s", userId, result.getRecordMetadata().offset()));
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
            logger.error(e.getMessage());
        }
    }
}
