package com.doctrine7.service;

import com.doctrine7.model.UserBotStatus;
import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.model.bot.MyBotCommand;
import com.doctrine7.model.bot.MyBotStatus;
import com.doctrine7.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final UserStatusRepository userStatusRepository;
    private final Logger logger = LoggerFactory.getLogger(InputMessageService.class);
    private final Map<String, MyBotCommand> commands = Arrays.stream(MyBotCommand.values())
            .collect(Collectors.toMap(
                    MyBotCommand::getCommand, command -> command
            ));

    public void processCommand(InputMessageDto message) {
        if (!Objects.equals(message.getRawInputCommand(), "/start") &&
                (message.getUserBlocked() == null || message.getUserExists() == null)) {
            logger.info("input message {} has no fields with the user status and is sent for processing in userservice",
                    message);
            //todo: отправить в юзерсервис, там же заполнить employees
        } else if (message.getUserBlocked() == Boolean.TRUE) {
            //todo: отправить сообщение что человек заблокирован
        } else {
            parseCommand(message);
        }
    }


    public void parseCommand(InputMessageDto message) {
        MyBotCommand command = commands.getOrDefault(message.getRawInputCommand(), MyBotCommand.UNRECOGNIZED);
        message.setBotCommand(command);
        UserBotStatus userStatus = userStatusRepository.findById(message.getChatId())
                .orElse(new UserBotStatus(message.getChatId(), MyBotStatus.STANDARD));
        switch (userStatus.getUserStatus()) {
            case STANDARD -> {

                switch (command) {
                    case START -> {
                        userStatusRepository.save(new UserBotStatus(message.getChatId(), MyBotStatus.STANDARD));
                        //todo: отправить в юзерсервис
                    }
                    case UNRECOGNIZED -> System.out.println(); //todo: отправить сообщение что команда не распознана
                    case TODAY -> System.out.println(1); //todo: запрос расписания
                    case TOMORROW -> System.out.println(2); //todo: запрос расписания
                    case THIS_MONTH -> System.out.println(3); //todo: отправка клавиатуры
                    case NEXT_MONTH -> System.out.println(4); //todo: отправка клавиатуры
                    case ADD_REG -> System.out.println(5); //todo: сменить статус
                    case DEL_REG -> System.out.println(6); //todo: сменить статус
                    case SEPARATED -> System.out.println(7); //todo: в юзерсервис
                    case ALL_EMPLOYEE -> System.out.println(8); //todo: в юзерсервис
                    default ->
                            throw new IllegalArgumentException("unrecognized command " + message.getRawInputCommand());
                }
            }

            case REGISTRATION -> {
                System.out.println(9); //todo: проверка пароля, потом в юзерсервис, потом сменить статус
            }

            case DELETING_EMPLOYEE -> {
                System.out.println(10); //todo: вывести список, сменить статус
            }
        }
    }
}
