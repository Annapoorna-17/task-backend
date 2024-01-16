package com.stixis.ems.service;

import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.helper.ExcelHelper;
import com.stixis.ems.model.Employee;
import com.stixis.ems.repository.DepartmentRepository;
import com.stixis.ems.repository.EmployeeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.util.codec.binary.Base64.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    JavaMailSender mailSender;

    public EmployeeService(EmployeeRepository employeeRepository,TokenRepository tokenRepository) {
        this.employeeRepository = employeeRepository;
        this.tokenRepository=tokenRepository;
    }



    /**
     * Add a new employee.
     *
     * @param employee The employee to be added.
     * @return The added employee.
     */
    public Employee addEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add employee", e);
        }
    }

    /**
     * Get an employee by ID.
     *
     * @param id The ID of the employee to retrieve.
     * @return The employee with the specified ID.
     */
    public Employee findEmployeeById(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            return employee.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee by ID", e);
        }
    }

    /**
     * Get List of employees by Department ID.
     *
     * @param id The ID of the department to retrieve.
     * @return The List of employees with the specified Department ID.
     */
    public List<Employee> findEmployeeByDepartmentId(Long id){
        try {
            List<Employee> employees = employeeRepository.findByDepartment_DepartmentId(id);
            employees.forEach(employee -> {
                if (employee.getPhoto() != null) {
                    employee.setImageDataAsBase64(java.util.Base64.getEncoder().encodeToString(employee.getPhoto()));
                }
            });
            return employees;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all employees", e);
        }
    }
    /**
     * Get an employee by first name.
     *
     * @param firstName The first name of the employee to retrieve.
     * @return The employee with the specified first name.
     */
    public Employee findEmployeeByFirstName(String firstName) {
        try {
            Optional<Employee> employee = employeeRepository.findByFirstName(firstName);
            return employee.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee by first name", e);
        }
    }

    /**
     * Get an employee by last name.
     *
     * @param lastName The last name of the employee to retrieve.
     * @return The employee with the specified last name.
     */
    public Employee findEmployeeByLastName(String lastName) {
        try {
            Optional<Employee> employee = employeeRepository.findByLastName(lastName);
            return employee.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee by last name", e);
        }
    }

    /**
     * Get an employee by email.
     *
     * @param email The email of the employee to retrieve.
     * @return The employee with the specified email.
     */
    public Employee findEmployeeByEmail(String email) {
        try {
            return employeeRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employee by email", e);
        }
    }

    /**
     * Get all employees.
     *
     * @return List of all employees.
     */
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            employees.forEach(employee -> {
                if (employee.getPhoto() != null) {
                    employee.setImageDataAsBase64(java.util.Base64.getEncoder().encodeToString(employee.getPhoto()));
                }
            });
            return employees;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all employees", e);
        }
    }

    /**
     * Update an existing employee.
     *
     * @param employee The employee to be updated.
     * @return The updated employee.
     */
    @CacheEvict(value="employees" ,allEntries=true)
    public Employee updateEmployee(Employee employee) {
        try {
            Employee e1 = findEmployeeById(employee.getEmployeeId());
            if (e1 != null) {
                return employeeRepository.save(employee);
            } else {
                throw new RuntimeException("Employee not found for updating");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    /**
     * Delete an employee by ID.
     *
     * @param id The ID of the employee to be deleted.
     * @return The deleted employee.
     */
    @Transactional
    @CacheEvict(value="employees" ,allEntries=true)
    public Employee deleleEmployee(Long id) {
        try {
            Employee deleted = findEmployeeById(id);
            if (deleted != null) {
//                tokenRepository.deleteByEmployee_EmployeeId(id);
                deleted.setDeleted(true);
                return deleted;
            } else {
                throw new RuntimeException("Employee not found for deletion");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    /**
     * Find employees by date of joining.
     *
     * @param dateOfJoining The date of joining to search for.
     * @return List of employees with the specified date of joining.
     */
    public List<Employee> findAllByDateOfJoining(LocalDate dateOfJoining) {
        try {
            return employeeRepository.findAllByDateOfJoining(dateOfJoining);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employees by date of joining", e);
        }
    }

    /**
     * Find employees by date of joining between a range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return List of employees with the date of joining between the specified range.
     */
    public List<Employee> findAllByDateOfJoiningBetween(LocalDate startDate, LocalDate endDate) {
        try {
            return employeeRepository.findAllByDateOfJoiningBetween(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve employees by date of joining between", e);
        }
    }

    @CacheEvict(value="employees",allEntries = true)
    public void save(MultipartFile file) {
        try{

            List<Employee> list = ExcelHelper.convertExcelToList(file.getInputStream(),departmentRepository);
            list.forEach(employee -> {
                String userPassword;
                userPassword = "!"+employee.getFirstName().toUpperCase()+"@"+employee.getDateOfBirth().getYear()+"$";
                String encodedPassword= passwordEncoder.encode(userPassword);
                employee.setPassword(encodedPassword);
            });
            sendWelcomeMailToImportedEmployees(list);
            employeeRepository.saveAll(list);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendWelcomeMailToImportedEmployees(List<Employee> list) {
        String loginLink = "http://localhost:4200/login";
        list.forEach(employee->{
//            String emailContent = createWelcomeMessageContent(employee, loginLink);
//            try {
//                sendRegisterEmail(employee.getEmail(), emailContent);
//            } catch (MessagingException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println("mail sent to -"+employee.getEmail());

        });

    }


    private static String createWelcomeMessageContent(Employee employee, String loginLink) {
        return "<p> Hi " + employee.getFirstName() + " " + employee.getLastName() + "</p>" +
                "<p>Thank you for registering with us, we look forward to provide you the best service." +
                "<br> Your User id is : "+employee.getEmployeeId() +"<br>"+
                "Please find your Username and Password to use our portal. <br>" +
                "Username : " + employee.getUsername() + "<br>" + "Password : " +"!(Your First name in caps)@(Your Birth Year)$"  + "<br>" +
                "<p><b>Note :-<b>It is recommended to reset you password after logging in the first time.</p>"+
                "<p>To Login in our website, click on the link below:</p><br>"+
                "<a href=\"" +
                loginLink + "\"> Login link</a>" +
                "<p>Thank you,<br> Stixis Technologies</p>";
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

    public ByteArrayInputStream getExcelSheet() throws IOException {
        List<Employee> all=employeeRepository.findAll();
        return ExcelHelper.convertListToExcel(all);

    }
}
