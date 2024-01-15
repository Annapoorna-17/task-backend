package com.stixis.ems.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Employee implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long mobileNumber;

    private byte[] photo;

    private boolean deleted = false;

    public String getImageDataAsBase64() {
        if (this.photo != null) {
            return Base64.getEncoder().encodeToString(photo);
        }
        return null;
    }

    public void setImageDataAsBase64(String base64ImageData) {
        if (base64ImageData != null) {
            this.photo = Base64.getDecoder().decode(base64ImageData);
        } else {
            this.photo = null;
        }
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "MM/dd/yyyy")
    private LocalDate dateOfBirth;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "MM/dd/yyyy")
    private LocalDate dateOfJoining;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "employee" ,fetch=FetchType.EAGER)
    private Set<Token> tokens;

    @ManyToOne
    private Department department;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", photo=" + Arrays.toString(photo) +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfJoining=" + dateOfJoining +
                ", role=" + role +
                ", tokens=" + tokens +
                ", department=" + department +
                '}';
    }

//    @Override
//    public String toString() {
//        return String.format(
//                "{\"employeeId\": %d,\"firstName\": \"%s\",\"lastName\": \"%s\",\"email\": \"%s\",\"password\": \"%s\",\"mobileNumber\": %d,\"photo\": %s,\"dateOfBirth\": \"%s\",\"dateOfJoining\": \"%s\",\"role\": \"%s\",\"tokens\": %s,\"department\": %s}",
//                employeeId,
//                escapeJson(firstName),
//                escapeJson(lastName),
//                escapeJson(email),
//                escapeJson(password),
//                mobileNumber,
//                escapeJson(Arrays.toString(photo)),
//                dateOfBirth,
//                dateOfJoining,
//                escapeJson(role.name()),
//                tokens,
//                department
//        );
//    }
//
//    private String escapeJson(String input) {
//        return Objects.isNull(input) ? "" : input.replace("\"", "\\\"");
//    }
}
