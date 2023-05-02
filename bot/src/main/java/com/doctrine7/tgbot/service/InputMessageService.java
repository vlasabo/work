package com.doctrine7.tgbot.service;

import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.tgbot.service.kafka.KafkaProducerInputMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final KafkaProducerInputMessage kafkaProducerInputMessage;
    private final Logger logger = LoggerFactory.getLogger(InputMessageService.class);

    public void addNewMessage(Update update) {
        InputMessageDto inputMessage = new InputMessageDto();
        inputMessage.setRawInputCommand(update.getMessage().getText());
        inputMessage.setChatId(update.getMessage().getChatId());
        var userCredentials = new HashMap<String, String>();
        Chat chat = update.getMessage().getChat();
        userCredentials.put("username", chat.getUserName());
        userCredentials.put("first_name", chat.getFirstName());
        userCredentials.put("last_name", chat.getLastName());
        inputMessage.setUserCredentials(userCredentials);
        long timestamp = (long) update.getMessage().getDate();
        inputMessage.setMessageTime(LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(3)));

        logger.info("new input message, sending to Kafka: {}", inputMessage);
        kafkaProducerInputMessage.sendMessage("InputMessageDto", "key", inputMessage);
    }
}
