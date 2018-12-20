const express = require('express');
const router = express.Router();

const db = require("../../configuration/database/queryBuilder");

const logger = require('../../configuration/logger')();


// get events for a given year 
router.get(`/events/:year`, async function(req, res) {

    logger.info(`GET /events/${req.params.year}`);

    //LIMIT {itemsPerPage} OFFSET {(page - 1) * itemsPerPage}

    /* 
    SELECT * FROM event 
    LIMIT 100 OFFSET ((page - 1) * 100) ;
    */


    let ret;
    let limit = 0;
    let offset = 10;

    try {
        ret = await db.queryBuilderPromise(` SELECT * FROM event where extract(year from date_event_begin) = $1 `, [req.params.year]);
    } catch (error) {
        logger.error(JSON.stringify(error));
        res.status(500).send('Impossible to retrieve Ids');
    }
    return res.send(ret.rows);
});

module.exports = router;