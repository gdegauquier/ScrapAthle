package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test_category")
public class TestCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String label;

}
