package com.doctrine7.repository;

import com.doctrine7.model.UserBotStatus;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface UserStatusRepository extends KeyValueRepository<UserBotStatus, Long> {
}
