package com.dugauguez.scrapathle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"stadiumAddress"})
@ToString(exclude = {"stadiumAddress"})
public class Event {

    private String fileId = null;

    private String code = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String league;

    private String type;

    private String department = null;

    private String phoneNumber1 = null;

    private String phoneNumber2 = null;

    private String eventSubmittedBy = null;

    private String resultsGivenBy = null;

    private String resultsControledBy = null;

    private String codePostal = null;

    private String organisation = null;

    private String recompenses = null;

    private String conditions = null;

    private String avisTechniqueEtSecurite = null;

    private String email = null;

    private String organisateur = null;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_stadium_address")
    private Address stadiumAddress = null;

    private String montantInscription = null;

    private String contactPresse = null;

    private String siteWeb = null;

    private String fax = null;

    private String vainqueur = null;

    private String certificatDeMesurage = null;

    private String contactEngagement = null;

    private String engagementEnLigne = null;

    private String autresInfos = null;

    private String anneePrecedente = null;

    private String officiel = null;

    private String infosEpreuve = null;

    private String challenge = null;

    private String contactTechnique = null;

    private String epreuves = null;

    private String services = null;

    private LocalDate dateDeDebut = null;

    private String dateDeFin = null;

    private String niveau = null;

    private String arrivee = null;

    private String depart = null;

    public void setDateDeDebut(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        this.dateDeDebut = LocalDate.parse(date.trim(), formatter);
    }

}
