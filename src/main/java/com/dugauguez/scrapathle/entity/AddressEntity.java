package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String line1;
    private String line2;
    private String line3;

    @ManyToOne
    @JoinColumn(name="town_id")
    private TownEntity town;

    @ManyToOne
    @JoinColumn(name="address_type_id")
    private AddressTypeEntity type;


}
