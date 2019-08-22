package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.repository.AddressRepository;
import com.dugauguez.scrapathle.repository.EventRepository;
import com.dugauguez.scrapathle.repository.ScrapingRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;


@Slf4j
@Service
public class ScrapingService {

    public static final int OLDER_THAN_TWO_DAYS = 2;
    @Autowired
    FileService fileService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ScrapingRepository scrapingRepository;

    @Async
    public void getAllByYear(int year) {

        log.debug("Parsing is starting...");

        long startProcessing = System.currentTimeMillis();

        String dir = getClass().getResource("/data/" + year).getFile();

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        List<Event> all = new ArrayList<>();

        Arrays.stream(listOfFiles)
                .parallel()
                .filter(file -> isValidFile(year, file.getAbsolutePath()))
                .forEach(file -> {
                    List<Event> eventList = scrapEvents(file);
                    all.addAll(eventList);
                });

        eventRepository.saveAll(all);

        long duration = System.currentTimeMillis() - startProcessing;
        log.debug("Elapsed time : {} min {} s {} ms", duration / 1000 / 60, duration / 1000 % 60, (duration - ((duration / 1000 % 60) * 1000)));
        log.info("There are {} events", all.size());
    }

    private boolean isValidFile(int year, String file) {
        return file.contains(year + "") && file.endsWith(".html");
    }

    private boolean hasDetailFileToBeRetrieved(int year, String department, String id) {
        URL url = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html");

        if (url == null) {
            return true;
        }

        String path = url.getFile();
        File test = new File(path);

        if (!test.exists()) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate fileTime = Instant.ofEpochMilli(test.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(fileTime, now);
        int diff = period.getDays();

        return diff >= OLDER_THAN_TWO_DAYS;

    }


    private List<Event> scrapEvents(File file) {

        int year = Integer.parseInt(file.getParentFile().getName());
        String department = file.getName().split(".html")[0];

        Document doc = JsoupUtils.instance.getDocument(file);

        Elements elements = doc.getElementsByAttribute("href");

        List<Event> enventList = new ArrayList<>();

        elements
                .stream()
                .parallel()
                .filter(element -> element.attr("href").contains("bddThrowCompet"))
                .forEach(element -> {
                    String id = element.attr("href").split("'")[1];
                    if (hasDetailFileToBeRetrieved(year, department, id)) {
                        fileService.getById(year, department, id);
                    }
                    enventList.add(parseEvent(year, department, id));
                });
        return enventList;
    }

    private Event parseEvent(int year, String department, String id) {

        //  department = "021";
        //  id = "903849522846443840174834256852468837";

        String file = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html").getFile();

        Document doc = JsoupUtils.instance.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return null;
        }

        Map<String, String> collectMap = new HashMap<>();

        collectMap.put("fileId", id);
        collectMap.put("code", scrapingRepository.getCode(doc));

        collectMap.put("beginDate", scrapingRepository.getBeginDate(doc));
        collectMap.put("endDate", scrapingRepository.getEndDate(doc));

        collectMap.put("title", scrapingRepository.getTitle(doc));
        collectMap.put("town", scrapingRepository.getTown(doc));

        collectMap.put("league", scrapingRepository.getLeague(doc));
        collectMap.put("department", scrapingRepository.getDepartment(doc));

        collectMap.put("type", scrapingRepository.getType(doc));
        collectMap.put("level", scrapingRepository.getLevel(doc));

        collectMap.put("technicalAdvice", scrapingRepository.getTechnicalAdvice(doc));

        // handle addresses
        Map<String, Map<String, String>> addresses = new HashMap<>();
        addresses.put("stadiumAddress", scrapingRepository.getStadiumAddress(doc));
        addresses.put("organisationAddress", scrapingRepository.getOrganisationAddress(doc));

        final ObjectMapper mapper = new ObjectMapper(); // jackson's object mapper to change with orika or mapstruct
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, String> stadiumAddress = scrapingRepository.getStadiumAddress(doc);
        if (stadiumAddress != null) {
            Address address = mapper.convertValue(stadiumAddress, Address.class);
            addressRepository.save(address);
        }

        Map<String, String> organisationAddress = scrapingRepository.getOrganisationAddress(doc);
        if (organisationAddress != null) {
            Address address = mapper.convertValue(organisationAddress, Address.class);
            addressRepository.save(address);
        }

        //handle contacts
        Map<String, String> contacts = scrapingRepository.getContacts(doc);
        Map<String, String> staff = scrapingRepository.getStaff(doc);

        // parse tests (epreuves)
        Map<String, Map<String, String>> tests = new HashMap<>();
        tests = scrapingRepository.getTests(doc);


        Map<String, String> organisationAdress = scrapingRepository.getOrganisationAdress(doc);
        if (organisationAdress != null) {
            Address address = mapper.convertValue(organisationAdress, Address.class);
            addressRepository.save(address);
        }


        //handle contacts
        Map<String, String> contacts = scrapingRepository.getContacts(doc);
        Map<String, String> staff = scrapingRepository.getStaff(doc);

        return mapper.convertValue(collectMap, Event.class);
    }

        Event event = mapper.convertValue(collectMap, Event.class);
        eventRepository.save(event);
        return event;
    }

}
