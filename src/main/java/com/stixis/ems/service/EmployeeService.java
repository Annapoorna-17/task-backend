package com.stixis.ems.service;

import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Employee;
import com.stixis.ems.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.util.codec.binary.Base64.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TokenRepository tokenRepository;

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
    public Employee deleleEmployee(Long id) {
        try {
            Employee deleted = findEmployeeById(id);
            if (deleted != null) {
                tokenRepository.deleteByEmployee_EmployeeId(id);
                employeeRepository.deleteById(id);
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
}
