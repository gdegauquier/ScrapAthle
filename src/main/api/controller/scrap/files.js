const express = require('express');
const router = express.Router();
const logger = require('./../../configuration/logger')();

const got = require('got');
const fs = require('fs').promises;



router.get(`/files/:year/:department`, async function(req, res) {

    logger.info(`GET /files/${req.params.year}/${req.params.department}`);

    let url = "http://bases.athle.com/asp.net/";
    url += "liste.aspx?frmpostback=true&frmbase=calendrier&frmmode=1&frmespace=0";
    url += `&frmsaison=${req.params.year}&frmtype1=&frmtype2=&frmtype3=&frmtype4=&frmniveau=&frmniveaulab=&frmligue=`;
    url += `&frmdepartement=${req.params.department}&frmepreuve=&frmdate_j1=&frmdate_m1=&frmdate_a1=&frmdate_j2=&frmdate_m2=&frmdate_a2=`;

    let fileName = "general_" + `y${req.params.year}d${req.params.department}_` + new Date().getTime() + ".html";

    try {

        const response = await get(url);
        await fs.writeFile("./src/main/resources/out/" + fileName, response.body);

    } catch (error) {
        logger.error(error.response.body);
    }

    res.send('File saved : ' + fileName);

});

async function get(url) {
    return got(url);
}

module.exports = router;