package com.dugauguez.scrapathle.repository;


import com.dugauguez.scrapathle.utils.JsoupUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DepartmentRepository {


    @Value("${bases.athle.uri.base}")
    private String host;

    @Autowired
    private JsoupUtils jsoupUtils;

    public List<String> getAll() {

        // prepare result list
        List<String> listDepartments = new ArrayList<>();

        // parse file
        File file = getDepartmentsFile();
        Document doc = jsoupUtils.getDocument(file);
        if (doc == null) {
            return listDepartments;
        }

        return extractDepartments(doc);
    }

    private List<String> extractDepartments(Document doc) {

        List<String> listDepartments = new ArrayList<>();
        Elements elements = doc.getElementsByAttribute("value");

        // loop on values && add them to the response
        for (Element e : elements) {

            if (!StringUtils.isEmpty(e.text()) && e.text().length() < 5) {
                listDepartments.add(e.text());
            }

        }
        return listDepartments;

    }

    private File getDepartmentsFile() {
        // open departments.html
        URL fileUrl = getClass().getResource("/departments.html");
        return new File(fileUrl.getFile());
    }


}
