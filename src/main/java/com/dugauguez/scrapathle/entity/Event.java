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

    @JsonProperty("telephone_1")
    private String telephone1 = null;

    @JsonProperty("telephone_2")
    private String telephone2 = null;

    @JsonProperty("resultats_charges_par")
    private String resultatsChargesPar = null;

    @JsonProperty("puis_controles_par")
    private String puisControlesPar = null;

    @JsonProperty("inscrite_au_calendrier_par")
    private String inscriteAuCalendrierPar = null;

    @JsonProperty("adresse")
    private String adresse = null;

    @JsonProperty("ville")
    private String ville = null;

    @JsonProperty("code_postal")
    private String codePostal = null;

    @JsonProperty("organisation")
    private String organisation = null;

    @JsonProperty("recompenses")
    private String recompenses = null;

    @JsonProperty("conditions")
    private String conditions = null;

    @JsonProperty("avis_technique_et_securite")
    private String avisTechniqueEtSecurite = null;

    @JsonProperty("mel")
    private String emaill = null;

    @JsonProperty("organisateur")
    private String organisateur = null;

    @JsonProperty("stade")
    private String stade = null;

    @JsonProperty("montant_inscription")
    private String montantInscription = null;

    @JsonProperty("contact_presse")
    private String contactPresse = null;

    @JsonProperty("site_web")
    private String siteWeb = null;

    @JsonProperty("fax")
    private String fax = null;

    @JsonProperty("vainqueur")
    private String vainqueur = null;

    @JsonProperty("certificat_de_mesurage")
    private String certificatDeMesurage = null;

    @JsonProperty("contact_engagement")
    private String contactEngagement = null;

    @JsonProperty("engagement_en_ligne")
    private String engagementEnLigne = null;

    @JsonProperty("autres_infos")
    private String autresInfos = null;

    @JsonProperty("annee_precedente")
    private String anneePrecedente = null;

    @JsonProperty("officiel_(juge_arbitre)")
    private String officiel = null;

    @JsonProperty("infos_epreuve")
    private String infosEpreuve = null;

    @JsonProperty("challenge")
    private String challenge = null;

    @JsonProperty("contact_technique")
    private String contactTechnique = null;

    @JsonProperty("epreuves")
    private String epreuves = null;

    @JsonProperty("services")
    private String services = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("date_de_debut")
    private LocalDate dateDeDebut = null;

    @JsonProperty("date_de_fin")
    private String dateDeFin = null;

    @JsonProperty("niveau")
    private String niveau = null;

    @JsonProperty("code")
    private String code = null;


    @JsonProperty("arrivee")
    private String arrivee = null;

    @JsonProperty("depart")
    private String depart = null;

    public void setDateDeDebut(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        this.dateDeDebut = LocalDate.parse(date.trim(), formatter);
    }

}
