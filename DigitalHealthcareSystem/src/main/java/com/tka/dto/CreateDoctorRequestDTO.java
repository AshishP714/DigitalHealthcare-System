package com.tka.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDoctorRequestDTO {

	private String name;
	private String email;
	private String specilization;
}
