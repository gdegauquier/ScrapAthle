package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contact_type")
public class ContactTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String label;


}
