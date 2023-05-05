package com.doctrine7.service;

import com.doctrine7.model.OutputMessageDto;
import com.doctrine7.model.UserBotStatus;
import com.doctrine7.model.bot.InputMessageDto;
import com.doctrine7.model.bot.MyBotCommand;
import com.doctrine7.model.bot.MyBotStatus;
import com.doctrine7.repository.UserStatusRepository;
import com.doctrine7.service.kafka.KafkaProducerInputMessage;
import com.doctrine7.service.kafka.KafkaProducerOutputMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final UserStatusRepository userStatusRepository;
    private final KafkaProducerOutputMessage kafkaProducerOutputMessage;
    private final KafkaProducerInputMessage kafkaProducerInputMessage;
    private final Logger logger = LoggerFactory.getLogger(InputMessageService.class);
    private final Map<String, MyBotCommand> commands = Arrays.stream(MyBotCommand.values())
            .collect(Collectors.toMap(
                    MyBotCommand::getCommand, command -> command
            ));

    public void processCommand(InputMessageDto message) {
        if (Objects.equals(message.getRawInputCommand(), "/start")) {
            userStatusRepository.save(new UserBotStatus(message.getChatId(), MyBotStatus.STANDARD));
        }
        if (!Objects.equals(message.getRawInputCommand(), "/start") &&
                (message.getUserBlocked() == null || message.getUserExists() == null)) {
            logger.info("input message {} has no fields with the user status and is sent for processing in userservice",
                    message);
            kafkaProducerInputMessage.sendMessage("filling", "key", message);
        } else if (message.getUserBlocked() == Boolean.TRUE) {
            sendOutputMessage(message.getChatId(), "Вы заблокированы и не можете пользоваться ботом");
        } else {
            parseCommand(message);
        }
    }


    public void parseCommand(InputMessageDto message) {
        MyBotCommand command = commands.getOrDefault(message.getRawInputCommand(), MyBotCommand.UNRECOGNIZED);
        message.setBotCommand(command);
        UserBotStatus userStatus = userStatusRepository.findById(message.getChatId())
                .orElse(new UserBotStatus(message.getChatId(), MyBotStatus.STANDARD));
        System.out.println(userStatus.getUserStatus());
        switch (userStatus.getUserStatus()) {
            case STANDARD -> {

                switch (command) {
                    case START -> {
                        message.setBotCommand(MyBotCommand.START);
                        kafkaProducerInputMessage.sendMessage("startCommand", "key", message);
                    }
                    case UNRECOGNIZED -> {
                        logger.info("unrecognized command " + message.getRawInputCommand());
                        sendOutputMessage(message.getChatId(), "Неизвестная команда " + message.getRawInputCommand());
                    }
                    case REG -> {
                        userStatusRepository.save(new UserBotStatus(message.getChatId(), MyBotStatus.REGISTRATION));
                        message.setBotCommand(MyBotCommand.REG);
                        sendOutputMessage(message.getChatId(), """
                                Вы вошли в режим ввода пароля для доступа к данным расписания.
                                Пожалуйста, напишите следующим сообщением пароль или /start для выхода в обычное меню""");
                    }
                    case TODAY -> System.out.println(1); //todo: запрос расписания
                    case TOMORROW -> System.out.println(2); //todo: запрос расписания
                    case THIS_MONTH -> System.out.println(3); //todo: отправка клавиатуры
                    case NEXT_MONTH -> System.out.println(4); //todo: отправка клавиатуры
                    case ADD_EMP -> System.out.println(5); //todo: сменить статус
                    case DEL_EMP -> System.out.println(6); //todo: сменить статус
                    case SEPARATED -> kafkaProducerInputMessage.sendMessage("setSeparatedCommand", "key", message);
                    case ALL_EMPLOYEE -> kafkaProducerInputMessage.sendMessage("allEmployeesCommand", "key", message);
                    default ->
                            throw new IllegalArgumentException("unrecognized command " + message.getRawInputCommand());
                }
            }

            case REGISTRATION -> {
                if (Objects.equals(message.getRawInputCommand(), PasswordChecker.getActualPassword())) {
                    kafkaProducerInputMessage.sendMessage("authenticated", "key", message);
                    userStatusRepository.save(new UserBotStatus(message.getChatId(), MyBotStatus.STANDARD));
                } else {
                    kafkaProducerInputMessage.sendMessage("failAuthenticate", "key", message);
                }
            }

            case DELETING_EMPLOYEE -> {
                System.out.println(10); //todo: вывести список, сменить статус
            }
        }
    }

    private void sendOutputMessage(Long chatId, String text) {
        kafkaProducerOutputMessage.sendMessage("msg", "key", new OutputMessageDto(
                List.of(chatId),
                text
        ));
    }
}
