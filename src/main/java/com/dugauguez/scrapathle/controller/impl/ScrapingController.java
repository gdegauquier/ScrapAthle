package com.dugauguez.scrapathle.controller.impl;

import com.dugauguez.scrapathle.controller.ScrapingApi;
import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ScrapingController implements ScrapingApi {

    @Autowired
    private ScrapingService scrapingService;

    public ResponseEntity<String> scrapDetails(@PathVariable("year") int year) {

        scrapingService.getAllByYear(year);

        return ResponseEntity.ok("Scraping has begun for year " + year + "...");

    }


    public ResponseEntity<List<Address>> getStadiumInTown(@PathVariable("regionPostalCode") int regionPostalCode) {
        List<Address> addresses = scrapingService.stadiumInTown(regionPostalCode);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }
}
