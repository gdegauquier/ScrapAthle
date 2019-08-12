package com.dugauguez.ScrapAthle.service;

import com.dugauguez.ScrapAthle.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    public List<String> getAll() {

        return departmentRepository.getAll();

    }

}
