package com.stixis.ems;

import com.stixis.ems.model.Department;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.repository.DepartmentRepository;
import com.stixis.ems.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);}
    @Bean
    CommandLineRunner run(EmployeeRepository userDao, PasswordEncoder passwordEncode){
        return args ->{
            if(!userDao.findByRole(Role.ADMIN).isEmpty()) return;

            Employee admin = new Employee();

            admin.setFirstName("Arunachalam");
            admin.setLastName("Rajkumar");
            admin.setEmail("r.arunachalam99@gmail.com");
            admin.setMobileNumber(8884481250L);
            admin.setDateOfBirth(LocalDate.of(1999,11,22));
            admin.setDateOfJoining(LocalDate.of(2023,12,4));
            admin.setPassword(passwordEncode.encode("123456"));
            admin.setPhoto(null);
            admin.setRole(Role.ADMIN);

            userDao.save(admin);
        };
    }
}


