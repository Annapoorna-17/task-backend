package com.stixis.ems.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.exceptions.DuplicateEmailException;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.model.Token;
import com.stixis.ems.repository.EmployeeRepository;
import com.stixis.ems.security.JwtUtil;
import com.stixis.ems.web.AutRequest;
import com.stixis.ems.web.AuthResponse;
import com.stixis.ems.web.RegisterRequest;
import com.stixis.ems.web.resetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Service
public class AuthService implements IAuthService{
    @Autowired
    EmployeeRepository userDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    TokenRepository tokenDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public Employee getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }


    @Override
    public List<Employee> getUsers() {
        return userDao.findAll();
    }


    private void revokeUserToken(Employee user){
        Set<Token> validTokens = tokenDao.findValidTokenByUser(user.getEmployeeId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(token->{
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenDao.saveAll(validTokens);
    }

    private void saveUserToken(Employee employee, String jwtToken) {

        Token tokenEntity = new Token();
        tokenEntity.setToken(jwtToken);
        tokenEntity.setType("BEARER");
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
        tokenEntity.setEmployee(employee);
        tokenDao.save(tokenEntity);
    }

    private Employee createUser(RegisterRequest request) {
       Employee user = new Employee();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setDateOfJoining(request.getDateOfJoining());
        user.setDepartment(request.getDepartment());
        user.setPhoto(request.getPhoto());
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(Role.USER);
        return user;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        Employee user = createUser(request);
        try{
            Employee newUser = userDao.save(user);
            String jwtToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            revokeUserToken(user);
            saveUserToken(newUser, jwtToken);

            return new AuthResponse(jwtToken,refreshToken);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException("Email "+request.getEmail()+" already exists!");
        }
    }

    @Override
    public AuthResponse authenticate(AutRequest request) {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),request.getPassword()
                    )
            );
            Employee user = userDao.findByEmail(request.getEmail());
            String jwtToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            revokeUserToken(user);
            saveUserToken(user,jwtToken);

            return new AuthResponse(jwtToken,refreshToken);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid email or password!");
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            refreshToken = authorizationHeader.substring(7);

            username = jwtUtil.getUsernameFromToken(refreshToken);
            if (username != null) {

                Employee user = userDao.findByEmail(username);
                if (jwtUtil.validateToken(refreshToken, user)) {
                    String newToken = jwtUtil.generateToken(user);
                    revokeUserToken(user);
                    saveUserToken(user,newToken);

                    AuthResponse authResponse= new AuthResponse();
                    authResponse.setAccessToken(newToken);
                    authResponse.setRefreshToken(refreshToken);

                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
                }
            }

        }
    }

    @Override
    public String generatePasswordResetToken(Employee user) {

        String jwtToken = jwtUtil.generateToken(user);

        revokeUserToken(user);
        saveUserToken(user,jwtToken);

        return jwtToken;
    }

    @Override
    public Map<String, String> resetPassword(resetPasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        String email = jwtUtil.getUsernameFromToken(request.getToken());
        if (email != null) {
            Employee user = userDao.findByEmail(email);
            if(jwtUtil.validateToken(request.getToken(), user)
                    && !tokenDao.findByToken(request.getToken()).isExpired()
                    && !tokenDao.findByToken(request.getToken()).isRevoked()){
                String encryptedPassword = passwordEncoder.encode(request.getPassword());
                user.setPassword(encryptedPassword);
                revokeUserToken(user);
                userDao.save(user);
                response.put("message", "Password successfully updated");
                return response;
            }else {
                response.put("message", "Invalid token");
                return response;
            }
        }
        response.put("message", "No access");
        return response;
    }
}
