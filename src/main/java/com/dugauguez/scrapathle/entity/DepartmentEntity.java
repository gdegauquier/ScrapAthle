package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

}
