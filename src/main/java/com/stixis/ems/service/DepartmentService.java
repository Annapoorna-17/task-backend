package com.stixis.ems.service;

import com.stixis.ems.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
   @Autowired
    private DepartmentRepository departmentRepository;
}
