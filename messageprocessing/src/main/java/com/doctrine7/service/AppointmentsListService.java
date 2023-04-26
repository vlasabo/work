package com.doctrine7.service;

import com.doctrine7.model.AppointmentsDocument;
import com.doctrine7.model.MessageDto;
import com.doctrine7.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentsListService {
    private final String NEW_DOCUMENT = "Создан новый лист назначений ";
    private final String OLD_DOCUMENT = "Отредактирован существующий лист назначений № ";
    private final KafkaProducer kafkaProducer;

    public void prepareNotifications(AppointmentsDocument document) {
        Long groupId = document.getGroupIdToNotification();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime documentDateTime = LocalDateTime.parse(document.getDate(), dtf);

        var changeFlag = documentDateTime.plusMinutes(5).isBefore(LocalDateTime.now()); //пять минут врачу на изменения
        if (changeFlag) {
            sendChangeNotify(document, groupId);
            return;
        }
        if (document.getIsNew()) {
            sendCreateNotify(document, groupId);
        }
    }

    private void sendCreateNotify(AppointmentsDocument document, long groupId) {
        StringBuilder messageText = new StringBuilder(NEW_DOCUMENT);
        messageText.append(" на пациента ")
                .append(document.getPatient())
                .append(". Врачи: \n")
                .append("невролог: ")
                .append("".equals(document.getNeurologist()) ? "-" : document.getNeurologist()).append("\n")
                .append("реабилитолог: ")
                .append("".equals(document.getRehabilitologist()) ? "-" : document.getRehabilitologist()).append("\n")
                .append("логопед: ")
                .append("".equals(document.getSpeechTherapist()) ? "-" : document.getSpeechTherapist()).append("\n")
                .append("Создал ").append(document.getEditor());

        sendMessage(groupId, messageText);
    }

    private void sendChangeNotify(AppointmentsDocument document, long groupId) {
        StringBuilder messageText = new StringBuilder(OLD_DOCUMENT);
        messageText.append(document.getId())
                .append(" на пациента ")
                .append(document.getPatient()).append("\n")
                .append("Отредактировал ").append(document.getEditor());

        sendMessage(groupId, messageText);
    }

    private void sendMessage(long groupId, StringBuilder messageText) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(messageText.toString());
        messageDto.setUsersId(List.of(groupId));
        kafkaProducer.sendMessage(messageDto);
    }
}
