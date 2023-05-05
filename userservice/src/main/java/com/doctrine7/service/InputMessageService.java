package com.doctrine7.service;

import com.doctrine7.model.User;
import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.service.kafka.KafkaProducerInputMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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
        kafkaProducerInputMessage.sendMessage("InputMessageDto", "key", inputMessageDto);
    }

    public void startCommand(InputMessageDto inputMessageDto) {
        var userOpt = userService.findById(inputMessageDto.getChatId());
        User user;
        if (userOpt.isEmpty()) {
            user = new User();
            user.setChatId(inputMessageDto.getChatId());
            user.setUserName(inputMessageDto.getUserCredentials().get("username"));
            user.setFirstName(inputMessageDto.getUserCredentials().get("first_name"));
            user.setLastName(inputMessageDto.getUserCredentials().get("last_name"));
            user.setRegisteredAt(Timestamp.valueOf(inputMessageDto.getMessageTime()));
        } else {
            user = userOpt.get();
            user.setBotBanned(false);
        }

        userService.save(user);
    }

    public void setSeparated(InputMessageDto inputMessageDto) {
        User user = userService.findById(inputMessageDto.getChatId()).orElseThrow();
        user.setSeparatedShedule(!user.getSeparatedShedule());
        userService.save(user);
    }
}
