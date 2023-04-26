package com.doctrine7.controller;

import com.doctrine7.model.AppointmentsDocument;
import com.doctrine7.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class DocumentUpdateController {
	private final DocumentService documentService;

	@PostMapping
	@RequestMapping(value = "appointment/")
	public void change(
			@RequestParam String id, // номер документа
					   @RequestParam String groupId, //номер группы телеграм для отправки сообщения
					   @RequestParam String isNew,
					   @RequestParam(defaultValue = "") String neurologist,
					   @RequestParam(defaultValue = "") String speechTherapist,
					   @RequestParam(defaultValue = "") String rehabilitologist,
					   @RequestParam(defaultValue = "") String date,
					   @RequestParam(defaultValue = "") String patient,
					   @RequestParam(defaultValue = "") String editor
	) {
		AppointmentsDocument document =
				new AppointmentsDocument(id, patient, date, neurologist, speechTherapist, rehabilitologist,
						editor, Boolean.parseBoolean(isNew), Long.parseLong(groupId));
		documentService.writeDocumentChange(document);
	}
}
