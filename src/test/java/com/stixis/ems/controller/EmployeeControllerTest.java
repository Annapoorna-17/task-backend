package com.stixis.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stixis.ems.controller.EmployeeController;
import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.repository.EmployeeRepository;
import com.stixis.ems.security.JwtUtil;
import com.stixis.ems.security.MyUserDetailsService;
import com.stixis.ems.service.AuthService;
import com.stixis.ems.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.stixis.ems.model.Role.ADMIN;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private AuthService authService;

    @MockBean
    private MyUserDetailsService userService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private GrantedAuthority simpleGrantedAuthority;

    @Test
    @WithMockUser
    void testGetAllEmployees() throws Exception {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);

        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value(employee.getFirstName()));
    }

    @Test
    @WithMockUser
    void testGetEmployeeById() throws Exception {
        Employee employee = createSampleEmployee();
        when(employeeService.findEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()));
    }

    @Test
    @WithMockUser
    void testGetEmployeeByFirstName() throws Exception {
        Employee employee = createSampleEmployee();
        when(employeeService.findEmployeeByFirstName("John")).thenReturn(employee);

        mockMvc.perform(get("/api/employee/firstname/John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()));
    }

    @Test
    @WithMockUser
    void testGetEmployeeByLastName() throws Exception {
        Employee employee = createSampleEmployee();
        when(employeeService.findEmployeeByLastName("Doe")).thenReturn(employee);

        mockMvc.perform(get("/api/employee/lastname/Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lastName").value(employee.getLastName()));
    }

    @Test
    @WithMockUser
    void testGetEmployeeByEmail() throws Exception {
        Employee employee = createSampleEmployee();
        when(employeeService.findEmployeeByEmail("john.doe@example.com")).thenReturn(employee);

        mockMvc.perform(get("/api/employee/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(employee.getEmail()));
    }

//    @Test
//    @WithMockUser(username = "r.arunachalam99@gmail.com", password = "123456", roles = "ADMIN")
//    void testUpdateEmployee() throws Exception {
//        Employee employee = createSampleEmployee();
//        employee.setEmployeeId(1L);
//        Employee updatedEmployee = createSampleEmployee();
//        updatedEmployee.setEmployeeId(1L);
//        updatedEmployee.setFirstName("UpdatedJohn");
//
//        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployee);
//
//        mockMvc.perform(put("/api/employee/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updatedEmployee.toString())
//                .andExpect(status().isAccepted())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()));
//
//        // Verify that the updateEmployee method is called with the correct argument
//        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
//    }

    @Test
    @WithMockUser(username = "r.arunachalam99@gmail.com", password = "123456", roles = "ADMIN")
    void testUpdateEmployee() throws Exception {
        Employee employee = createSampleEmployee();
        employee.setEmployeeId(1L);
        Employee updatedEmployee = createSampleEmployee();
        updatedEmployee.setEmployeeId(1L);
        updatedEmployee.setFirstName("UpdatedJohn");

        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployee);
        System.out.println(updatedEmployee.toString());
        mockMvc.perform(put("/api/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployee.toString()))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(updatedEmployee.toString()));

        // Verify that the updateEmployee method is called with the correct argument
        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }


    @Test
    @WithMockUser(username = "r.arunachalam99@gmail.com", password = "123456", roles = "ADMIN")
    void testDeleteEmployee() throws Exception {
        Employee employee = createSampleEmployee();
        employee.setEmployeeId(1L);
        System.out.println(employee);

        when(employeeService.deleleEmployee(employee.getEmployeeId())).thenReturn(employee);

        mockMvc.perform(delete("/api/employee/delete/{id}", employee.getEmployeeId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.employeeId").value(employee.getEmployeeId()))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
                .andReturn();

        verify(employeeService, times(1)).deleleEmployee(employee.getEmployeeId());
        verifyNoMoreInteractions(employeeService);
    }



    @Test
    @WithMockUser(username = "r.arunachalam99@gmail.com",password = "123456",roles = "ADMIN")
    void testFindByDateOfJoining() throws Exception {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);

        when(employeeService.findAllByDateOfJoining(any(LocalDate.class))).thenReturn(employeeList);

        mockMvc.perform(get("/api/employee/date-of-joining/2023-12-15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dateOfJoining").value("15/12/2023"));
    }

    @Test
    @WithMockUser(username = "r.arunachalam99@gmail.com",password = "123456",roles = "ADMIN")
    void testFindAllByDateOfJoiningBetween() throws Exception {
        Employee employee = createSampleEmployee();
        List<Employee> employeeList = Arrays.asList(employee);

        when(employeeService.findAllByDateOfJoiningBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(employeeList);

        mockMvc.perform(get("/api/employee/date-of-joining-between/2023-12-01/2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dateOfJoining").value("15/12/2023"));
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
