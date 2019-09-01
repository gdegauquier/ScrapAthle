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

    private Double latitude;

    private Double longitude;

    public void setId(Integer id) {
        this.id = id;
    }

    private String cleanAddress(String address) {
        String data = address.trim().toLowerCase();
        return (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setName(String name) {
        this.name = cleanAddress(name);
    }

    public void setLine1(String line1) {
        this.line1 = cleanAddress(line1);
    }

    public void setLine2(String line2) {
        this.line2 = cleanAddress(line2);
    }

    public void setLine3(String line3) {
        this.line3 = cleanAddress(line3);
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
