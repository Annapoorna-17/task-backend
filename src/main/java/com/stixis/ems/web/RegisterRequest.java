package com.stixis.ems.web;

import com.stixis.ems.model.Department;
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
	private Long mobileNumber;
	private LocalDate dateOfJoining;
	private LocalDate dateOfBirth;
	private Department department;


}
