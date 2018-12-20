const express = require('express');
const router = express.Router();
const logger = require('../../configuration/logger')();

const analysisService = require('../../../backend/service/scrap/analysisService');

const { SCRAP_DIR } = process.env;



router.get(`/analysis/:type`, async function(req, res) {

    logger.info(`GET /analysis/${req.params.type}`);

    try {

        await analysisService.analyseDir(SCRAP_DIR, req.params.type);

    } catch (err) {
        let errorMsg = `Error while reading directory : ${JSON.stringify(err)}`;
        logger.error(errorMsg);
        res.status(500).send(errorMsg);
        return;
    }

    // let fileName = `general_analysis_${new Date().getTime()}.html`;
    res.send('Analysis completed : ' + SCRAP_DIR);

});











module.exports = router;