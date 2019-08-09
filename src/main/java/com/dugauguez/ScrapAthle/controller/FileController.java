package com.dugauguez.ScrapAthle.controller;

import com.dugauguez.ScrapAthle.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/files/general/2019",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getProducts(){

        fileService.getAllByYear(2019);

        return ResponseEntity.ok("Files are being retrieved...");

    }

}
