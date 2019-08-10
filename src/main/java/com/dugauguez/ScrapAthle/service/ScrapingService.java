package com.dugauguez.ScrapAthle.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ScrapingService {

    @Value("${bases.athle.uri.base}")
    private String host;

    @Async
    public void getAllByYear(int year) {

        String dir = getClass().getResource("/").getFile();

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (!isValidFile(year, file.getName())) {
                continue;
            }

            log.info("File being scraped is {}", file.getName());
            scrap(file);

        }

    }

    private boolean isValidFile(int year, String file) {
        return file.contains(year + "") && file.endsWith(".html");
    }

    private void scrap(File file) {

        Document doc;

        try {
            doc = Jsoup.parse(file, StandardCharsets.UTF_8.displayName(), host);
        } catch (Exception e) {
            log.error("File {} could  not be parsed", e);
            return;
        }

        Map<String, String> parsed = new HashMap<>();

        parsed.put("title", doc.title());

        Elements elements = doc.getElementsByClass("datas0");

        int indexEl = 1;
        
        for (Element element : elements) {
            parsed.put("data_" + indexEl, element.text());
            indexEl++;
        }


        log.info("File {} parsed {}", file.getName(), parsed);

    }

}
