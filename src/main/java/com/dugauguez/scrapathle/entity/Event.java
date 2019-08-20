package com.dugauguez.scrapathle.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("file_id")
    private String fileId = null;

    @JsonProperty("Téléphone 1")
    private String telephone1 = null;

    @JsonProperty("Téléphone 2")
    private String telephone2 = null;

    @JsonProperty("Résultats chargés par")
    private String resultatsChargesPar = null;

    @JsonProperty("Puis contrôlés par")
    private String puisControlesPar = null;

    @JsonProperty("Inscrite au calendrier par")
    private String inscriteAuCalendrierPar = null;

    @JsonProperty("Adresse")
    private String adresse = null;

    @JsonProperty("Ville")
    private String ville = null;

    @JsonProperty("Code Postal")
    private String codePostal = null;

    @JsonProperty("Organisation")
    private String organisation = null;

    @JsonProperty("Récompenses")
    private String recompenses = null;

    @JsonProperty("Conditions")
    private String conditions = null;

    @JsonProperty("Avis Technique et Sécurité")
    private String avisTechniqueEtSecurite = null;

    @JsonProperty("Mèl")
    private String emaill = null;

    @JsonProperty("Organisateur")
    private String organisateur = null;

    @JsonProperty("Stade")
    private String stade = null;

    @JsonProperty("Montant Inscription")
    private String montantInscription = null;

    @JsonProperty("Contact Presse")
    private String contactPresse = null;

    @JsonProperty("Site Web")
    private String siteWeb = null;

    @JsonProperty("Fax")
    private String fax = null;

    @JsonProperty("Vainqueur")
    private String vainqueur = null;

    @JsonProperty("Certificat de mesurage")
    private String certificatDeMesurage = null;

    @JsonProperty("Contact Engagement")
    private String contactEngagement = null;

    @JsonProperty("Engagement en ligne")
    private String engagementEnLigne = null;

    @JsonProperty("Autres Infos")
    private String autresInfos = null;

    @JsonProperty("Année Précédente")
    private String anneePrecedente = null;

    @JsonProperty("Officiel (Juge arbitre)")
    private String officiel = null;

    @JsonProperty("Infos Epreuve")
    private String infosEpreuve = null;

    @JsonProperty("Challenge")
    private String challenge = null;

    @JsonProperty("Contact Technique")
    private String contactTechnique = null;

    @JsonProperty("Epreuves")
    private String epreuves = null;

    @JsonProperty("Services")
    private String services = null;

//    @JsonProperty("type")
//    private String type = null;

    @JsonProperty("Date de Début")
    private LocalDate dateDeDebut = null;

//    @JsonProperty("date_de_fin")
//    private String dateDeFin = null;

    @JsonProperty("Niveau")
    private String niveau = null;

//    @JsonProperty("code")
//    private String code = null;


    @JsonProperty("Arrivée")
    private String arrivee = null;

    @JsonProperty("Départ")
    private String depart = null;

    public void setDateDeDebut(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        this.dateDeDebut = LocalDate.parse(date.trim(), formatter);
    }

}
