package com.doctrine7.service;

import com.doctrine7.model.OutputMessageDto;
import com.doctrine7.model.SheduleChangeDto;
import com.doctrine7.model.SheduleDto;
import com.doctrine7.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final KafkaProducer kafkaProducer;

    public void prepareMessage(SheduleChangeDto sheduleChangeDto) {
        if (sheduleChangeDto.getTelegramIdAndEmployees() == null ||
                sheduleChangeDto.getTelegramIdAndEmployees().size() == 0) {
            return;
        }

        switch (sheduleChangeDto.getStatus()) {
            case CREATE -> {
                SheduleDto sheduleDto = sheduleChangeDto.getNewShedule();
                OutputMessageDto outputMessageDto = getCreateMessage(sheduleChangeDto, sheduleDto);
                kafkaProducer.sendMessage(outputMessageDto);
            }
            case CHANGE -> {
                List<OutputMessageDto> outputMessageDtoList = getChangeMessages(sheduleChangeDto);
                outputMessageDtoList.forEach(kafkaProducer::sendMessage);
            }
            case DELETE -> {
                SheduleDto sheduleDto = sheduleChangeDto.getOldShedule();
                OutputMessageDto outputMessageDto = getDeleteMessages(sheduleChangeDto, sheduleDto);
                kafkaProducer.sendMessage(outputMessageDto);
            }
        }
    }

    private OutputMessageDto getCreateMessage(SheduleChangeDto sheduleChangeDto, SheduleDto sheduleDto) {
        OutputMessageDto outputMessageDto = new OutputMessageDto();
        String message = String.format("Пациент %s был добавлен на %s, процедура %s",
                sheduleDto.getPatient(), sheduleDto.getTime(), sheduleDto.getProcedure());
        outputMessageDto.setMessage(message);
        outputMessageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDto.getEmployee()));
        return outputMessageDto;
    }

    private OutputMessageDto getDeleteMessages(SheduleChangeDto sheduleChangeDto, SheduleDto sheduleDto) {

        OutputMessageDto outputMessageDto = new OutputMessageDto();
        outputMessageDto.setMessage(String.format("Удалена процедура %s у %s в %s",
                sheduleDto.getProcedure(), sheduleDto.getPatient(), sheduleDto.getTime()));
        outputMessageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDto.getEmployee()));
        return outputMessageDto;
    }

    private List<OutputMessageDto> getChangeMessages(SheduleChangeDto sheduleChangeDto) {
        var sheduleDtoOld = sheduleChangeDto.getOldShedule();
        var sheduleDtoNew = sheduleChangeDto.getNewShedule();

        List<OutputMessageDto> outputMessageDtoList = new ArrayList<>();

        //изменили комментарий, подтверждение или что-то еще несущественное для отправки
        if (Objects.equals(sheduleDtoOld.getEmployee(), sheduleDtoNew.getEmployee()) &&
                Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient()) &&
                Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure())) {
            return outputMessageDtoList;
        }

        //перенос к другому сотруднику
        if (!Objects.equals(
                sheduleDtoOld.getEmployee(),
                sheduleDtoNew.getEmployee()
        )
        ) {
            outputMessageDtoList.add(getDeleteMessages(sheduleChangeDto, sheduleDtoOld));
            outputMessageDtoList.add(getCreateMessage(sheduleChangeDto, sheduleDtoNew));
            return outputMessageDtoList;
        }

        //изменилось время процедуры
        if (Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()) &&
                Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient())) {
            OutputMessageDto outputMessageDto = new OutputMessageDto();
            outputMessageDto.setMessage(String.format("Пациент %s был перенесён с %s на %s, процедура %s",
                    sheduleDtoOld.getPatient(), sheduleDtoOld.getTime(), sheduleDtoNew.getTime(), sheduleDtoOld.getProcedure()));
            outputMessageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
            return List.of(outputMessageDto);
        }

        //изменилась процедура
        if (Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient()) &&
                !Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()) &&
                Objects.equals(sheduleDtoOld.getTime(), sheduleDtoNew.getTime())) {
            OutputMessageDto outputMessageDto = new OutputMessageDto();
            outputMessageDto.setMessage(String.format("У пациента %s в %s изменилась процедура с %s на %s",
                    sheduleDtoOld.getPatient(), sheduleDtoOld.getTime(), sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()));
            outputMessageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
            return List.of(outputMessageDto);
        }

        //изменилось много параметров и/или пациент, сформируем сообщения сотруднику с указанием что было и что стало
        String message = String.format(
                "Произошли изменения,\n    БЫЛО: процедура %s у %s в %s у пациента %s,\n   СТАЛО: процедура %s у %s в %s у пациента %s",
                sheduleDtoOld.getProcedure(), sheduleDtoOld.getEmployee(), sheduleDtoOld.getTime(), sheduleDtoOld.getPatient(),
                sheduleDtoNew.getProcedure(), sheduleDtoNew.getEmployee(), sheduleDtoNew.getTime(), sheduleDtoNew.getPatient());
        OutputMessageDto outputMessageDto = new OutputMessageDto();
        outputMessageDto.setMessage(message);
        outputMessageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
        return List.of(outputMessageDto);
    }
}
