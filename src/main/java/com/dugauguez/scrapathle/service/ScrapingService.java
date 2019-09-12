package com.dugauguez.scrapathle.service;

import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.entity.Event;
import com.dugauguez.scrapathle.entity.Organizer;
import com.dugauguez.scrapathle.repository.AddressRepository;
import com.dugauguez.scrapathle.repository.EventRepository;
import com.dugauguez.scrapathle.repository.OrganizerRepository;
import com.dugauguez.scrapathle.repository.ScrapingRepository;
import com.dugauguez.scrapathle.utils.JsoupUtils;
import com.dugauguez.scrapathle.utils.OpenStreetMapUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
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
import java.util.stream.Collectors;


@Slf4j
@Service
public class ScrapingService {

    @Autowired
    OpenStreetMapUtils openStreetMapUtils;

    @Autowired
    JsoupUtils jsoupUtils;

    public static final int OLDER_THAN_TWO_DAYS = 2;
    @Autowired
    FileService fileService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrganizerRepository organizerRepository;

    @Autowired
    ScrapingRepository scrapingRepository;

    @Async
    public void getAllByYear(int year) {

        log.info("Parsing is starting...");

        long startProcessing = System.currentTimeMillis();

        String dir = getClass().getResource("/data/" + year).getFile();

        File folder = new File(dir);

        List<Event> all = Arrays.stream(folder.listFiles())
                .filter(file -> isValidFile(year, file.getAbsolutePath()))
                .map(file -> scrapEvents(file))
                .flatMap(List::stream)
                .collect(Collectors.toList());


        List<Event> allDistinctEvent = new ArrayList<>(all.stream()
                .parallel()
                .filter(e -> e.getCode() != null)
                .collect(Collectors.toMap(Event::getCode, p -> p, (p, q) -> p))
                .values());

        eventRepository.saveAll(allDistinctEvent);

        long duration = System.currentTimeMillis() - startProcessing;
        log.info("Elapsed time : {} min {} s {} ms", duration / 1000 / 60, duration / 1000 % 60, duration % 1000);
        log.info("There are {} events", allDistinctEvent.size());
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

        Document doc = jsoupUtils.getDocument(file);

        List<String> ids = doc.getElementsByAttribute("href")
                .parallelStream()
                .filter(element -> element.attr("href").contains("bddThrowCompet"))
                .map(element -> element.attr("href").split("'")[1])
                .collect(Collectors.toList());

        ids.parallelStream()
                .filter(id -> hasDetailFileToBeRetrieved(year, department, id))
                .forEach(id -> fileService.getById(year, department, id));

        return ids.stream()
                .map(id -> parseEvent(year, department, id))
                .collect(Collectors.toList());

    }

    private Event parseEvent(int year, String department, String id) {

        // department = "021";
        // id = "903849522846443840174834256852468837";

        // department = "069";
        // id = "764849668846493828149846125855762849";

        String file = getClass().getResource("/data/" + year + "/" + department + "/" + id + ".html").getFile();

        Document doc = jsoupUtils.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return null;
        }

        Map<String, String> collectMap = new HashMap<>();

        collectMap.put("fileId", id);
        collectMap.put("code", scrapingRepository.getCode(doc));

        collectMap.put("title", scrapingRepository.getTitle(doc));
        collectMap.put("town", scrapingRepository.getTown(doc));

        collectMap.put("league", scrapingRepository.getLeague(doc));
        collectMap.put("department", scrapingRepository.getDepartment(doc));
        collectMap.put("labeledEvent", scrapingRepository.isLabeledEvent(doc) + "");

        // types
        String type = scrapingRepository.getType(doc);
        List<String> subTypes = new ArrayList<>();

        if (type != null) {
            collectMap.put("type", type.split(" / ")[0]);
            subTypes = Arrays.asList(type.split(" / ")[1].split(" - "));
        }

        // services
        List<String> services = scrapingRepository.getServices(doc);

        collectMap.put("level", scrapingRepository.getLevel(doc));
        collectMap.put("technicalAdvice", scrapingRepository.getTechnicalAdvice(doc));

        // handle addresses
        Map<String, Map<String, String>> addresses = new HashMap<>();
        addresses.put("stadiumAddress", scrapingRepository.getStadiumAddress(doc));
        addresses.put("organisationAddress", scrapingRepository.getOrganisationAddress(doc));

        //handle contacts with mails
        Map<String, String> contacts = scrapingRepository.getContacts(doc);
        Map<String, String> staff = scrapingRepository.getStaff(doc);

        // parse tests (epreuves)
        Map<String, Map<String, String>> tests = scrapingRepository.getTests(doc);

        // parse conditions
        Map<String, String> conditions = scrapingRepository.getConditions(doc);



        final ObjectMapper mapper = new ObjectMapper(); // jackson's object mapper to change with orika or mapstruct
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Event event = mapper.convertValue(collectMap, Event.class);

        Map<String, String> stadiumAddress = scrapingRepository.getStadiumAddress(doc);
        if (stadiumAddress != null) {
            Address address = mapper.convertValue(stadiumAddress, Address.class);

            if (address.getName() != null && address.getTown() != null && address.getPostalCode() != null) {
                Address foundAddress = addressRepository.findByNameAndTownAndPostalCode(address.getName(), address.getTown(), address.getPostalCode());
                if (foundAddress == null) {
                    addressRepository.save(address);
                    event.setStadiumAddress(address);
                } else {
                    event.setStadiumAddress(foundAddress);
                }
            }
        }

        Map<String, String> organisationAddress = scrapingRepository.getOrganisationAddress(doc);
        if (organisationAddress != null) {
            Organizer organizer = mapper.convertValue(organisationAddress, Organizer.class);
            Organizer foundOrganizerContact = organizerRepository.findByOrganisationAndTownAndPostalCode(organizer.getOrganisation(), organizer.getTown(), organizer.getPostalCode());
            if (foundOrganizerContact == null) {
                organizerRepository.save(organizer);
                event.setOrganizerContact(organizer);
            } else {
                event.setOrganizerContact(foundOrganizerContact);
            }
        }

        event.setDateDeDebut(scrapingRepository.getBeginDate(doc));
        event.setDateDeFin(scrapingRepository.getEndDate(doc));

        return event;
    }


    public List<Address> stadiumInTown(Integer postalCode) {
        List<Address> stade = addressRepository.findByTypeAndPostalCodeStartsWith("STD", postalCode.toString());

        stade.stream().parallel()
                .forEach(sta -> {
                    Map<String, Double> coordinates = openStreetMapUtils.getCoordinates(sta.getAddress());
                    sta.setLatitude(coordinates.get("lat"));
                    sta.setLongitude(coordinates.get("lon"));
                });

        addressRepository.saveAll(stade);

        log.info("stadiumInTown : " + stade.size());
        return stade;
    }
}

//TODO: improve epreuves conditions
// improve conditions on tests part