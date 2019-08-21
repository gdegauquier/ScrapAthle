package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.repository.EventRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    private List<String> getColumnNames() {
        /**
         *  Retrieve the values of the annotation @JsonProperty which exits in the class Event.
         *  This values are exactly the same in the html file
         */
        return Stream.of(Event.class.getDeclaredFields()).map(field -> field.getDeclaredAnnotation(JsonProperty.class).value()).collect(Collectors.toList());
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


        List<String> keys = getColumnNames();
        strr.removeAll(Collections.singleton(null));

        Map<String, String> collectMap = strr.stream()
                                             .parallel()
                                             .filter(str -> str.contains(" : "))
                                             .map(str -> str.split(" : ", 2))
                                             .filter(str -> keys.contains(str[0].trim()))
                                             .filter(str -> str[1].length() < 254)
                                             .collect(toMap(str -> str[0].trim(),
                                                            str -> (str[0].equals("Téléphone 2") || str[0].equals("Téléphone 1")) ? str[1].replaceAll("\\s+", "").replaceAll("\\.", "").trim() : str[1].trim(),
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
