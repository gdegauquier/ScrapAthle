package com.dugauguez.scrapathle.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "event_type")
public class EventTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String code;
    private String label;
    private Boolean isSubType;

    @ManyToMany(mappedBy = "types")
    private List<EventEntity> events = new ArrayList<>();

}
