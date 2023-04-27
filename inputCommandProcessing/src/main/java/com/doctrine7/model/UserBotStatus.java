package com.doctrine7.model;

import com.doctrine7.model.bot.MyBotStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("status")
@NoArgsConstructor
@AllArgsConstructor
public class UserBotStatus {
    @Id
    private Long userId;
    private MyBotStatus userStatus;
}
