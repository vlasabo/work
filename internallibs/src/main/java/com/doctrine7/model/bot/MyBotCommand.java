package com.doctrine7.model.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MyBotCommand {

    START("/start", "Запуск бота"),
    REG("/reg", "Регистрация участника"),
    ADD_EMP("/addemp", "Добавление сотрудника к рассылке"),
    DEL_EMP("/delemp", "Удаление сотрудника из рассылки"),
    TODAY("/today", "Расписание на сегодня"),
    TOMORROW("/tomorrow", "Расписание на завтра"),
    THIS_MONTH("/thismonth", "расписание на текущий месяц"),
    NEXT_MONTH("/nextmonth", "расписание на следующий месяц"),
    ALL_EMPLOYEE("/allemployees", "На кого получаю расписание"),
    SEPARATED("/separated", "Получать расписание раздельно/вместе"),
    UNRECOGNIZED("", "");
    private final String command;
    private final String description;

    }
