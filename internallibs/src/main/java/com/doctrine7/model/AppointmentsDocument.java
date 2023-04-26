package com.doctrine7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentsDocument {
	private String id;
	private String patient;
	private String date;
	private String neurologist;
	private String speechTherapist;
	private String rehabilitologist;
	private String editor;
	private Boolean isNew;
	private Long groupIdToNotification;
}
