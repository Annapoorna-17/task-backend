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

@SpringBootApplication
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);}
    @Bean
    CommandLineRunner run(EmployeeRepository userDao, PasswordEncoder passwordEncode){
        return args ->{
            if(userDao.findByRole(Role.ADMIN)!=null) return;

            Employee admin = new Employee();

            admin.setFirstName("Arunachalam");
            admin.setLastName("Rajkumar");
            admin.setEmail("r.arunachalam@gmail.com");
            admin.setPassword(passwordEncode.encode("Stixis@123"));
            admin.setRole(Role.ADMIN);

            userDao.save(admin);
        };
    }
}


