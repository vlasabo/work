package com.doctrine7.tgbot.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonSerialize
public class Message {
    private List<Long> usersId;
    private String message;

    @Override
    public String toString() {
        return "IDs:" + usersId.toString() + "; Message: " + message;
    }
}
