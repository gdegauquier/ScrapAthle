package com.dugauguez.scrapathle.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "event")
public class EventEntity {

    @Id
    private String id;

    private String code;
    private String name;
    private Boolean isLabeled;
    private Boolean isTechnicallyAdviced;
    private Date dateBegin;
    private Date dateEnd;
    private String conditions;
    private String otherInfo;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private LeagueEntity league;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private LevelEntity level;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private TownEntity town;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "rel_event_type",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "event_type_id")
    )
    private List<EventTypeEntity> types;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "rel_event_test",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id"))
    private List<TestEntity> tests;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "rel_event_address",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<AddressTypeEntity> adresses;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "rel_event_contact",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<ContactEntity> contacts;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "rel_event_service",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceEntity> services;


}
