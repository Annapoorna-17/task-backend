package com.stixis.ems.controller;

import com.stixis.ems.model.Department;
import com.stixis.ems.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/api/department")
    public ResponseEntity<List<Department>> getDepartments(){
        return ResponseEntity.ok(departmentRepository.findAll());
    }
}
