package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.Endpoints;
import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.entity.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Api(value = "Scraping file API", description = "Scraping file API")
public interface ScrapingApi {
    @ApiOperation(value = "", notes = "Scrap details", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Scraping will begin", response = String.class),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = Endpoints.SCRAPINGS_YEAR, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> scrapDetails(@PathVariable("year") int year);


    @ApiOperation(value = "", notes = "Get scraped file", response = Address.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Scraped file response", response = Address.class, responseContainer = "List"),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = Endpoints.STADIUM_TOWN, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<Address>> getStadiumInTown(@PathVariable("regionPostalCode") int postalCode);


    @ApiOperation(value = "", notes = "Get scraped file", response = Region.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Scraped file response", response = Region.class, responseContainer = "List"),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = Endpoints.REGION_LIST, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<Region>> getRegionList();
}
