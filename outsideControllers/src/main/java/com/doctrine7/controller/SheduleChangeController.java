package com.doctrine7.controller;

import com.doctrine7.service.SheduleService;
import com.doctrine7.model.SheduleDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SheduleChangeController {
    private final SheduleService sheduleService;
    private final Logger logger = LoggerFactory.getLogger(SheduleChangeController.class);

    @PostMapping
    @RequestMapping(value = "updateShedule/")
    public void change(@RequestParam(defaultValue = "") String lastEmployee,
                       @RequestParam(defaultValue = "") String lastPatient,
                       @RequestParam(defaultValue = "") String lastProcedure,
                       @RequestParam(defaultValue = "") String lastTime,
                       @RequestParam String employee, @RequestParam String patient,
                       @RequestParam String procedure, @RequestParam String time) {
        logger.info("new request to change the schedule in the controller");
        List<SheduleDto> sheduleList = new ArrayList<>();
        sheduleList.add( //в [0] всегда лежит новая версия расписания
                new SheduleDto(
                        time,
                        employee,
                        patient,
                        procedure
                ));
        if (!(lastEmployee.isBlank() & lastPatient.isBlank() & lastProcedure.isBlank() & lastTime.isBlank())) {
            sheduleList.add( //в [1] лежит старая версия расписания если она была
                    new SheduleDto(
                            lastTime,
                            lastEmployee,
                            lastPatient,
                            lastProcedure
                    ));

        }
        sheduleService.writeProcedureChange(sheduleList);
    }

    @PostMapping
    @RequestMapping(value = "deleteShedule/")
    public void delete(@RequestParam String employee, @RequestParam String patient,
                       @RequestParam String procedure, @RequestParam String time) {
        logger.info("new request to delete a schedule in the controller");
        SheduleDto shedule = new SheduleDto(
                time,
                employee,
                patient,
                procedure
        );
        sheduleService.writeProcedureDelete(List.of(shedule));
    }
}
