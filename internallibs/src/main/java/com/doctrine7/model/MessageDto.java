package com.doctrine7.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDto {
    private List<Long> usersId;
    private String message;

    @Override
    public String toString() {
        return "IDs:" + usersId.toString() + "; Message: " + message;
    }
}
