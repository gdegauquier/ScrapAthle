package com.dugauguez.scrapathle.service;

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

    @Autowired
    DepartmentService departmentService;
    @Value("${bases.athle.uri.base}")
    private String host;
    @Value("${bases.athle.uri.list}")
    private String hostArgsByYearAndDepartment;
    @Value("${bases.athle.uri.detail}")
    private String hostArgsById;

    @Async
    public void getAllByYear(int year) {

        List<String> departments = departmentService.getAll();

        for (String department : departments) {

            String lf = getWebPageAsString(year, department);

            if (lf != null) {
                writeWebPageOnLocalFile(year, department, lf);
            }
        }

    }

    public void getById(int year, String department, String id) {

        String lf = getWebPageAsString(id);

        if (lf == null) {
            return;
        }

        writeWebPageOnLocalFile(year, department, id, lf);

    }


    // TODO: TO IMPROVE with a FORMATTER
    private String getHostAndArgs(int year, String department) {

        String ret = host.concat(hostArgsByYearAndDepartment);
        ret = ret.replace("%YEAR%", year + "");
        ret = ret.replace("%DEPARTMENT%", department);

        return ret;

    }

    // TODO: TO IMPROVE with a FORMATTER
    private String getHostAndArgs(String id) {

        String ret = host.concat(hostArgsById);
        ret = ret.replace("%ID%", id);

        return ret;

    }

    private String getWebPageAsString(String id) {

        String uri = getHostAndArgs(id);

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
            log.error("Could not read the file {}", id, e);
            return null;
        }

        return lf;

    }

    private String getWebPageAsString(int year, String department) {

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
            log.error("Could not read the file {}/{}", year, department, e);
            return null;
        }

        return lf;

    }

    private void writeWebPageOnLocalFile(int year, String department, String content) {

        String dir = getClass().getResource("/").getFile();
        String fileStr = dir + "/data/" + year + "/" + department + ".html";
        new File(fileStr).getParentFile().mkdirs();
        OutputStream os;

        try {
            os = new FileOutputStream(fileStr);
        } catch (Exception e) {
            log.error("Could not write the file {}/{}", year, department, e);
            return;
        }

        final PrintStream printStream = new PrintStream(os);
        printStream.println(content);
        printStream.close();
    }

    private void writeWebPageOnLocalFile(int year, String department, String id, String content) {

        String dir = getClass().getResource("/").getFile();
        String fileStr = dir + "/data/" + year + "/" + department + "/" + id + ".html";
        new File(fileStr).getParentFile().mkdirs();
        OutputStream os;

        try {
            os = new FileOutputStream(fileStr);
        } catch (Exception e) {
            log.error("Could not write the file {}", id, e);
            return;
        }

        final PrintStream printStream = new PrintStream(os);
        printStream.println(content);
        printStream.close();
    }

}
