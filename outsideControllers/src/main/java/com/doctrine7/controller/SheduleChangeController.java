package com.doctrine7.controller;

import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.model.StatusSheduleChanging;
import com.doctrine7.service.SheduleService;
import com.doctrine7.model.SheduleDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
        SheduleChangeDto sheduleChangeDto = new SheduleChangeDto();
        sheduleChangeDto.setStatus(StatusSheduleChanging.CREATE);
        sheduleChangeDto.setNewShedule(new SheduleDto(
                time,
                employee,
                patient,
                procedure
        ));
        if (!(lastEmployee.isBlank() & lastPatient.isBlank() & lastProcedure.isBlank() & lastTime.isBlank())) {
            sheduleChangeDto.setOldShedule(
                    new SheduleDto(
                            lastTime,
                            lastEmployee,
                            lastPatient,
                            lastProcedure
                    ));
            sheduleChangeDto.setStatus(StatusSheduleChanging.CHANGE);
        }

        sheduleService.writeProcedureChange(sheduleChangeDto);
    }

    @PostMapping
    @RequestMapping(value = "deleteShedule/")
    public void delete(@RequestParam String employee, @RequestParam String patient,
                       @RequestParam String procedure, @RequestParam String time) {
        logger.info("new request to delete a schedule in the controller");
        SheduleChangeDto sheduleChangeDto = new SheduleChangeDto();
        sheduleChangeDto.setStatus(StatusSheduleChanging.DELETE);
        sheduleChangeDto.setOldShedule(new SheduleDto(
                time,
                employee,
                patient,
                procedure
        ));

        sheduleService.writeProcedureDelete(sheduleChangeDto);
    }
}
