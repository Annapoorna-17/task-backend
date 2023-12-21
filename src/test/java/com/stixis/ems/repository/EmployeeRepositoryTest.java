package com.stixis.ems.repository;

import com.stixis.ems.model.Department;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.model.Token;
import com.stixis.ems.security.MyUserDetailsService;
import com.stixis.ems.service.AuthService;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.stixis.ems.model.Role.ADMIN;
import static com.stixis.ems.model.Role.USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @MockBean
    private AuthService authService;

    @MockBean
    private MyUserDetailsService userService;

    private Employee testEmployee1, testEmployee2;

    @BeforeEach
    void setUp() {

        Employee employee = Employee.builder()
                .firstName("Stixis")
                .lastName("Test")
                .email("stixis@mail.com")
                .password("123456")
                .role(USER)
                .tokens(null)
                .dateOfBirth(LocalDate.of(1999, 11, 22))
                .dateOfJoining(LocalDate.of(2023, 12, 15))
                .mobileNumber(8884481250L).build();

        Employee employee1 = Employee.builder()
                .firstName("Ljurex")
                .lastName("law")
                .email("lejurex@mail.com")
                .password("123456")
                .role(ADMIN)
                .tokens(null)
                .dateOfBirth(LocalDate.of(1989, 11, 12))
                .dateOfJoining(LocalDate.of(2023, 12, 20))
                .mobileNumber(7775481250L).build();

        testEmployee1 = employeeRepository.save(employee);
        testEmployee2 = employeeRepository.save(employee1);
    }

    @AfterEach
    void tearDown() {
        employeeRepository.delete(testEmployee1);
        employeeRepository.delete(testEmployee2);
    }

    @Test
    void findByEmail() {
        Employee foundEmployee = employeeRepository.findByEmail(testEmployee1.getEmail());
        assertNotNull(foundEmployee);
        assertEquals(testEmployee1.getEmail(), foundEmployee.getEmail());

    }

    @Test
    void findByRole() {
        List<Employee> foundEmployee=employeeRepository.findByRole(ADMIN);
        assertFalse(foundEmployee.isEmpty());
        foundEmployee.forEach(employee -> Hibernate.initialize(employee.getTokens()));
        foundEmployee.forEach(employee -> {
            System.out.println("Found Employee: " + employee);
        });
        assertTrue(foundEmployee.stream().anyMatch(employee->employee.getEmail().equals(testEmployee2.getEmail())));

    }

    @Test
    void findByFirstName() {
        Optional<Employee> foundEmployee = employeeRepository.findByFirstName(testEmployee1.getFirstName());
        assertTrue(foundEmployee.isPresent());
        assertEquals(testEmployee1.getFirstName(), foundEmployee.get().getFirstName());
    }

    @Test
    void findByLastName() {
        Optional<Employee> foundEmployee = employeeRepository.findByLastName(testEmployee1.getLastName());
        assertTrue(foundEmployee.isPresent());
        assertEquals(testEmployee1.getLastName(), foundEmployee.get().getLastName());
    }

    @Test
    void findAllByDateOfJoining() {
        List<Employee> employeeList = employeeRepository.findAllByDateOfJoining(testEmployee1.getDateOfJoining());
        assertFalse(employeeList.isEmpty());
        assertEquals(testEmployee1.getDateOfJoining(), employeeList.get(0).getDateOfJoining());

    }

    @Test
    void findAllByDateOfJoiningBetween() {
        List<Employee> employeeList = employeeRepository.findAllByDateOfJoiningBetween(testEmployee1.getDateOfJoining(),testEmployee2.getDateOfJoining());
        assertFalse(employeeList.isEmpty());
        assertEquals(testEmployee1.getDateOfJoining(), employeeList.get(0).getDateOfJoining());
        assertEquals(testEmployee2.getDateOfJoining(), employeeList.get(1).getDateOfJoining());
    }
}