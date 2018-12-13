const express = require('express');
const router = express.Router();
const logger = require('./../../configuration/logger')();

const fs = require('fs').promises;

const { SCRAP_DIR } = process.env;

const cheerio = require('cheerio');
var he = require('he');
cheerioTableparser = require('cheerio-tableparser');

// move these into a service
const eventRepository = require('./../../../backend/repository/eventRepository');
const eventDetailRepository = require('./../../../backend/repository/eventDetailRepository');


const baseAthleDateUtils = require('./../../../backend/utils/BaseAthleDateUtils');



router.get(`/analysis/:type`, async function(req, res) {

    logger.info(`GET /analysis/${req.params.type}`);

    try {

        await analyseDir(SCRAP_DIR, req.params.type);

    } catch (err) {
        let errorMsg = `Error while reading directory : ${JSON.stringify(err)}`;
        logger.error(errorMsg);
        res.status(500).send(errorMsg);
        return;
    }

    // let fileName = `general_analysis_${new Date().getTime()}.html`;
    res.send('Analysis completed : ' + SCRAP_DIR);

})

async function analyseDir(dir, type) {

    logger.debug(`/analisys.analyseDir(${dir})`);

    let files = await fs.readdir(SCRAP_DIR);

    logger.info(`Files to analyze : ${files.length}`);

    for (let file of files) {

        if (!file.startsWith(type)) {
            continue;
        }

        // if (!file.startsWith("detail_319849555846697828244840224828730834_1543394819326.html")) {
        //     continue;
        // }

        try {

            if (type === 'detail') {
                await analyseFileDetail(dir + file);
            } else {
                await analyseFileGeneral(dir + file);
            }

        } catch (err) {
            let errorMsg = `Error while reading file ${file} : ${JSON.stringify(err)}`;
            logger.error(errorMsg);
        }

    }

}



async function analyseFileDetail(file) {

    logger.debug(`/analisys.analyseFileDetail(${file})`);

    var contents = await fs.readFile(file, 'utf8');
    logger.debug(`Size is ${contents.length}`);

    let $ = cheerio.load(contents);

    cheerioTableparser($);

    let data = $("#bddDetails").parsetable();

    let object = {};


    object.id_js = file.split("_")[1];
    object.date_event = {};
    object.date_event.begin = null;
    object.date_event.end = null;

    object.stadium = null;
    object.website = null;

    object.organizer = { type: "Organisation", name: null, email: null };

    object.address = {};
    object.address.lines = [];
    object.contacts = [];
    object.services = [];
    object.events = [];
    object.phones = [];
    object.event_types = [];
    object.family = null;

    object.conditions = null;
    object.other_information = null;
    object.technical_security_advice = null;
    object.online_engagement = null;

    object.label = $(".titles").text().trim().split("\n")[0];
    object.event_label = null;

    try {
        object.event_label = $("td[ style='text-align:right']")[2].childNodes[0].childNodes[0].data;
    } catch (e) {
        //
    }

    for (let indRow = 0; indRow < data.length; indRow++) {

        for (let indCol = 0; indCol < data[indRow].length; indCol++) {

            let value = data[indRow][indCol];

            if (!value || value.indexOf("<table cellpadding=\"2\" cellspacing=\"0\">") > -1) {
                continue;
            }

            // handled in DB
            if (value.indexOf("bddThrowContact") > -1) {
                object.stadium = he.decode(value.split(')">')[1].split("<")[0]);
            }

            // handled in DB
            if (value.indexOf("Niveau : ") > -1) {
                object.level = he.decode(value.split(">")[1].split("<")[0]);
            }

            // handled in DB
            if (value.indexOf("Code : ") > -1) {
                object.code = value.split(">")[1].split("<")[0];
            }

            // handled in DB
            if (value.indexOf("Type : ") > -1) {
                let types = value.split(">")[1].split("<")[0].split(" / ");
                let typesToAdd = [];
                object.family = types[0];

                let typesSplitted = types[1].split(" - ");
                for (let typeSplitted of typesSplitted) {
                    typesToAdd.push(he.decode(typeSplitted));
                }

                object.event_types = typesToAdd;

            }


            // handled in DB
            if (value.indexOf("Date de D") > -1) {
                object.date_event.begin = baseAthleDateUtils.formatDateForDB(value.split('">')[1].split("<")[0]);
            }
            // handled in DB
            if (value.indexOf("Date de Fin") > -1) {
                object.date_event.end = baseAthleDateUtils.formatDateForDB(value.split('>')[1].split("<")[0]);
            }

            // handled in DB
            if (value.indexOf("Organisateur") > -1) {
                object.organizer.name = he.decode(data[indRow + 2][indCol]);
            }

            // handled in DB
            if (he.decode(value).indexOf("Mèl") > -1) {
                object.organizer.email = data[indRow + 2][indCol].split(":")[1].split("?")[0];
            }

            // handled in DB
            if (value.indexOf("Site Web") > -1) {
                object.website = $(data[indRow + 2][indCol]).text();
            }

            if (value.indexOf("Adresse") > -1 &&
                object.address.lines.length === 0) {

                let currentRow = indRow + 2;
                let currentCol = indCol;
                object.address.lines.push(he.decode(data[currentRow][indCol]));

                // number of remaining rows
                for (let ind = 0; ind < 6; ind++) {
                    currentCol++;
                    let valueCol = data[currentRow][currentCol];

                    if (!valueCol || isNormalInteger(valueCol)) {
                        break;
                    }
                    object.address.lines.push(he.decode(valueCol));
                }

            }

            if (value.indexOf("Code Postal") > -1) {
                object.address.postal_code = data[indRow + 2][indCol];
            }

            if (value === "Ville") {
                object.address.town = he.decode(data[indRow + 2][indCol]);
            }

            if (he.decode(value).indexOf("Téléphone") > -1) {
                object.phones.push = data[indRow + 2][indCol];
            }

            // handled in DB
            if (value.indexOf("Engagement en ligne") > -1) {
                object.online_engagement = $(data[indRow + 2][indCol]).text();
            }

            // handled in DB
            if (he.decode(value).indexOf("Avis Technique et Sécurité") > -1) {
                object.technical_security_advice = he.decode(data[indRow + 2][indCol]);
            }

            // handled in DB
            if (value.search('Contact [A-Z].') > -1) {
                let contact = $(he.decode(data[indRow + 2][indCol])).text().split(" - ");
                let objContact = {}

                objContact.name = contact[0];
                objContact.type = value.split(" ")[1];
                objContact.email = null;
                if (contact.length > 0) {
                    objContact.email = contact[1];
                }

                object.contacts.push(objContact);

            }

            // handled in DB
            if (value.indexOf("Services") > -1) {

                let services = $(data[indRow + 2][indCol]);

                for (let service in services) {
                    try {
                        object.services.push(services[service].next.attribs.alt);
                    } catch (e) {
                        logger.warn("Service impossible à déterminer " + JSON.stringify(e));
                    }
                }

            }

            if (value.indexOf("plus.gif") > -1 && value.length < 1000) {

                let event = {};

                let dateHour = data[indRow + 1][indCol].split(" ");
                if (dateHour.length > 1) {
                    event.date = he.decode(dateHour[0].split(">")[1]).trim();
                    event.hour = he.decode(dateHour[1].split("<")[0]).trim();
                } else {
                    event.date = null;
                    event.hour = he.decode(dateHour[0].split(">")[1].split("<")[0]).trim();
                }

                let label = $(data[indRow + 2][indCol]).text().split(" - ");
                event.label = he.decode(label[0]);
                event.type = he.decode(label[1]);

                event.categories = data[indRow + 3][indCol].split(" / ");
                event.distance = data[indRow + 4][indCol];

                // RAZ if necessary

                if (event.label === null || event.label.trim() === "") {
                    event.label = null;
                }

                if (event.date === null || event.date.trim() === "") {
                    event.date = null;
                }
                if (event.hour === null || event.hour.trim() === "") {
                    event.hour = null;
                }

                if (event.distance === null || event.distance.trim() === "") {
                    event.distance = null;
                }

                object.events.push(event);

            }

            // handled in DB
            if (value === "Conditions") {
                object.conditions = he.decode(data[indRow + 2][indCol]);
            }

            // handled in DB
            if (value === "Autres Infos") {
                object.other_information = he.decode(data[indRow + 2][indCol]);
            }

            // handled in DB
            if (value === "Inscrite au calendrier par") {
                let objContact = {};
                let val = he.decode(data[indRow + 2][indCol]).split(" - ");
                objContact.name = val[0];
                objContact.type = "Calendrier"
                objContact.email = val.length > 1 && val[1].indexOf("mailto:") > -1 ? val[1].split(":")[1].split("?")[0] : null;
                object.contacts.push(objContact);
            }

            // handled in DB
            if (he.decode(value) === "Résultats chargés par") {
                let objContact = {};
                let val = he.decode(data[indRow + 2][indCol]).split(" - ");
                objContact.name = val[0];
                objContact.type = "Résultats - envoi";
                objContact.email = val.length > 1 && val[1].indexOf("mailto:") > -1 ? val[1].split(":")[1].split("?")[0] : null;
                object.contacts.push(objContact);
            }

            // handled in DB
            if (he.decode(value) === "Puis contrôlés par") {
                let objContact = {};
                let val = he.decode(data[indRow + 2][indCol]).split(" - ");
                objContact.name = val[0];
                objContact.type = "Résultats - contrôle";
                objContact.email = val.length > 1 && val[1].indexOf("mailto:") > -1 ? val[1].split(":")[1].split("?")[0] : null;
                object.contacts.push(objContact);
            }

        }

    }

    logger.info(`line : ${JSON.stringify(object)}`);

    object.contacts.push(object.organizer);
    await eventDetailRepository.upsert(object);

}

function isNormalInteger(str) {
    return /^\+?(0|[1-9]\d*)$/.test(str);
}


async function analyseFileGeneral(file) {

    logger.debug(`/analisys.analyseFileGeneral(${file})`);
    var contents = await fs.readFile(file, 'utf8');
    logger.debug(`Size is ${contents.length}`);

    let $ = cheerio.load(contents);
    cheerioTableparser($);
    let data = $("#ctnCalendrier").parsetable();


    // column 
    for (let indRow = 0; indRow < data[0].length; indRow++) {

        let object = {};

        let value = he.decode(data[6][indRow]);

        if (value.indexOf("javascript:bddThrowCompet") > -1) {
            object.id_js = value.split("'")[1];
        } else {
            continue;
        }

        let dates = baseAthleDateUtils.extractDatesFromString($(data[2][indRow]).text(), '2019');
        object.date_event = {
            begin: dates.begin,
            // for the main entity, we have to take the begin value
            end: dates.begin
        };

        object.family = $(data[4][indRow]).text();

        object.label = $(he.decode(data[6][indRow])).text();

        object.town = he.decode(data[8][indRow]);

        object.league = $(he.decode(data[10][indRow])).text();

        object.department = $(data[12][indRow]).text();

        object.event_type = data[14][indRow];

        object.level = he.decode(data[16][indRow]);

        object.stamp = null;
        try {
            object.stamp = $(data[18][indRow])[0].attribs.title;
        } catch (ex) {
            //
        }

        logger.debug(`line : ${JSON.stringify(object)}`);

        await eventRepository.upsert(object);

    }

}




module.exports = router;