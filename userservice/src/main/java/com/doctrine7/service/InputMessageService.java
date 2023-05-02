package com.doctrine7.service;

import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.service.kafka.KafkaProducerInputMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final UserService userService;
    private final KafkaProducerInputMessage kafkaProducerInputMessage;

    public void fill(InputMessageDto inputMessageDto) {
        var userOpt = userService.findById(inputMessageDto.getChatId());
        inputMessageDto.setUserExists(userOpt.isPresent());
        userOpt.ifPresent(user -> inputMessageDto.setUserBlocked(user.isBanned()));
        userOpt.ifPresent(user -> inputMessageDto.setEmployees(user.getEmployees()));
        kafkaProducerInputMessage.sendMessage("InputMessageDto","key", inputMessageDto);
    }
}