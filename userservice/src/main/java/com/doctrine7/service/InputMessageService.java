package com.doctrine7.service;

import com.doctrine7.model.OutputMessageDto;
import com.doctrine7.model.User;
import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.service.kafka.KafkaProducerOutputMessage;
import com.doctrine7.service.kafka.KafkaProducerInputMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final UserService userService;
    private final KafkaProducerInputMessage kafkaProducerInputMessage;
    private final KafkaProducerOutputMessage kafkaProducerOutputMessage;

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
        sendOutputMessage(inputMessageDto.getChatId(), user.getSeparatedShedule() ?
                "Теперь вы получаете раздельное расписание" : "Теперь вы получаете расписание подряд");
    }


    public void allEmployees(InputMessageDto inputMessageDto) {
        User user = userService.findById(inputMessageDto.getChatId()).orElseThrow();
        List<String> employeessList = user.getEmployees();
        StringBuilder text = new StringBuilder("Вы получаете расписание для:\n");
        for (int i = 0; i < employeessList.size(); i++) {
            text.append(i + 1).append(". ").append(employeessList.get(i)).append("\n");
        }
        sendOutputMessage(inputMessageDto.getChatId(), employeessList.size() == 0 ?
                "Вы еще не добавили сотрудников" : text.toString());
    }

    private void sendOutputMessage(Long chatId, String text) {
        OutputMessageDto outputMessageDto = new OutputMessageDto();
        outputMessageDto.setUsersId(List.of(chatId));
        outputMessageDto.setMessage(text);
        kafkaProducerOutputMessage.sendMessage("msg", "key", outputMessageDto);
    }
}
