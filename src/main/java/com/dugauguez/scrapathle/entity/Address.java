package com.dugauguez.scrapathle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"event"})
@ToString(exclude = {"event"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("Line1")
    private String line1 = null;

    @JsonProperty("Line2")
    private String line2 = null;

    @JsonProperty("Line3")
    private String line3 = null;

    @JsonProperty("Ville")
    private String town = null;

    @JsonProperty("Code Postal")
    private String postalCode = null;

    @JsonProperty("Type")
    private String type = null;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        String data = name.trim().toLowerCase();
        this.name = (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setLine1(String line1) {

        String data = line1.trim().toLowerCase();
        this.line1 = (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setLine2(String line2) {
        String data = line2.trim().toLowerCase();
        this.line2 = (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setLine3(String line3) {
        String data = line3.trim().toLowerCase();
        this.line3 = (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setTown(String town) {
        this.town = town.trim().toUpperCase();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode.trim();
    }

    public void setType(String type) {
        this.type = type.trim();
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    private Double latitude;

    private Double longitude;

    public String getAddress() {
        String address = "";

        if (getLine1() != null) {
            address = address + getLine1() + " ";
        }
        if (getLine2() != null) {
            address = address + getLine2() + " ";
        }
        if (getLine3() != null) {
            address = address + getLine3() + " ";
        }
        return address + getPostalCode() + " " + getTown();
    }

    @JsonIgnore
    @OneToMany(mappedBy = "stadiumAddress")
    private Set<Event> events;

    public Event addEvent(Event event) {
        if (getEvents() == null) {
            events = new HashSet<>();
        }
        getEvents().add(event);
        event.setStadiumAddress(this);
        return event;
    }

    public Event removeEvent(Event event) {
        getEvents().remove(event);
        event.setStadiumAddress(null);
        return event;
    }

}
