package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contact")
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;
    private String lastname;
    private String name;
    @Column(name = "telephone_1")
    private String telephone1;
    @Column(name = "telephone_2")
    private String telephone2;
    private String email;

    @ManyToOne
    @JoinColumn(name = "contact_type_id")
    private ContactTypeEntity type;



}
