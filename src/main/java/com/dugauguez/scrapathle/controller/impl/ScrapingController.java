package com.dugauguez.scrapathle.controller.impl;

import com.dugauguez.scrapathle.controller.ScrapingApi;
import com.dugauguez.scrapathle.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScrapingController implements ScrapingApi {

    @Autowired
    private ScrapingService scrapingService;

    public ResponseEntity<String> getProducts(@PathVariable("year") int year) {

        scrapingService.getAllByYear(year);

        return ResponseEntity.ok("Scraping has begun for year " + year + "...");

    }

}
