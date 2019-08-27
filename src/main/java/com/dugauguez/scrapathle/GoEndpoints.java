package com.dugauguez.scrapathle;

public abstract class GoEndpoints {

    public static final String ROOT = "/";
    public static final String SWAGGER = "/swagger";
    public static final String SWAGGER_API_DOCS = "/v2/api-docs";
    public static final String SWAGGER_WEBJARS = "/webjars/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String SWAGGER_UI = "/swagger-ui.html";

    public static final String DEPARTMENTS = "/departments";
    public static final String FILES_YEAR = "/files/general/{year}";
    public static final String SCRAPINGS_YEAR = "/scrapings/{year}";
    public static final String STADIUM_TOWN = "/stadium/{regionPostalCode}";


}
