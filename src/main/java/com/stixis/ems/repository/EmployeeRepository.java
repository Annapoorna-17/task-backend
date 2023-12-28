package com.stixis.ems.repository;

import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e")
    List<Employee> findAll();
    // Find an employee by email
    Employee findByEmail(String email);

    List<Employee> findByDepartment_DepartmentId(Long id);

    List<Employee> findByRole(Role role);

    // Find an employee by first name
    Optional<Employee> findByFirstName(String firstName);

    // Find an employee by last name
    Optional<Employee> findByLastName(String lastName);

    // Find all employees by date of joining
    List<Employee> findAllByDateOfJoining(LocalDate date);

    // Find all employees by date of joining between two dates
    List<Employee> findAllByDateOfJoiningBetween(LocalDate startDate, LocalDate endDate);

}
