package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.repository.EventRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


@Slf4j
@Service
public class ScrapingService {

    @Autowired
    FileService fileService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    private JsoupUtils jsoupUtils;


    @Async
    public void getAllByYear(int year) {


        log.info("[getAllByYear] Starting calculate supplier size");
        long startProcessing = System.currentTimeMillis();

        String dir = getClass().getResource("/data/" + year).getFile();

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        List<Event> all = new ArrayList<>();

        Arrays.stream(listOfFiles)
              .parallel()
              .filter(file -> isValidFile(year, file.getAbsolutePath()))
              .forEach(file -> {
                  List<Event> eventList = scrapEvents(file);
                  all.addAll(eventList);
              });

        eventRepository.saveAll(all);

        long duration = System.currentTimeMillis() - startProcessing;
        log.info("[getAllByYear] Elapsed time : {} min {} s {} ms", duration / 1000 / 60, duration / 1000 % 60, (duration - ((duration / 1000 % 60) * 1000)));
        log.info("there are " + all.size() + " events ");
    }

    private boolean isValidFile(int year, String file) {
        return file.contains(year + "") && file.endsWith(".html");
    }

    private boolean hasDetailFileToBeRetrieved(int year, String department, String id) {
        URL url = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html");

        if (url == null) {
            return true;
        }

        String path = url.getFile();
        File test = new File(path);

        if (!test.exists()) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate fileTime = Instant.ofEpochMilli(test.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(fileTime, now);
        int diff = period.getDays();

        return diff >= 2;

    }


    private List<Event> scrapEvents(File file) {

        int year = Integer.parseInt(file.getParentFile().getName());
        String department = file.getName().split(".html")[0];

        Document doc = JsoupUtils.INSTANCE.getDocument(file);

        Elements elements = doc.getElementsByAttribute("href");

        List<Event> enventList = new ArrayList<>();

        elements
                .stream()
                .parallel()
                .filter(element -> element.attr("href").contains("bddThrowCompet"))
                .forEach(element -> {
                    String id = element.attr("href").split("'")[1];
                    if (hasDetailFileToBeRetrieved(year, department, id)) {
                        fileService.getById(year, department, id);
                    }
                    enventList.add(parseEvent(year, department, id));
                });
        return enventList;
    }

    private Event parseEvent(int year, String department, String id) {

        String file = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html").getFile();

        Document doc = JsoupUtils.INSTANCE.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return null;
        }

        Elements styles = doc.getElementsByTag("td");

        List<String> strr = new ArrayList<>();
        styles.stream()
              .parallel()
              .forEach(element -> {
                  String text = element.text();
                  boolean parseConditions = text.contains(":") && text.length() > 5 && !text.contains(":00") && text.indexOf(":") != 2 && text.indexOf(":") != 4 && text.indexOf(":") != 5;
                  if (parseConditions) {

                      String[] splitString = text.replace("Récompenses :", "\nrécompenses :")
                                                 .replace("Résultats chargés par :", "\nrésultats chargés par :")
                                                 .replace("Puis contrôlés par :", "\npuis contrôlés par :")
                                                 .replace("Adresse :", "\nadresse :")
                                                 .replace("Code Postal :", "\nCode Postal :")
                                                 .replace("Ville :", "\nville :")
                                                 .replace("Services :", "\nServices :")
                                                 .replace("Avis Technique et Sécurité :", "\nAvis Technique et Sécurité :")
                                                 .replace("Contact Engagement :", "\nContact Engagement :")
                                                 .replace("Conditions :", "\nConditions :")
                                                 .replace("Organisation :", "\nOrganisation :")
                                                 .replace("Organisateur :", "\nOrganisateur :")
                                                 .replace("Mèl :", "\nMèl :")
                                                 .replace("Stade :", "\nStade :")
                                                 .replace("Fax :", "\nFax :")
                                                 .replace("Inscrite au calendrier par :", "\nInscrite au calendrier par :")
                                                 .replace("Femmes :", "\nFemmes :")
                                                 .replace("Vainqueur :", "\nVainqueur :")
//                                                 .replace("Hommes :", "\nHommes :")
                                                 .replace("Date de Début :", "\nDate de Début :")
                                                 .replace("Départ :", "\nDépart :")
                                                 .replace("Arrivée :", "\nArrivée :")
                                                 .replace("Droits d'inscription :", "\nDroits d'inscription :")
                                                 .replace("Epreuves :", "\nEpreuves :")
                                                 .replace("Les licenciés titulaires d’une licence sportive des fédérations suivantes :", "\nLes licenciés titulaires d’une licence sportive des fédérations suivantes :")
                                                 .replace("Les principes :", "\nLes principes :")
                                                 .replace("Contact Technique :", "\nContact Technique :")
                                                 .replace("Challenge :", "\nChallenge :")
                                                 .replace("Infos Epreuve :", "\nInfos Epreuve :")
                                                 .replace("Officiel (Juge arbitre) :", "\nOfficiel (Juge arbitre) :")
                                                 .replace("Année Précédente :", "\nAnnée Précédente :")
                                                 .replace("Autres Infos :", "\nAutres Infos :")
                                                 .replace("Engagement en ligne :", "\nEngagement en ligne :")
                                                 .replace("Certificat de mesurage :", "\nCertificat de mesurage :")
                                                 .replace("Site Web :", "\nSite Web :")
                                                 .replace("Contact Presse :", "\nContact Presse :")
                                                 .replace("Montant Inscription :", "\nMontant Inscription :")
                                                 .replace("Téléphone 1 :", "\nTéléphone 1 :")
                                                 .replace("Téléphone 2 :", "\nTéléphone 2 :")
                                                 .split("\n");

                      Stream.of(splitString)
                            .parallel()
                            .filter(e -> e != null)
                            .filter(e -> e != "")
                            .filter(e -> e.contains(":"))
                            .forEach(e -> strr.add(e));
                  }
              });


        List<String> keys = Arrays.asList("récompenses :", "résultats chargés par :", "puis contrôlés par :", "adresse :", "code postal :", "ville :", "services :",
                                          "avis technique et sécurité :", "contact engagement :", "conditions :", "organisation :", "organisateur :", "mèl :", "stade :", "fax :",
                                          "inscrite au calendrier par :", "vainqueur :", "date de début :",
//                                                                "femmes :", "hommes :","droits d'inscription :", "les licenciés titulaires d’une licence sportive des fédérations suivantes :",
                                          "départ :", "arrivée :", "epreuves :", "niveau :",
                                          "les principes :", "contact technique :", "challenge :", "infos epreuve :", "officiel (juge arbitre) :", "année précédente :", "autres infos :",
                                          "engagement en ligne :", "certificat de mesurage :", "site web :", "contact presse :", "montant inscription :", "téléphone 1 :", "téléphone 2 :");

        strr.removeAll(Collections.singleton(null));

        Map<String, String> collectMap = strr.stream()
                                             .parallel()
                                             .filter(str -> str.contains(":"))
                                             .map(str -> str.split(":", 2))
                                             .filter(str -> keys.contains(str[0].toLowerCase() + ":"))
                                             .filter(str -> str[1].length() < 254)
                                             .collect(toMap(str -> str[0].trim()
                                                                         .replaceAll(" ", "_")
                                                                         .replaceAll("é", "e")
                                                                         .replaceAll("è", "e")
                                                                         .replaceAll("ô", "o")
                                                                         .toLowerCase(),
                                                            str -> (str[0].equals("Téléphone 2 ") || str[0].equals("Téléphone 1 ")) ? str[1].replaceAll("\\s+", "").replaceAll("\\.", "") : str[1],
                                                            (v1, v2) -> v1
                                             ));


        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        Event event = mapper.convertValue(collectMap, Event.class);
        event.setFileId(id);

        return event;
    }


}
