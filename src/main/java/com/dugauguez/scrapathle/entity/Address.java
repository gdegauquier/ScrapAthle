package com.dugauguez.scrapathle.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("Line1")
    private String line1 = null;

    @JsonProperty("Line2")
    private String line2 = null;

    @JsonProperty("Line3")
    private String line3 = null;

    @JsonProperty("Ville")
    private String ville = null;

    @JsonProperty("Code Postal")
    private String codePostal = null;

    @JsonProperty("type")
    private String type = null;

    private String phone1 = null;

    private Integer eventId;

}
