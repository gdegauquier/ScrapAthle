package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.repository.EventRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


@Slf4j
@Service
public class ScrapingService {

    public static final int OLDER_THAN_TWO_DAYS = 2;
    @Autowired
    FileService fileService;

    @Autowired
    EventRepository eventRepository;

    @Async
    public void getAllByYear(int year) {

        log.debug("Parsing is starting...");

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
        log.debug("Elapsed time : {} min {} s {} ms", duration / 1000 / 60, duration / 1000 % 60, (duration - ((duration / 1000 % 60) * 1000)));
        log.info("There are {} events", all.size());
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

        return diff >= OLDER_THAN_TWO_DAYS;

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

    private List<String> getColumnNamesToLowerCase() {
        List<String> keys = getColumnNames();
        return keys.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private List<String> getColumnNames() {

        List<String> keys = new ArrayList<>();

        keys.add("Récompenses");
        keys.add("Résultats chargés par");
        keys.add("Puis contrôlés par");

        keys.add("Adresse");
        keys.add("Code Postal");
        keys.add("Ville");
        keys.add("Services");

        keys.add("Avis Technique et Sécurité");
        keys.add("Contact Engagement");
        keys.add("Conditions");

        keys.add("Organisation");
        keys.add("Organisateur");
        keys.add("Mèl");
        keys.add("Stade");
        keys.add("Fax");

        keys.add("Inscrite au calendrier par");
        keys.add("Vainqueur");
        keys.add("Date de Début");

        keys.add("Départ");
        keys.add("Arrivée");
        keys.add("Epreuves");
        keys.add("Niveau");

        keys.add("Les principes");
        keys.add("Contact Technique");
        keys.add("Challenge");
        keys.add("Infos Epreuve");
        keys.add("Officiel (Juge arbitre)");
        keys.add("Année Précédente");
        keys.add("Autres Infos");

        keys.add("Engagement en ligne");
        keys.add("Certificat de mesurage");
        keys.add("Site Web");
        keys.add("Contact Presse");
        keys.add("Montant Inscription");
        keys.add("Téléphone 1");
        keys.add("Téléphone 2");

        keys.add("Droits d'inscription");
        keys.add("Les licenciés titulaires d’une licence sportive des fédérations suivantes");

        return keys;

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

                    boolean canParseColumn = text.contains(":") && text.length() > 5 &&
                            !text.contains(":00") &&
                            text.indexOf(":") != 2 &&
                            text.indexOf(":") != 4 &&
                            text.indexOf(":") != 5;

                    if (!canParseColumn) {
                        return;
                    }

                    for (String key : getColumnNames()) {
                        text = addSplitDelemiter(text, key);
                    }

                    String[] splitString = text.split("\n");

                    Stream.of(splitString)
                            .parallel()
                            .filter(e -> e != null)
                            .filter(e -> e != "")
                            .filter(e -> e.contains(":"))
                            .forEach(e -> strr.add(e));

                });


        List<String> keys = getColumnNamesToLowerCase();
        strr.removeAll(Collections.singleton(null));

        Map<String, String> collectMap = strr.stream()
                .parallel()
                .filter(str -> str.contains(" : "))
                .map(str -> str.split(" : ", 2))
                .filter(str -> keys.contains(str[0].toLowerCase()))
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

    private String addSplitDelemiter(String text, String marker) {
        return text.replace(marker + " :", "\n" + marker + " :");
    }


}
