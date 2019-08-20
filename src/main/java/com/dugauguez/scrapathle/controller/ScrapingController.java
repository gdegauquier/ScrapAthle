package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScrapingController {

    @Autowired
    private ScrapingService scrapingService;

    @GetMapping(value = "/scrapings/{year}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getProducts(@PathVariable("year") int year) {

        scrapingService.getAllByYear(year);

        return ResponseEntity.ok("Scraping has begun for year " + year + "...");

    }

}
