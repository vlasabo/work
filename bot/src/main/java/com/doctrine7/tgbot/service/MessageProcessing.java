package com.doctrine7.tgbot.service;

import com.doctrine7.model.MessageDto;
import com.doctrine7.tgbot.model.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageProcessing {
    private final TelegramBot telegramBot;

    public void sendMessage(MessageDto message) {
        var text = message.getMessage();
        Map<Long, DeliveryStatus> result = new HashMap<>();
        for (Long userId : message.getUsersId()) {
            var resultMessageSending = telegramBot.sendMessageToId(userId, text);
            result.put(userId, resultMessageSending);
            System.out.println(result);
        }

        for (Map.Entry<Long, DeliveryStatus> entryset : result.entrySet()) {
            if (DeliveryStatus.BLOCKED.equals(entryset.getValue())){
                markUserAsUnavailable(entryset.getKey());
            }
        }
    }

    private void markUserAsUnavailable(Long key) {

    }
}
