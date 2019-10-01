package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.repository.DepartmentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentReader departmentReader;

    public List<String> getAll() {

        return departmentReader.getAll();

    }

}
