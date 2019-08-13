package com.dugauguez.scrapathle.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "bases.athle.uri")
public class JsoupUtils {

    private String base;

    @Bean
    public JsoupUtils jsoupUtilsBean() {
        return new JsoupUtils();
    }

    public Document getDocument(File file) {
        try {
            return Jsoup.parse(file, StandardCharsets.UTF_8.displayName(), base);
        } catch (IOException e) {
            log.error("Document file could not be parsed", e);
        }
        return null;
    }

}