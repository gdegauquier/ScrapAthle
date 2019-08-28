package com.dugauguez.scrapathle.controller.impl;

import com.dugauguez.scrapathle.controller.DepartmentApi;
import com.dugauguez.scrapathle.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class DepartmentController implements DepartmentApi {

    @Autowired
    private DepartmentService departmentService;


    public ResponseEntity<List<String>> getDepartmentsCodes() throws IOException {

        List<String> departments = departmentService.getAll();

        if (departments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(departments);
        }

        return ResponseEntity.ok(departments);

    }

}
