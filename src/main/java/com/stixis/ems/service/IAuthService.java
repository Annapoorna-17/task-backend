package com.stixis.ems.service;

import com.stixis.ems.model.Employee;
import com.stixis.ems.web.AutRequest;
import com.stixis.ems.web.AuthResponse;
import com.stixis.ems.web.RegisterRequest;
import com.stixis.ems.web.resetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IAuthService {

    Employee getUserByEmail(String email);
    List<Employee> getUsers();
    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(AutRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String generatePasswordResetToken(Employee user);


    Map<String, String> resetPassword(resetPasswordRequest request);
}
