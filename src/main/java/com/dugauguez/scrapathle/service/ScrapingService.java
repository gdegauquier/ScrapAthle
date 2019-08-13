package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.repository.ScrapingRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

@Slf4j
@Service
public class ScrapingService {

    @Autowired
    FileService fileService;

    @Autowired
    ScrapingRepository scrapingRepository;

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
            scrapEvents(file);
            break;

        }

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


    private void scrapEvents(File file) {

        int year = Integer.parseInt(file.getParentFile().getName());
        String department = file.getName().split(".html")[0];

        Document doc = JsoupUtils.INSTANCE.getDocument(file);

        Elements elements = doc.getElementsByAttribute("href");

        for (Element element : elements) {

            if (element.attr("href").contains("bddThrowCompet")) {

                String id = element.attr("href").split("'")[1];

                log.debug("Compet {} found", id);
                if (hasDetailFileToBeRetrieved(year, department, id)) {
                    fileService.getById(year, department, id);

                }
                parseEvent(year, department, id);
            }

        }

    }

    private void parseEvent(int year, String department, String id) {

        String file = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html").getFile();

        Document doc = JsoupUtils.INSTANCE.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return;
        }

        log.debug("DOC ID PARSED {}", doc);

        getGeneralInformation(doc);

    }

    private void getGeneralInformation(Document doc) {

        String title =  scrapingRepository.getTitle(doc);
        String code = scrapingRepository.getCode(doc);

        String department =  scrapingRepository.getDepartment(doc);
        String league = scrapingRepository.getLeague(doc);

        String dateBegin = scrapingRepository.getBeginDate(doc);
        String dateEnd = scrapingRepository.getEndDate(doc);

        String level = scrapingRepository.getLevel(doc);
        String type = scrapingRepository.getType(doc);


    }


}
