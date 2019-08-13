package com.dugauguez.scrapathle.repository;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ScrapingRepository {

    private String getPropertyViaParentNode(Elements els, String textToFind){
        for (Element el : els){
            if (el.parentNode().outerHtml().contains(textToFind)){
                return el.text();
            }
        }
        return null;
    }

    public String getBeginDate(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Date de Début");
    }

    public String getCode(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Code");
    }

    public String getLevel(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Niveau");
    }

    public String getType(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Type");
    }
}
