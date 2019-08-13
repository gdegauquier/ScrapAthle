package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.repository.DepartmentRepository;
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
