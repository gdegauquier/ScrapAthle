package com.dugauguez.scrapathle.repository;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ScrapingRepository {

    private String getPropertyViaParentNode(Elements els, String textToFind) {
        for (Element el : els) {
            if (el.parentNode().outerHtml().contains(textToFind)) {
                return el.text();
            }
        }
        return null;
    }

    public String getTitle(Document doc) {
        return doc.select("div.titles").first().childNodes().get(0).toString().replace("\n", "").trim();
    }

    public String getLeague(Document doc) {
        String title = doc.select("div.titles").text();
        return title.substring(title.lastIndexOf('(') + 1).split(" / ")[0];
    }

    public String getDepartment(Document doc) {
        String title = doc.select("div.titles").text();
        title = title.substring(title.lastIndexOf('(')).split(" / ")[1];
        return title.substring(0, title.length() - 1);
    }


    public String getBeginDate(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Date de Début");
    }

    public String getEndDate(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Date de Fin");
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

    public String getTown(Document doc) {
        return doc.select("div.titles").first().childNodes().get(3).childNodes().get(0).toString().split("\\(")[0].trim();
    }
}
