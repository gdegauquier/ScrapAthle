const express = require('express');
const router = express.Router();

const db = require("./../../configuration/database/queryBuilder");

const logger = require('./../../configuration/logger')();

const got = require('got');
const fs = require('fs').promises;

const URL_BASE = `http://bases.athle.com/asp.net/`;

router.get(`/data/:year`, async function(req, res) {

    logger.info(`GET /data/${req.params.year}`);

    let ret;

    try {
        ret = await db.queryBuilderPromise(` SELECT * FROM event where extract(year from date_event_begin) = $1 `, [req.params.year]);
    } catch (error) {
        logger.error(JSON.stringify(error));
        res.status(500).send('Impossible to retrieve Ids');
    }
    return res.send(ret.rows);
});

module.exports = router;