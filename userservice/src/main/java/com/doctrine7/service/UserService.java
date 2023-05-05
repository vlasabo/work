package com.doctrine7.service;

import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.model.StatusSheduleChanging;
import com.doctrine7.model.User;
import com.doctrine7.repository.UserRepository;
import com.doctrine7.service.kafka.KafkaProducerSheduleDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducerSheduleDto kafkaProducerSheduleDto;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void setEmployees(SheduleChangeDto sheduleChangeDto) {
        switch (sheduleChangeDto.getStatus()) {
            case CREATE -> addIdNew(sheduleChangeDto);
            case CHANGE -> {
                addIdNew(sheduleChangeDto);
                addIdOld(sheduleChangeDto);
            }
            case DELETE -> addIdOld(sheduleChangeDto);
            default -> throw new RuntimeException(String.format("incorrect status in %s", sheduleChangeDto));
        }

        if (sheduleChangeDto.getTelegramIdAndEmployees().size() > 0) {
            logger.info("add new kafka message with sheduleChangeDto \n {} and id's: {}",
                    sheduleChangeDto, sheduleChangeDto.getTelegramIdAndEmployees());
            kafkaProducerSheduleDto.sendMessage(sheduleChangeDto);
        }
    }

    private void addIdOld(SheduleChangeDto sheduleChangeDto) {
        String employee = sheduleChangeDto.getOldShedule().getEmployee();
        List<User> usersList = userRepository.findAllByEmployeesContainsAndBannedIsFalseAndBotBannedIsFalse(employee);
        setId(sheduleChangeDto, employee, usersList,
                sheduleChangeDto.getStatus().equals(StatusSheduleChanging.DELETE) ?
                        new HashMap<>() : sheduleChangeDto.getTelegramIdAndEmployees());
    }

    private void addIdNew(SheduleChangeDto sheduleChangeDto) {
        String employee = sheduleChangeDto.getNewShedule().getEmployee();
        List<User> usersList = userRepository.findAllByEmployeesContainsAndBannedIsFalseAndBotBannedIsFalse(employee);
        setId(sheduleChangeDto, employee, usersList, new HashMap<>());
    }

    private void setId(SheduleChangeDto sheduleChangeDto, String employee, List<User> usersList,
                       Map<String, List<Long>> telegramIdAndEmployees) {
        List<Long> usersIds = new ArrayList<>();
        for (User user : usersList) {
            usersIds.add(user.getChatId());
        }
        if (usersIds.size() > 0) {
            telegramIdAndEmployees.put(employee, usersIds);
        }
        sheduleChangeDto.setTelegramIdAndEmployees(telegramIdAndEmployees);
    }

    public void blockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBotBanned(true);
        userRepository.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public void save(User user){
        userRepository.save(user);
    }
}
