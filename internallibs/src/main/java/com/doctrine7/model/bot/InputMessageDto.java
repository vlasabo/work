package com.doctrine7.model.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InputMessageDto {
    private MyBotCommand botCommand;
    private String rawInputCommand;
    private Long chatId;
    private Boolean isUserBlocked;
    private Boolean isUserExists;

    @Override
    public String toString() {
        return String.format(
                "chatId = %s, rawInputCommand = '%s', botCommand = %s, isUserBlocked = %s, isUserExists=%s",
                chatId, rawInputCommand, botCommand, isUserBlocked, isUserExists
        );
    }
}
