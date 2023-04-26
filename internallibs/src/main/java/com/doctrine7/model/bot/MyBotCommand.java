package com.doctrine7.model.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MyBotCommand {

    START("/start", "Регистрация участника"),
    ADD_REG("/addreg", "Добавление сотрудника к рассылке"),
    DEL_REG("/delreg", "Удаление сотрудника из рассылки"),
    TODAY("/today", "Расписание на сегодня"),
    TOMORROW("/tomorrow", "Расписание на завтра"),
    THIS_MONTH("/thismonth", "расписание на текущий месяц"),
    NEXT_MONTH("/nextmonth", "расписание на следующий месяц"),
    ALL_EMPLOYEE("/allemployees", "На кого получаю расписание"),
    SEPARATED("/separated", "Получать расписание раздельно/вместе");
    private final String command;
    private final String description;

    }
