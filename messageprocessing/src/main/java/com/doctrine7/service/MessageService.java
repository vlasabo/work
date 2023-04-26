package com.doctrine7.service;

import com.doctrine7.model.MessageDto;
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
                MessageDto messageDto = getCreateMessage(sheduleChangeDto, sheduleDto);
                kafkaProducer.sendMessage(messageDto);
            }
            case CHANGE -> {
                List<MessageDto> messageDtoList = getChangeMessages(sheduleChangeDto);
                messageDtoList.forEach(kafkaProducer::sendMessage);
            }
            case DELETE -> {
                SheduleDto sheduleDto = sheduleChangeDto.getOldShedule();
                MessageDto messageDto = getDeleteMessages(sheduleChangeDto, sheduleDto);
                kafkaProducer.sendMessage(messageDto);
            }
        }
    }

    private MessageDto getCreateMessage(SheduleChangeDto sheduleChangeDto, SheduleDto sheduleDto) {
        MessageDto messageDto = new MessageDto();
        String message = String.format("Пациент %s был добавлен на %s, процедура %s",
                sheduleDto.getPatient(), sheduleDto.getTime(), sheduleDto.getProcedure());
        messageDto.setMessage(message);
        messageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDto.getEmployee()));
        return messageDto;
    }

    private MessageDto getDeleteMessages(SheduleChangeDto sheduleChangeDto, SheduleDto sheduleDto) {

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(String.format("Удалена процедура %s у %s в %s",
                sheduleDto.getProcedure(), sheduleDto.getPatient(), sheduleDto.getTime()));
        messageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDto.getEmployee()));
        return messageDto;
    }

    private List<MessageDto> getChangeMessages(SheduleChangeDto sheduleChangeDto) {
        var sheduleDtoOld = sheduleChangeDto.getOldShedule();
        var sheduleDtoNew = sheduleChangeDto.getNewShedule();

        List<MessageDto> messageDtoList = new ArrayList<>();

        //изменили комментарий, подтверждение или что-то еще несущественное для отправки
        if (Objects.equals(sheduleDtoOld.getEmployee(), sheduleDtoNew.getEmployee()) &&
                Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient()) &&
                Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure())) {
            return messageDtoList;
        }

        //перенос к другому сотруднику
        if (!Objects.equals(
                sheduleDtoOld.getEmployee(),
                sheduleDtoNew.getEmployee()
        )
        ) {
            messageDtoList.add(getDeleteMessages(sheduleChangeDto, sheduleDtoOld));
            messageDtoList.add(getCreateMessage(sheduleChangeDto, sheduleDtoNew));
            return messageDtoList;
        }

        //изменилось время процедуры
        if (Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()) &&
                Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient())) {
            MessageDto messageDto = new MessageDto();
            messageDto.setMessage(String.format("Пациент %s был перенесён с %s на %s, процедура %s",
                    sheduleDtoOld.getPatient(), sheduleDtoOld.getTime(), sheduleDtoNew.getTime(), sheduleDtoOld.getProcedure()));
            messageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
            return List.of(messageDto);
        }

        //изменилась процедура
        if (Objects.equals(sheduleDtoOld.getPatient(), sheduleDtoNew.getPatient()) &&
                !Objects.equals(sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()) &&
                Objects.equals(sheduleDtoOld.getTime(), sheduleDtoNew.getTime())) {
            MessageDto messageDto = new MessageDto();
            messageDto.setMessage(String.format("У пациента %s в %s изменилась процедура с %s на %s",
                    sheduleDtoOld.getPatient(), sheduleDtoOld.getTime(), sheduleDtoOld.getProcedure(), sheduleDtoNew.getProcedure()));
            messageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
            return List.of(messageDto);
        }

        //изменилось много параметров и/или пациент, сформируем сообщения сотруднику с указанием что было и что стало
        String message = String.format(
                "Произошли изменения,\n    БЫЛО: процедура %s у %s в %s у пациента %s,\n   СТАЛО: процедура %s у %s в %s у пациента %s",
                sheduleDtoOld.getProcedure(), sheduleDtoOld.getEmployee(), sheduleDtoOld.getTime(), sheduleDtoOld.getPatient(),
                sheduleDtoNew.getProcedure(), sheduleDtoNew.getEmployee(), sheduleDtoNew.getTime(), sheduleDtoNew.getPatient());
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setUsersId(sheduleChangeDto.getTelegramIdAndEmployees().get(sheduleDtoOld.getEmployee()));
        return List.of(messageDto);
    }
}
