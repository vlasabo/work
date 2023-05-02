package com.doctrine7.model.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InputMessageDto {
    private MyBotCommand botCommand;
    private String rawInputCommand;
    private Long chatId;
    private List<String> employees = new ArrayList<>();
    private Boolean UserBlocked;
    private Boolean UserExists;
    private Map<String, String> userCredentials = new HashMap<>();
    private LocalDateTime messageTime;

    @Override
    public String toString() {
        return String.format(
                "chatId = %s, rawInputCommand = '%s', botCommand = %s, isUserBlocked = %s, isUserExists=%s",
                chatId, rawInputCommand, botCommand, UserBlocked, UserExists
        );
    }
}
