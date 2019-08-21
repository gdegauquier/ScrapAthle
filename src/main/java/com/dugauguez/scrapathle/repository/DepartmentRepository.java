package com.dugauguez.scrapathle.repository;


import com.dugauguez.scrapathle.utils.JsoupUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class DepartmentRepository {


    @Value("${bases.athle.uri.base}")
    private String host;

    public List<String> getAll() {

        // prepare result list
        List<String> listDepartments = new ArrayList<>();

        // parse file
        File file = getDepartmentsFile();
        Document doc = JsoupUtils.INSTANCE.getDocument(file);
        if (doc == null) {
            return listDepartments;
        }

        return extractDepartments(doc);
    }

    private List<String> extractDepartments(Document doc) {

        Elements elements = doc.getElementsByAttribute("value");

        log.info("[extractDepartments] Starting calculate supplier size");
        long startProcessing = System.currentTimeMillis();

        // loop on values && add them to the response
        List<String> listDepartments = elements.parallelStream()
                                               .filter(e -> !StringUtils.isEmpty(e.text()) && e.text().length() < 5)
                                               .map(e -> e.text())
                                               .collect(Collectors.toList());
        log.info("[extractDepartments] Elapsed time : {} ms", System.currentTimeMillis() - startProcessing);

        return listDepartments;

    }

    private File getDepartmentsFile() {
        // open departments.html
        URL fileUrl = getClass().getResource("/departments.html");
        return new File(fileUrl.getFile());
    }


}
