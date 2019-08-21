package com.dugauguez.scrapathle.controller.impl;

import com.dugauguez.scrapathle.controller.FileApi;
import com.dugauguez.scrapathle.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController implements FileApi {

    @Autowired
    private FileService fileService;

    public ResponseEntity<String> getProducts(@PathVariable("year") int year) {

        fileService.getAllByYear(year);

        return ResponseEntity.ok("Files are being retrieved for year " + year + "...");

    }

}
