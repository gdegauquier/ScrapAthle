package com.dugauguez.scrapathle.repository;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
public class ScrapingRepository {

    private String getPropertyViaParentNode(Elements els, String textToFind) {
        return getPropertyViaParentNode(els, textToFind, false);
    }

    private String getPropertyViaParentNode(Elements els, String textToFind, boolean valueAfterDots) {
        for (Element el : els) {
            if (el.parentNode().outerHtml().contains(textToFind)) {

                if (valueAfterDots) {
                    return el.parentNode().childNode(2).childNode(0).toString();
                }

                return el.text();
            }
        }
        return null;
    }

    private Map<String, String> getPropertiesViaParentNode(Elements els, String textToFind) {
        Map<String, String> props = new HashMap<>();

        for (Element el : els) {
            if (el.parentNode().outerHtml().contains(textToFind) && el.text().contains(textToFind)) {
                props.put(el.text(), el.parentNode().childNode(2).childNode(0).toString());
            }
        }

        return props;
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

    private Map<String, String> parseAdressElements(Elements els, String begin, String end) {

        if (els.size() == 0 || els.get(0).parentNode().childNodes().size() == 0) {
            return null;
        }

        AtomicInteger nbLines = new AtomicInteger(1);
        Map<String, String> adress = new HashMap<>();
        List<Node> nodes = els.get(0).parentNode().parentNode().childNodes();

        boolean canParse = begin == null ? true : false;

        for (Node node : nodes) {

            // Parsing can begin ?
            if (begin != null && !canParse) {
                if (node.toString().contains(begin)) {
                    canParse = true;
                }
            }

            // End is reached
            if (end != null && node.toString().contains(end)) {
                break;
            }

            if (!canParse) {
                continue;
            }

            // TR is empty
            if (node.childNodes().size() != 3) {
                continue;
            }

            // TR has column : value
            Node rawNode = node.childNodes().get(0).childNodes().get(0);
            Node rawNodeValue = node.childNodes().get(2).childNodes().get(0);
            adress.put(getAdressColumnName(rawNode.toString(), nbLines),
                    getAdressColumnValue(rawNodeValue));
        }

        return adress;

    }

    private String getAdressColumnName(String rawColumn, AtomicInteger nbLines) {
        if (rawColumn.equals("&nbsp;")) {
            rawColumn = "Line" + nbLines;
            nbLines.incrementAndGet();
        }
        return rawColumn;

    }

    private String getAdressColumnValue(Node rawColumnValue) {

        boolean hasHref = rawColumnValue.toString().indexOf("href") > -1;

        int nbChilds = rawColumnValue.parent().childNodes().size();


        String value = rawColumnValue.toString();

        if (hasHref) {

            if (nbChilds > 1) {
                value = rawColumnValue.parent().childNodes().get(1).childNode(0).toString();
            } else {
                value = rawColumnValue.parent().childNodes().get(0).childNode(0).toString();
            }

        }

        return value;

    }

    public String getTechnicalAdvice(Document doc) {
        Elements els = doc.select("td[style*=font-weight:bolder]");
        return getPropertyViaParentNode(els, "Avis Technique et Sécurité", true);
    }

    public Map<String, String> getContacts(Document doc) {
        Elements els = doc.select("td[style*=font-weight:bolder]");
        return getPropertiesViaParentNode(els, "Contact ");
    }

    public Map<String, String> getStaff(Document doc) {
        Elements els = doc.select("td[style*=font-weight:bolder]");
        return getPropertiesViaParentNode(els, " par");
    }

    public Map<String, String> getStadiumAdress(Document doc) {

        String end = "padding:10px;text-align:left;width:100%";

        Elements els = doc.select("td[style=" + end + "]");
        return parseAdressElements(els, null, end);
    }

    public Map<String, String> getOrganisationAdress(Document doc) {

        String begin = "padding:10px;text-align:left;width:100%";
        String end = "padding:5px;text-align:left;width:100%";

        Elements elsBegin = doc.select("td[style=" + begin + "]");
        Elements els = doc.select("td[style=" + end + "]");

        return parseAdressElements(els, elsBegin.size() == 0 ? null : begin, end);
    }

    public String getType(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Type");
    }

    public String getTown(Document doc) {
        return doc.select("div.titles").first().childNodes().get(3).childNodes().get(0).toString().split("\\(")[0].trim();
    }
}
