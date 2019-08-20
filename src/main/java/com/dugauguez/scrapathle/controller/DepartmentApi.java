package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.GoEndpoints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Api(value = "Department API", description = "Department API")
public interface DepartmentApi {

    @ApiOperation(value = "", notes = "Get department list", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Batch object response", response = String.class),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = GoEndpoints.DEPARTMENTS, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<String>> getProducts() throws IOException;
}
