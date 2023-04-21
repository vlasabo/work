package com.doctrine7.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SheduleChangeDto {
    private SheduleDto oldShedule;
    private SheduleDto newShedule;
    private StatusSheduleChanging status;

    @Override
    public String toString() {
        return "old shedule: " + oldShedule + "; new shedule: " + newShedule + "; status: " + status.toString();
    }
}