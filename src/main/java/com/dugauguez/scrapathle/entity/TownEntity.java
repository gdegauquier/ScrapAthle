package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "town")
public class TownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String zipcode;

    private String inseecode;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;


}
