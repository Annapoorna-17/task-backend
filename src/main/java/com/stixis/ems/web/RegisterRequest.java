package com.stixis.ems.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stixis.ems.model.Department;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	@Lob
	private byte[] photo;

	private Long mobileNumber;

	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
	private LocalDate dateOfBirth;

	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
	private LocalDate dateOfJoining;

	private Department department;


}
