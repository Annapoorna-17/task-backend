package com.stixis.ems.service;

import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.repository.EmployeeRepository;
import com.stixis.ems.security.MyUserDetailsService;
import com.stixis.ems.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TokenRepository tokenRepository;

    @MockBean
    private AuthService authService;


    @MockBean
    private MyUserDetailsService userService;
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        createSampleEmployee();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEmployee() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.addEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee.getFirstName(), savedEmployee.getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testFindEmployeeById() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeById(1L);

        assertNotNull(foundEmployee);
        assertEquals(employee.getFirstName(), foundEmployee.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindEmployeeByFirstName() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.findByFirstName("John")).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeByFirstName("John");

        assertNotNull(foundEmployee);
        assertEquals(employee.getFirstName(), foundEmployee.getFirstName());
        verify(employeeRepository, times(1)).findByFirstName("John");
    }

    @Test
    void testFindEmployeeByLastName() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.findByLastName("Doe")).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeByLastName("Doe");

        assertNotNull(foundEmployee);
        assertEquals(employee.getLastName(), foundEmployee.getLastName());
        verify(employeeRepository, times(1)).findByLastName("Doe");
    }

    @Test
    void testFindEmployeeByEmail() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.findByEmail("john.doe@example.com")).thenReturn(employee);

        Employee foundEmployee = employeeService.findEmployeeByEmail("john.doe@example.com");

        assertNotNull(foundEmployee);
        assertEquals(employee.getEmail(), foundEmployee.getEmail());
        verify(employeeRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testGetAllEmployees() {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> foundEmployees = employeeService.getAllEmployees();

        assertNotNull(foundEmployees);
        assertEquals(1, foundEmployees.size());
        assertEquals(employee.getFirstName(), foundEmployees.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEmployee() {
        Employee existingEmployee = createSampleEmployee();
        Employee updatedEmployee = createSampleEmployee();
        updatedEmployee.setFirstName("UpdatedJohn");

        when(employeeRepository.findById(updatedEmployee.getEmployeeId())).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(updatedEmployee);

        assertNotNull(result);
        assertEquals(updatedEmployee.getFirstName(), result.getFirstName());
        verify(employeeRepository, times(1)).findById(updatedEmployee.getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = createSampleEmployee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleleEmployee(1L);

        verify(tokenRepository, times(1)).deleteByEmployee_EmployeeId(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllByDateOfJoining() {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);
        when(employeeRepository.findAllByDateOfJoining(any(LocalDate.class))).thenReturn(employeeList);

        List<Employee> foundEmployees = employeeService.findAllByDateOfJoining(LocalDate.now());

        assertNotNull(foundEmployees);
        assertEquals(1, foundEmployees.size());
        assertEquals(employee.getFirstName(), foundEmployees.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAllByDateOfJoining(any(LocalDate.class));
    }

    @Test
    void testFindAllByDateOfJoiningBetween() {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);
        when(employeeRepository.findAllByDateOfJoiningBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(employeeList);

        List<Employee> foundEmployees = employeeService.findAllByDateOfJoiningBetween(LocalDate.now(), LocalDate.now());

        assertNotNull(foundEmployees);
        assertEquals(1, foundEmployees.size());
        assertEquals(employee.getFirstName(), foundEmployees.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAllByDateOfJoiningBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @AfterEach
    void tearDown() {

    }

    private Employee createSampleEmployee() {
        return Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .mobileNumber(1234567890L)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .dateOfJoining(LocalDate.of(2023, 12, 15))
                .role(Role.USER)
                .photo(new byte[]{1, 2, 3})
                .build();
    }
}
