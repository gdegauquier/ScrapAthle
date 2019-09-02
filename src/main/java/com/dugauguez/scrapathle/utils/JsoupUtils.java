package com.dugauguez.scrapathle.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class JsoupUtils {

    @Value("${bases.athle.uri.base}")
    private String host;


    public Document getDocument(File file) {
        try {
            return Jsoup.parse(file, "UTF-8", host);
        } catch (IOException e) {
            log.error("Document file could not be parsed", e);
        }
        return null;
    }

}