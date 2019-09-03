package com.dugauguez.scrapathle.controller;

import com.dugauguez.scrapathle.Endpoints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(value = "Department API", description = "Department API")
public interface DepartmentApi {

    @ApiOperation(value = "", notes = "Get departments list", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Departments codes", response = String.class),
                           @ApiResponse(code = 204, message = "No Content", response = Void.class),
                           @ApiResponse(code = 403, message = "Forbidden", response = Void.class)})
    @GetMapping(value = Endpoints.DEPARTMENTS, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<List<String>> getDepartmentsCodes();
}
