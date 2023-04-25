package com.doctrine7.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SheduleChangeDto {
    private SheduleDto oldShedule;
    private SheduleDto newShedule;
    private StatusSheduleChanging status;
    private Map<String, List<Long>> telegramIdAndEmployees;

    @Override
    public String toString() {
        return "old shedule: " + oldShedule + "new shedule: " + newShedule + "\n" + "status: " + status.toString();
    }
}
