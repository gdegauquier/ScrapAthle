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
                Node node = el.parentNode();

                if (node.childNodes().size() < 3) {
                    props.put(el.text(), "");
                }
                Node node1 = node.childNode(2);
                if (node1.childNodes().isEmpty()) {
                    props.put(el.text(), "");
                } else {
                    String string = node1.childNode(0).toString();
                    props.put(el.text(), string);
                }
            }
        }

        return props;
    }

    public String getTitle(Document doc) {
        Element first = doc.select("div.titles").first();
        if (first == null) {

            return "";
        }
        return first.childNodes().get(0).toString().replace("\n", "").trim();
    }

    public String getLeague(Document doc) {
        String title = doc.select("div.titles").text();
        return title.substring(title.lastIndexOf('(') + 1).split(" / ")[0];
    }

    public String getDepartment(Document doc) {
        String title = doc.select("div.titles").text();
        int beginIndex = title.lastIndexOf('(');
        if (beginIndex < 0) {
            return "";
        }
        String substring = title.substring(beginIndex);
        if (substring.split(" / ").length == 1) {
            return "";
        }
        title = substring.split(" / ")[1];
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

    private Map<String, String> parseAddressElements(Elements els, String begin, String end, String type) {

        if (els.isEmpty() || els.get(0).parentNode().childNodes().isEmpty()) {
            return null;
        }

        AtomicInteger nbLines = new AtomicInteger(1);
        Map<String, String> address = new HashMap<>();
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
            address.put(getAddressColumnName(rawNode.toString(), nbLines),
                        getAddressColumnValue(rawNodeValue));
        }

        if (address.containsKey("stName")) {
            address.put("Type", "STD");
            address.put("name", address.get("stName"));
            address.remove("stName");
        }

        if (address.containsKey("orgName")) {
            address.put("Type", "ORG");
            address.put("name", address.get("orgName"));
            address.remove("orgName");
        }

        return address;

    }

    private String getAddressColumnName(String rawColumn, AtomicInteger nbLines) {

        if (rawColumn.equals("Stade")) {
            rawColumn = "stName";
            return rawColumn;
        }

        if (rawColumn.contains("Org")) {
            rawColumn = "orgName";
            return rawColumn;
        }

        if (rawColumn.equals("&nbsp;") || rawColumn.equals("Adresse")) {
            rawColumn = "Line" + nbLines;
            nbLines.incrementAndGet();
            return rawColumn;
        }
        return rawColumn;
    }

    private String getAddressColumnValue(Node rawColumnValue) {

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

    public Map<String, Map<String, String>> getTests(Document doc) {

        Map<String, Map<String, String>> tests = new HashMap<>();

        Elements els = doc.select("td[style*=width:5%; padding:3px 0px 3px 10px; white-space:nowrap; color:#444]");

        if (els == null) {
            return null;
        }

        int index = 1;


        for (Element el : els) {

            List<Node> nodes = els.get(index - 1).getElementsByTag("td").get(0).parentNode().childNodes();

            Map<String, String> test = new HashMap<>();

            test.put("time", nodes.get(1).childNodes().get(0).childNode(0).toString());
            test.put("description", buildTestDescription(nodes));
            // TODO split with " / "
            test.put("categories", nodes.get(3).childNodes().get(0).toString());
            test.put("distance", !nodes.get(4).childNodes().isEmpty() ? nodes.get(4).childNodes().get(0).toString() : "");
            tests.put("test" + index, test);

            index++;
        }

        return tests;

    }

    private String buildTestDescription(List<Node> nodes) {

        String desc = nodes.get(2).childNodes().get(0).childNodes().get(0).toString();

        if (nodes.get(2).childNodes().size() > 2) {
            desc += " - " + nodes.get(2).childNodes().get(2).childNodes().get(0).toString();
        }

        return desc;

    }

    public Map<String, String> getStaff(Document doc) {
        Elements els = doc.select("td[style*=font-weight:bolder]");
        return getPropertiesViaParentNode(els, " par");
    }

    public Map<String, String> getStadiumAddress(Document doc) {

        String end = "padding:10px;text-align:left;width:100%";

        Elements els = doc.select("td[style=" + end + "]");
        return parseAddressElements(els, null, end, "STD");
    }

    public Map<String, String> getOrganisationAddress(Document doc) {

        String begin = "padding:10px;text-align:left;width:100%";
        String end = "padding:5px;text-align:left;width:100%";

        Elements elsBegin = doc.select("td[style=" + begin + "]");
        Elements els = doc.select("td[style=" + end + "]");

        return parseAddressElements(els, elsBegin.isEmpty() ? null : begin, end, "ORG");
    }

    public String getType(Document doc) {
        Elements els = doc.select("b");
        return getPropertyViaParentNode(els, "Type");
    }

    public String getTown(Document doc) {
        Elements select = doc.select("div.titles");
        Element first = select.first();
        if (first == null) {
            return "";
        }
        List<Node> nodes = first.childNodes();
        Node node = nodes.get(3);
        List<Node> nodes1 = node.childNodes();
        Node node1 = nodes1.get(0);
        return node1.toString().split("\\(")[0].trim();
    }
}
