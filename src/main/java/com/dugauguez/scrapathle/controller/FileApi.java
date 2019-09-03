package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.Endpoints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "File API", description = "File API")
public interface FileApi {

    @ApiOperation(value = "", notes = "Get general files", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "File object response", response = String.class),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = Endpoints.FILES_YEAR, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> getFiles(@PathVariable("year") int year);

}
