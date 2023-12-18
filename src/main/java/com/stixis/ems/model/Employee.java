package com.stixis.ems.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

       private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long mobileNumber;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
    private LocalDate dateOfJoining;


    @ManyToOne
    private Department department;


}
