package com.dugauguez.scrapathle.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Event {

    private String id = null;
    private String telephone = null;
    private String resultatsChargesPar = null;
    private String puisControlesPar = null;
    private String inscriteAuCalendrierPar = null;
    private String adresse = null;
    private String ville = null;
    private String codePostal = null;
    private String organisation = null;
    private String recompenses = null;
    private String conditions = null;
    private String avisTechniqueEtSecurite = null;
    private String emaill = null;
    private String organisateur = null;
    private String stade = null;
    private String montantInscription = null;
    private String contactPresse = null;
    private String siteWeb = null;
    private String fax = null;
    private String certificatDeMesurage = null;
    private String contactEngagement = null;
    private String engagementEnligne = null;
    private String autresInfos = null;
    private String anneePrecedente = null;
    private String officiel = null;
    private String infosEpreuve = null;
    private String challenge = null;
    private String contactTechnique = null;
    private String epreuves = null;

    public void setEventField(String extractedString, String field) {
        switch (field) {
            case "Téléphone 1":
                this.setTelephone(extractedString);
                break;
            case "Résultats chargés par":
//                BERNARD AUDINEAU - <a href="mailto:bernard.athle@free.fr?subject=[Competition 226090 - foulée de la vogue SAINT MARTIN LE CHATEL]">Cliquez pour contacter
                this.setResultatsChargesPar(cleanEmail(extractedString, "<a href=\"mailto:", "?subject="));
                break;
            case "Puis contrôlés par":
//                DEBRION RENE - <a href="mailto:darebane@free.fr?subject=[Competition 224164 - Trail de la Michaille CHATILLON EN MICHAILLE]">Cliquez pour contacter
                this.setPuisControlesPar(cleanEmail(extractedString, "<a href=\"mailto:", "?subject="));
                break;
            case "Inscrite au calendrier par":
                this.setInscriteAuCalendrierPar(extractedString);
                break;
            case "Adresse":
                this.setAdresse(extractedString);
                break;
            case "Ville":
                this.setVille(extractedString);
                break;
            case "Code Postal":
                this.setCodePostal(extractedString);
                break;
            case "Organisation":
                this.setOrganisation(extractedString);
                break;
            case "Récompenses":
                this.setRecompenses(extractedString);
                break;
            case "Conditions":
//                <a href="http://athletisme-aura.athle.fr/asp.net/espaces.engage/engage.aspx" target="_blank"> Engagement des Athlètes AuRA et Jury
                this.setConditions(cleanCondition(extractedString));
                break;
            case "Avis Technique et Sécurité":
                this.setAvisTechniqueEtSecurite(extractedString);
                break;
            case "Mèl":
//                <a href="mailto:jean-claude.tomei@wanadoo.fr?subject=[Calendriers FFA]" target="_blank">jean-claude.tomei@wanadoo.fr
                this.setEmaill(extractedString.substring(extractedString.indexOf("mailto:") + 7, extractedString.indexOf("?subject=")));
                break;
            case "Organisateur":
                this.setOrganisateur(extractedString);
                break;
            case "Stade":
                this.setStade(extractedString);
                break;
            case "Montant Inscription":
                this.setMontantInscription(extractedString);
                break;
            case "Contact Presse":
//                contactPresse=BON Eric - <a href="mailto:dombesrunningevasion@laposte.net?subject=[Calendriers FFA]">dombesrunningevasion@laposte.net
                this.setContactPresse(cleanEmail(extractedString, "<a href=\"mailto:", "?subject="));
                break;
            case "Site Web":
//                <a href="http://24hvillenave.fr/" target="_blank">http://24hvillenave.fr/
                this.setSiteWeb(extractedString.substring(extractedString.indexOf(">") + 1, extractedString.length()));
                break;
            case "Fax":
                this.setFax(extractedString);
                break;
            case "Certificat de mesurage":
                this.setCertificatDeMesurage(extractedString);
                break;
            case "Contact Engagement":
//                CHARBIN Sébastien - <a href="mailto:seb.charbin@orange.fr?subject=[Calendriers FFA]">seb.charbin@orange.fr
                this.setContactEngagement(cleanContactEngagement(extractedString));
                break;
            case "Engagement en ligne":
//                <a href="http://fouleesvouglaisie.fr" target="_blank">http://fouleesvouglaisie.fr
                this.setEngagementEnligne(extractedString.substring(extractedString.indexOf(">") + 1, extractedString.length()));
                break;
            case "Autres Infos":
                this.setAutresInfos(extractedString);
                break;
            case "Année Précédente":
                this.setAnneePrecedente(extractedString);
                break;
            case "Officiel (Juge arbitre)":
//                JEAN-PIERRE LAVIE - <a href="mailto:lavie.jp@orange.fr?subject=[Competition 228796 - Animation EA/PO en gymnase MONTREVEL EN BRESSE]">Cliquez pour
                this.setOfficiel(cleanOfficiel(extractedString, "<a href=\"mailto:", "?subject="));
                break;
            case "Infos Epreuve":
                this.setInfosEpreuve(extractedString);
                break;
            case "Challenge":
                this.setChallenge(extractedString);
                break;
            case "Contact Technique":
                this.setContactTechnique(extractedString);
                break;
            case "Epreuves":
                this.setEpreuves(extractedString);
                break;

            default:
        }
    }

    private String cleanContactEngagement(String extractedString) {
        int startIndex = extractedString.indexOf("<");
        int endIndex = extractedString.indexOf(">");
        if (startIndex == -1 || endIndex == -1) {
            return extractedString;
        }
        String replacement = "";
        String toBeReplaced = extractedString.substring(startIndex, endIndex + 1);
        return extractedString.replace(toBeReplaced, replacement);
    }

    private String cleanCondition(String extractedString) {
        int startIndex = extractedString.indexOf("<a href=\"");
        int endIndex = extractedString.indexOf(">");
        if (startIndex == -1 || endIndex == -1) {
            return extractedString;
        }

        int end = extractedString.indexOf("\" target=") != -1 ? extractedString.indexOf("\" target=") : endIndex - 1;
        String replacement = extractedString.substring(startIndex + "<a href=\"".length(), end);
        String toBeReplaced = extractedString.substring(startIndex, endIndex + 1);
        return extractedString.replace(toBeReplaced, replacement);
    }


    private String cleanEmail(String extractedString, String from, String to) {

        int startIndex = extractedString.indexOf(from);
        int endIndex = extractedString.indexOf(to);
        if (startIndex == -1 || endIndex == -1) {
            return extractedString;
        }

        String replacement = extractedString.substring(startIndex + from.length(), endIndex);
        String toBeReplaced = extractedString.substring(startIndex, extractedString.length());
        return extractedString.replace(toBeReplaced, replacement);
    }

    private String cleanOfficiel(String extractedString, String from, String to) {
        String part1 = cleanEmail(extractedString, from, to);

        int startIndex = extractedString.indexOf("Competition");
        int endIndex = extractedString.indexOf("]");
        String part2 = extractedString.substring(startIndex + from.length(), endIndex);

        String part3 = part2.substring(part2.indexOf("-") - 1, part2.length());
        return part1 + part3;

    }


    public String toString() {
        return
                "\nId : " + this.id + " \n" +
                        "telephone: " + this.telephone + " \n" +
                        "resultatsChargesPar: " + this.resultatsChargesPar + " \n" +
                        "puisControlesPar: " + this.puisControlesPar + " \n" +
                        "inscriteAuCalendrierPar: " + this.inscriteAuCalendrierPar + " \n" +
                        "adresse: " + this.adresse + " \n" +
                        "ville: " + this.ville + " \n" +
                        "codePostal: " + this.codePostal + " \n" +
                        "organisation: " + this.organisation + " \n" +
                        "recompenses: " + this.recompenses + " \n" +
                        "conditions: " + this.conditions + " \n" +
                        "avisTechniqueEtSecurite: " + this.avisTechniqueEtSecurite + " \n" +
                        "emaill: " + this.emaill + " \n" +
                        "organisateur: " + this.organisateur + " \n" +
                        "stade: " + this.stade + " \n" +
                        "montantInscription: " + this.montantInscription + " \n" +
                        "contactPresse: " + this.contactPresse + " \n" +
                        "siteWeb: " + this.siteWeb + " \n" +
                        "fax: " + this.fax + " \n" +
                        "certificatDeMesurage: " + this.certificatDeMesurage + " \n" +
                        "contactEngagement: " + this.contactEngagement + " \n" +
                        "engagementEnligne: " + this.engagementEnligne + " \n" +
                        "autresInfos: " + this.autresInfos + " \n" +
                        "anneePrecedente: " + this.anneePrecedente + " \n" +
                        "officiel: " + this.officiel + " \n" +
                        "infosEpreuve: " + this.infosEpreuve + " \n" +
                        "challenge: " + this.challenge + " \n" +
                        "contactTechnique: " + this.contactTechnique + " \n" +
                        "epreuves: " + this.epreuves + " \n";
    }
}
