package com.doctrine7.tgbot.service;

import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.tgbot.service.kafka.KafkaProducerInputMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final KafkaProducerInputMessage kafkaProducerInputMessage;
    private final Logger logger = LoggerFactory.getLogger(InputMessageService.class);

    public void addNewMessage(String text, long chatId) {
        InputMessageDto inputMessage = new InputMessageDto();
        inputMessage.setRawInputCommand(text);
        inputMessage.setChatId(chatId);
        logger.info("new input message, sending to Kafka: {}", inputMessage);
        kafkaProducerInputMessage.sendMessage("InputMessageDto", "key", inputMessage);
    }
}
