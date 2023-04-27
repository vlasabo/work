package com.doctrine7.service;

import com.doctrine7.model.UserBotStatus;
import com.doctrine7.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InputMessageService {
    private final UserStatusRepository userStatusRepository;

    public void test(UserBotStatus botStatus){
        userStatusRepository.save(botStatus);
    }
}
