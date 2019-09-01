package com.dugauguez.scrapathle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"stadiumAddress", "organizerContact"})
@ToString(exclude = {"stadiumAddress", "organizerContact"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileId = null;

    private String code = null;

    private String title;

    private String league;

    private String type;

    private String department = null;

    private String recompenses = null;

    private String conditions = null;

    private String avisTechniqueEtSecurite = null;

    private String montantInscription = null;

    private String vainqueur = null;

    private String certificatDeMesurage = null;

    private String autresInfos = null;

    private String anneePrecedente = null;

    private String officiel = null;

    private String infosEpreuve = null;

    private String challenge = null;

    private String contactTechnique = null;

    private String epreuves = null;

    private String services = null;

    private String niveau = null;

    private String arrivee = null;

    private String depart = null;

    private LocalDate dateDeDebut = null;

    private LocalDate dateDeFin = null;

    private LocalDate fromStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        return date == null ? null : LocalDate.parse(date.trim(), formatter);
    }

    public void setDateDeDebut(String date) {
        this.dateDeDebut = fromStringToDate(date);
    }

    public void setDateDeFin(String date) {
        this.dateDeFin = fromStringToDate(date);
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_organizer_contact")
    private Organizer organizerContact = null;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_stadium_address")
    private Address stadiumAddress = null;
}
