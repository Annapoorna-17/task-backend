package com.stixis.ems.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "token")
    @Entity
    public class Token {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;
        private String type;
        private boolean expired;
        private boolean revoked;

        @ManyToOne
        @JoinColumn(name = "emp_id")
        private Employee employee;
    }

