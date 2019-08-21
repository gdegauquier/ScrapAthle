package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.GoEndpoints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Api(value = "Scraping file API", description = "Scraping file API")
public interface ScrapingApi {
    @ApiOperation(value = "", notes = "Get scraped file", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Scraped file response", response = String.class),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = GoEndpoints.SCRAPINGS_YEAR, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> getProducts(@PathVariable("year") int year);
}
