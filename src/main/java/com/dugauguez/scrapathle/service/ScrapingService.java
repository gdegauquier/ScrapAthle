package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.utils.JsoupUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ScrapingService {

    @Autowired
    FileService fileService;

    @Autowired
    private JsoupUtils jsoupUtils;

    @Async
    public void getAllByYear(int year) {

        String dir = getClass().getResource("/data/" + year).getFile();

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        log.info("[getAllByYear] Starting calculate supplier size");
        long startProcessing = System.currentTimeMillis();

        List<Event> all = new ArrayList<>();

        Arrays.stream(listOfFiles)
              .parallel()
              .filter(file -> isValidFile(year, file.getAbsolutePath()))
              .forEach(file -> all.addAll(scrapEvents(file)));

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

        Document doc = jsoupUtils.getDocument(file);

        Elements elements = doc.getElementsByAttribute("href");

        List<Event> enventList = new ArrayList<>();

        elements.parallelStream()
                .filter(element -> element.attr("href").contains("bddThrowCompet"))
                .forEach(element -> {
                    String id = element.attr("href").split("'")[1];
                    log.info("Compet {} found", id);
                    if (hasDetailFileToBeRetrieved(year, department, id)) {
                        fileService.getById(year, department, id);
                    }
                    enventList.add(parseEvent(year, department, id));
                });
        return enventList;
    }

    private Event parseEvent(int year, String department, String id) {

        String file = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html").getFile();

        Document doc = jsoupUtils.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return null;
        }

        Elements styles = doc.getElementsByAttributeValue("style", "vertical-align:top");
        Event event = new Event();
        event.setId(id);

        styles.stream()
              .parallel()
              .forEach(element -> {
                  Elements key = element.getElementsByAttributeValue("style", "font-weight:bolder;text-align:right;width:9%;white-space:nowrap");
                  Elements value = element.getElementsByAttributeValue("style", "font-weight:normal;text-align:left;width:90%");

                  if (key.toString().length() != 0) {

                      int valueBeginIndex = value.toString().indexOf(">") + 1;
                      int valueEndIndex = value.toString().indexOf("</");

                      String extractedString = value.toString().substring(valueBeginIndex, valueEndIndex).trim().replaceAll("<br>", " ");

                      int keyBeginIndex = key.toString().indexOf(">") + 1;
                      int keyEndIndex = key.toString().indexOf("</");
                      String field = key.toString().substring(keyBeginIndex, keyEndIndex).trim();

                      event.setEventField(extractedString, field);
                  }

              });

        log.debug("DOC ID PARSED {}", doc);

        log.info(event.toString());

        return event;
    }


}
