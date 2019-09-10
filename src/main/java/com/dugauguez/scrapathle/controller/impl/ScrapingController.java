package com.dugauguez.scrapathle.controller.impl;

import com.dugauguez.scrapathle.controller.ScrapingApi;
import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.entity.Region;
import com.dugauguez.scrapathle.service.ScrapingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
public class ScrapingController implements ScrapingApi {

    @Autowired
    private ScrapingService scrapingService;

    public ResponseEntity<String> scrapDetails(@PathVariable("year") int year) {

        scrapingService.getAllByYear(year);

        return ResponseEntity.ok("Scraping has begun for year " + year + "...");

    }


    public ResponseEntity<List<Address>> getStadiumInTown(@PathVariable("regionPostalCode") int regionPostalCode) {
        List<Address> addresses = scrapingService.StadiumInTown(regionPostalCode);
        log.info("StadiumInTown : " + addresses.size());
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    public ResponseEntity<List<Region>> getRegionList() {
        List<Region> regionList = scrapingService.getRegionList();
        return ResponseEntity.status(HttpStatus.OK).body(regionList);
    }
}
