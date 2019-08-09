package com.dugauguez.ScrapAthle.controller;

import com.dugauguez.ScrapAthle.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @GetMapping(value = "/departments",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> getProducts() throws IOException {

        List<String> departments = departmentService.getAll();

        if (departments.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(departments);
        }

        return ResponseEntity.ok(departments);

    }

}
