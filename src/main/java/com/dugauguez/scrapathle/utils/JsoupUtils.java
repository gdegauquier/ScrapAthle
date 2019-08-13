package com.dugauguez.scrapathle.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class JsoupUtils {

    public static JsoupUtils INSTANCE;
    @Value("${bases.athle.uri.base}")
    private String host;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public Document getDocument(File file) {
        try {
            Document doc = Jsoup.parse(file, "UTF-8", host);
            return doc;
        } catch (IOException e) {
            log.error("Document file could not be parsed", e);
        }
        return null;
    }

}