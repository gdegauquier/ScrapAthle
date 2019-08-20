package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/files/general/{year}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getProducts(@PathVariable("year") int year) {

        fileService.getAllByYear(year);

        return ResponseEntity.ok("Files are being retrieved for year " + year + "...");

    }

}
