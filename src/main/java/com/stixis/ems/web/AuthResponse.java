package com.stixis.ems.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
	private String name;
	private String accessToken;
	private String refreshToken;
}
