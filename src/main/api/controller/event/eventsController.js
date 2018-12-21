const express = require('express');
const router = express.Router();

const db = require("../../configuration/database/queryBuilder");

const logger = require('../../configuration/logger')();


const { RECORDSET_MAX_RESULTS } = process.env;

// get events for a given year 
router.get(`/events/:year`, async function(req, res) {

    let page = 1;
    if (req.query.page) {
        page = req.query.page;
    }

    logger.info(`GET /events/${req.params.year}`);

    //LIMIT {itemsPerPage} OFFSET {(page - 1) * itemsPerPage}

    /* 
    SELECT * FROM event 
    LIMIT 100 OFFSET ((page - 1) * 100) ;
    */


    let ret;
    let limit = 0;
    let offset = 10;

    let query = ` SELECT * 
                                             FROM event 
                                             where extract(year from date_event_begin) = $1 
                                             LIMIT ${RECORDSET_MAX_RESULTS} OFFSET (( ${page} - 1 ) * ${RECORDSET_MAX_RESULTS} ) `;
    logger.debug(query);

    try {

        ret = await db.queryBuilderPromise(query, [req.params.year]);


    } catch (error) {
        logger.error(JSON.stringify(error));
        res.status(500).send('Impossible to retrieve Ids');
    }
    return res.send({ rows: ret.rows, rowCount: ret.rowCount });
});

module.exports = router;