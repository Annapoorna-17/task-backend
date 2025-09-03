package com.stixis.ems.web;


import com.stixis.ems.exceptions.DuplicateEmailException;
import com.stixis.ems.model.Employee;
import com.stixis.ems.service.IAuthService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    IAuthService userService;

    @Autowired
    JavaMailSender mailSender;


    @GetMapping("/hello")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("hello");
    }

//	@PostMapping(value="/register",consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
//	public ResponseEntity<?> register(@RequestPart("request") RegisterRequest request,@RequestPart("photo") MultipartFile photo){
//		try{
//			request.setPhoto(photo.getBytes());
//			return ResponseEntity.ok(userService.register(request));
//		}catch (DuplicateEmailException e){
//			Map<String, String> response = new HashMap<>();
//			response.put("message", e.getMessage());
//			System.out.println();
//			return ResponseEntity.badRequest().body(response);
//		} catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    //	@PostMapping("/register")
//	public ResponseEntity<?> register(@RequestParam("file") MultipartFile file,@RequestParam("request") RegisterRequest request){
//		try{
//
//			request.setPhoto(file.getBytes());
//			return ResponseEntity.ok(userService.register(request));
//
//		}
//		catch (DuplicateEmailException e){
//			Map<String, String> response = new HashMap<>();
//			response.put("message", e.getMessage());
//			System.out.println();
//			return ResponseEntity.badRequest().body(response);
//		}
//		catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws MessagingException, DuplicateEmailException {
        String loginLink = "http://localhost:4200/login";
        Employee e = userService.register(request);
        e.setPassword(request.getPassword());
        String emailContent = createWelcomeMessageContent(e, loginLink);
        sendRegisterEmail(e.getEmail(), emailContent);
        return ResponseEntity.ok(e);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AutRequest request) {
        try {
            return ResponseEntity.ok(userService.authenticate(request));
        } catch (BadCredentialsException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    @CacheEvict(value="employees",allEntries = true)
    public ResponseEntity<?> logout(){

        System.out.println("inside logout");
        return ResponseEntity.ok("Logged out Successfully!!");
    }

    @PostMapping("/refreshToken")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);

    }

//	@PostMapping("/forgotpassword")
//	public ResponseEntity<?> forgotPassword(@RequestParam String email) throws MessagingException {
//		Employee user = userService.getUserByEmail(email);
//		System.out.println(user);
//		Map<String, String> response = new HashMap<>();
//
//		if(user!=null){
//			String jwtToken = userService.generatePasswordResetToken(user);
//
//			String resetLink = "http://localhost:4200/resetPassword?token=" + jwtToken;
//
//
//			String emailContent = createEmailContent(user, resetLink);
//
//			sendEmail(email, emailContent);
//
//			response.put("message", "check ur email!");
//			return ResponseEntity.ok(response);
//		}
//		response.put("message", "email doesn't exist!");
//		return ResponseEntity.badRequest().body(response);
//	}

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws MessagingException {
        Employee user = userService.getUserByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (user != null) {
            String jwtToken = userService.generatePasswordResetToken(user);

            String resetLink = "http://localhost:4200/resetPassword/" + jwtToken;

            String emailContent = createResetEmailContent(user, resetLink);

            sendResetEmail(email, emailContent);

            response.put("message", "Check your email for the password reset link!");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Email address not found!");
        return ResponseEntity.badRequest().body(response);
    }

    private void sendResetEmail(String email, String emailContent) throws MessagingException {

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        messageHelper.setFrom("r.arunachalam99@gmail.com");
        messageHelper.setSubject("Password Reset Mail for Spring");
        messageHelper.setText(emailContent, true);
        messageHelper.setTo(email);

        mailSender.send(mailMessage);
    }

    private void sendRegisterEmail(String email, String emailContent) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        messageHelper.setFrom("r.arunachalam99@gmail.com");
        messageHelper.setSubject("Welcome to Stixis Employee Management System!!");
        messageHelper.setText(emailContent, true);
        messageHelper.setTo(email);
        mailSender.send(mailMessage);


    }

    private static String createResetEmailContent(Employee user, String resetLink) {

        return "<p>Hi " + user.getFirstName() + " " + user.getLastName() + "</p>" +
                "<p>We received a request to reset your password." +
                " If you did not initiate this request, please ignore this email</p>" +
                "<p>To reset your password, click on the link below:</p> <a href=\"" +
                resetLink + "\"> Reset Password</a>" +
                " this link is available just for 15min" +
                "<p>Thank you,<br> Stixis Technologies</p>";

    }

    private static String createWelcomeMessageContent(Employee employee, String loginLink) {
        return "<p> Hi " + employee.getFirstName() + " " + employee.getLastName() + "</p>" +
                "<p>Thank you for registering with us, we look forward to provide you the best service." +
                "<br> Your User id is : "+employee.getEmployeeId() +"<br>"+
                "Please find your Username and Password to use our portal. <br>" +
                "Username : " + employee.getUsername() + "<br>" + "Password : " + employee.getPassword() + "<br>" +
                "<p>To Login your password, click on the link below:</p> <a href=\"" +
                loginLink + "\"> Login link</a>" +
                "<p>Thank you,<br> Stixis Technologies</p>";
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody resetPasswordRequest request) {
        Map<String, String> response = userService.resetPassword(request);
        HttpStatus status = response.get("message").equals("Password successfully updated") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
