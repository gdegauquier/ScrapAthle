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
const baseAthleDateUtils = require('./../../../backend/utils/BaseAthleDateUtils');



router.get(`/analysis`, async function(req, res) {

    logger.info(`GET /analysis`);

    try {

        await analyseDir(SCRAP_DIR);

    } catch (err) {
        let errorMsg = `Error while reading directory : ${JSON.stringify(err)}`;
        logger.error(errorMsg);
        res.status(500).send(errorMsg);
        return;
    }

    let fileName = `general_analysis_${new Date().getTime()}.html`;
    res.send('Analysis completed : ' + fileName);

})

async function analyseDir(dir) {

    logger.debug(`/analisys.analyseDir(${dir})`);

    let files = await fs.readdir(SCRAP_DIR);

    logger.info(`Files to analyze : ${files.length}`);

    for (let file of files) {
        try {
            await analyseFile(dir + file);
        } catch (err) {
            let errorMsg = `Error while reading file ${file} : ${JSON.stringify(err)}`;
            logger.error(errorMsg);
        }

    }

}

async function analyseFile(file) {

    logger.debug(`/analisys.analyseFile(${file})`);
    var contents = await fs.readFile(file, 'utf8');
    logger.debug(`Size is ${contents.length}`);

    let $ = cheerio.load(contents);
    cheerioTableparser($);
    let data = $("#ctnCalendrier").parsetable();


    // column 
    for (let indRow = 0; indRow < data[0].length; indRow++) {

        // let line = "";
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