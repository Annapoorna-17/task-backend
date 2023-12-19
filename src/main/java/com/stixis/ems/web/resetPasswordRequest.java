package com.stixis.ems.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class resetPasswordRequest {
	private String token;
	private String password;
	private String confirmPassword;
}
