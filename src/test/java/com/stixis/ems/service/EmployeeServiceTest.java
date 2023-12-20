package com.stixis.ems.service;

import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Department;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.repository.EmployeeRepository;
import lombok.experimental.ExtensionMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.stixis.ems.model.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtensionMethod(MockitoExtension.class)
class EmployeeServiceTest {
    private Employee e;

    @BeforeEach
    void setUp() {
        e.setFirstName("Aruncahalam");
        e.setLastName("Rajkumar");
        e.setMobileNumber(8884481250L);
        e.setEmail("r.arunachalam99@gmail.com");
        e.setPassword("123456");
        e.setDateOfJoining(LocalDate.of(2023,12,04));
        e.setDateOfBirth(LocalDate.of(1999,11,22));
        

    }

    @Test
    void addEmployee() {
        EmployeeService em = new EmployeeService(mock(EmployeeRepository.class),mock(TokenRepository.class));
    }

    @Test
    void findEmployeeById() {
    }

    @Test
    void findEmployeeByFirstName() {
    }

    @Test
    void findEmployeeByLastName() {
    }

    @Test
    void findEmployeeByEmail() {
    }

    @Test
    void getAllEmployees() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleleEmployee() {
    }

    @Test
    void findAllByDateOfJoining() {
    }

    @Test
    void findAllByDateOfJoiningBetween() {
    }
}