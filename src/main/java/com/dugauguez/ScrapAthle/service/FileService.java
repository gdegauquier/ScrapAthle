package com.dugauguez.ScrapAthle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Slf4j
@Service
public class FileService {

    @Value("${bases.athle.uri.base}")
    private String host;

    @Value("${bases.athle.uri.list}")
    private String hostArgs;


    @Autowired
    DepartmentService departmentService;

    @Async
    public void getAllByYear(int year) {

        List<String> departments = departmentService.getAll();

        for (String department : departments) {

            String uri = getHostAndArgs(year, department);

            URLConnection con;
            URL url;
            BufferedReader in;
            String lf = "";
            String l;

            try {
                url = new URL(uri);
                con = url.openConnection();
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((l = in.readLine()) != null) {
                    lf += l;
                }
            } catch (Exception e) {
                log.info("Could not read the file {}/{}", year, department, e);
                continue;
            }

            String dir = getClass().getResource("/").getFile();
            OutputStream os;

            try {
                os = new FileOutputStream(dir + "/data_general_" + year + "_" + department + ".txt");
            } catch (FileNotFoundException f) {
                log.info("Could not write the file {}/{}", year, department);
                continue;
            }

            final PrintStream printStream = new PrintStream(os);
            printStream.println(lf);
            printStream.close();

        }


    }

    // TO IMPROVE with a FORMATTER
    private String getHostAndArgs(int year, String department) {

        String ret = host.concat(hostArgs);
        ret = ret.replace("%YEAR%", year + "");
        ret = ret.replace("%DEPARTMENT%", department);

        return ret;

    }

}
