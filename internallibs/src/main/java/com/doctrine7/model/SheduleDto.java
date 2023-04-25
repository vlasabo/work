package com.doctrine7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheduleDto {

	private String time;
	private String employee;
	private String patient;
	private String procedure;

	@Override
	public String toString() {
		return time + " - " + patient + ", " + procedure + "\n";
	}
}
