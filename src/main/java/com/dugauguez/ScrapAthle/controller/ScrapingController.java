package com.dugauguez.ScrapAthle.controller;

import com.dugauguez.ScrapAthle.service.FileService;
import com.dugauguez.ScrapAthle.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScrapingController {

    @Autowired
    private ScrapingService scrapingService;

    @GetMapping(value = "/scrapings/2019",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getProducts(){

        scrapingService.getAllByYear(2019);

        return ResponseEntity.ok("Scraping has begun for year "+2019+"...");

    }

}
