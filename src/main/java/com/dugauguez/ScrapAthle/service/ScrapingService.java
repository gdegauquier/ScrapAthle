package com.dugauguez.ScrapAthle.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Slf4j
@Service
public class ScrapingService {

    @Autowired
    FileService fileService;

    @Value("${bases.athle.uri.base}")
    private String host;

    @Async
    public void getAllByYear(int year) {

        String dir = getClass().getResource("/data/" + year).getFile();

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (!isValidFile(year, file.getAbsolutePath())) {
                continue;
            }

            log.info("File being scraped is {}", file.getName());
            scrap(file);
            break;

        }

    }

    private boolean isValidFile(int year, String file) {
        return file.contains(year + "") && file.endsWith(".html");
    }

    private boolean hasDetailFileToBeRetrieved(int year, String department, String id) {
        URL url = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html");

        if (url == null){
            return true;
        }

        String path = url.getFile();
        File test = new File(path);

        if (!test.exists()) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate fileTime = Instant.ofEpochMilli(test.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(now, fileTime);
        int diff = period.getDays();

        return diff >= 2;

    }


    private void scrap(File file) {

        int year = Integer.valueOf(file.getParentFile().getName());
        String department = file.getName().split(".html")[0];

        Document doc;

        try {
            doc = Jsoup.parse(file, StandardCharsets.UTF_8.displayName(), host);
        } catch (Exception e) {
            log.error("File {} could not be parsed", e);
            return;
        }

        Elements elements = doc.getElementsByAttribute("href");

        for (Element element : elements) {

            if (element.attr("href").contains("bddThrowCompet")) {

                String id = element.attr("href").split("'")[1];

                log.debug("Compet {} found", id);
                if (!hasDetailFileToBeRetrieved(year, department, id)) {
                    log.debug("Compet {} already retrieved", id);
                    continue;
                }
                fileService.getById(year, department, id);
            }

        }

        // log.info("File {} parsed {}", file.getName(), parsed);

    }

}
