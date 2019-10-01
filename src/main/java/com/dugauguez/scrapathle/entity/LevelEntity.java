package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "level")
public class LevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String label;

}
