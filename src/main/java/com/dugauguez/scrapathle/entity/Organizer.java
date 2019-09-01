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
@Table(name = "organizer")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"event"})
@ToString(exclude = {"event"})
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("name")
    private String organisation = null;

    @JsonProperty("Mèl")
    private String email = null;

    public void setEmail(String email) {
        this.email = email == null ? null : email.contains("@") ? email.trim() : null;
    }

    private String contactPresse = null;

    @JsonProperty("Site Web")
    private String siteWeb = null;

    private String contactEngagement = null;

    @JsonProperty("Engagement en ligne")
    private String engagementEnLigne = null;

    @JsonProperty("Fax")
    private String fax = null;

    @JsonProperty("Téléphone 1")
    private String phoneNumber1 = null;

    @JsonProperty("Téléphone 2")
    private String phoneNumber2 = null;

    public void setFax(String faxNumber) {
        this.fax = cleanNumber(faxNumber);
    }

    public void setphoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = cleanNumber(phoneNumber1);
    }

    public void setphoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = cleanNumber(phoneNumber2);
    }

    private String cleanNumber(String phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.trim()
                                                       .replace(".", "")
                                                       .replace(" ", "")
                                                       .replace("-", "")
                                                       .replace("+033", "0")
                                                       .replace("+33", "0")
                                                       .replace("006", "06")
                                                       .replace("+006", "06");
    }


    private String eventSubmittedBy = null;

    @JsonProperty("Résultats chargés par")
    private String resultsGivenBy = null;

    @JsonProperty("Puis contrôlés par")
    private String resultsControledBy = null;

    @JsonIgnore
    @OneToMany(mappedBy = "organizerContact")
    private Set<Event> events;

    public Event addEvent(Event event) {
        if (getEvents() == null) {
            events = new HashSet<>();
        }
        getEvents().add(event);
        event.setOrganizerContact(this);
        return event;
    }

    public Event removeEvent(Event event) {
        getEvents().remove(event);
        event.setOrganizerContact(null);
        return event;
    }


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


    private String cleanAddress(String organisation) {
        String data = organisation.trim().toLowerCase();
        return (Arrays.asList("*", ",", "-", ".", "/", "?", "x", "_").contains(data)) ? null : data;
    }

    public void setOrganisation(String organisation) {
        this.organisation = cleanAddress(organisation);
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

}
