package com.stixis.ems.controller;

import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Token;
import com.stixis.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;



    // Endpoint to register a new employee
//    @PostMapping("/employee")
//    public ResponseEntity<Employee> employeeRegister(@RequestBody Employee employee) {
//        try {
//            Employee savedEmployee = employeeService.addEmployee(employee);
//            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle exceptions and return an appropriate HTTP status
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // Endpoint to retrieve all employees
    @GetMapping("/employee")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> list = employeeService.getAllEmployees();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to retrieve an employee by ID
    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Employee e = employeeService.findEmployeeById(id);
            return new ResponseEntity<>(e, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to retrieve an employee by first name
    @GetMapping("/employee/firstname/{firstName}")
    public ResponseEntity<Employee> getEmployeeByFirstName(@PathVariable String firstName) {
        try {
            Employee e = employeeService.findEmployeeByFirstName(firstName);
            return new ResponseEntity<>(e, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to retrieve an employee by last name
    @GetMapping("/employee/lastname/{lastName}")
    public ResponseEntity<Employee> getEmployeeByLastName(@PathVariable String lastName) {
        try {
            Employee e = employeeService.findEmployeeByLastName(lastName);
            return new ResponseEntity<>(e, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to retrieve an employee by email
    @GetMapping("/employee/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        try {
            Employee e = employeeService.findEmployeeByEmail(email);
            return new ResponseEntity<>(e, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to update an employee
    @PutMapping("/employee/update")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete an employee by ID
    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
           employeeService.deleleEmployee(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to retrieve employees by date of joining
    @GetMapping("/employee/date-of-joining/{date}")
    public ResponseEntity<List<Employee>> findByDateOfJoining(@PathVariable LocalDate date) {
        try {
            List<Employee> employees = employeeService.findAllByDateOfJoining(date);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to retrieve employees by date of joining between two dates
    @GetMapping("/employee/date-of-joining-between/{startDate}/{endDate}")
    public ResponseEntity<List<Employee>> findAllByDateOfJoiningBetween(
            @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        try {
            List<Employee> employees = employeeService.findAllByDateOfJoiningBetween(startDate, endDate);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate HTTP status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
