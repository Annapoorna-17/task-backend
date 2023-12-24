package com.stixis.ems.controller;

import com.stixis.ems.model.Department;
import com.stixis.ems.repository.DepartmentRepository;
import com.stixis.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/api/department")
    public ResponseEntity<List<Department>> getDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartment());
    }
}
