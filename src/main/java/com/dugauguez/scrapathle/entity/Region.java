package com.dugauguez.scrapathle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "region")
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    private String code = null;
    private String town = null;
    private Double latitude = null;
    private Double longitude = null;
}
