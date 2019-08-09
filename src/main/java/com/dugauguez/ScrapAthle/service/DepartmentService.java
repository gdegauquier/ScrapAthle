package com.dugauguez.ScrapAthle.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    public static final String FOO_URI = "http://example.com/";

    public List<String> getAll(){

        // prepare result list
        List<String> listDepartments = new ArrayList<>();

        // open departments.txt
        URL fileUrl = getClass().getResource("/departments.txt");
        File file = new File(fileUrl.getFile());

        // parse file
        Document doc;

        try {
            doc = Jsoup.parse(file, "UTF-8", FOO_URI);
        }catch(IOException e){
            return listDepartments;
        }



        Elements elements = doc.getElementsByAttribute("value");

        // loop on values && add them to the response
        for (Element e : elements) {

            if (!StringUtils.isEmpty(e.text()) && e.text().length() < 5) {
                listDepartments.add(e.text());
            }

        }

        return listDepartments;

    }

}
