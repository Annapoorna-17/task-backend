package com.stixis.ems.repository;

import com.stixis.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find an employee by email
    Optional<Employee> findByEmail(String email);

    // Find an employee by first name
    Optional<Employee> findByFirstName(String firstName);

    // Find an employee by last name
    Optional<Employee> findByLastName(String lastName);

    // Find all employees by date of joining
    List<Employee> findAllByDateOfJoining(LocalDate date);

    // Find all employees by date of joining between two dates
    List<Employee> findAllByDateOfJoiningBetween(LocalDate startDate, LocalDate endDate);

}
