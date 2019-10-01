package com.dugauguez.scrapathle.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "test")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String distance;
    private String label;
    private String description;
    private String amount;
    private String conditions;
    private String formerYear;
    private String rewards;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JoinTable(name = "rel_test_test_category",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "test_category_id"))
    private List<TestCategoryEntity> categories;

}
