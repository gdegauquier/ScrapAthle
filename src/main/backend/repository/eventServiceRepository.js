const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();


async function insertService(object) {

    let query = ` insert into event_service (label, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM event_service WHERE label = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.label]);
    } catch (ex) {
        logger.debug(`KO => eventServiceRepository.insertService() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventServiceRepository.insertService() : err => ${JSON.stringify(ex)}`);
    }

}


async function insertRel(object) {

    let query = ` insert into rel_event_service ( fk_id_js_event, fk_id_event_service, date_creation, date_modification  ) 
                  SELECT CAST( $1 AS VARCHAR), CAST( $2 AS INTEGER), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM rel_event_service WHERE fk_id_js_event = $1 AND fk_id_event_service = $2 ) `;

    try {
        await db.queryBuilderPromise(query, [object.fk_id_js_event, object.fk_id_event_service]);
    } catch (ex) {
        logger.debug(`KO => eventServiceRepository.insertRel() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventServiceRepository.insertRel() : err => ${JSON.stringify(ex)}`);
    }

}


async function getTypeByKey(object) {

    let query = ` select * from event_service where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}


async function deleteRelByEventId(object) {

    let query = ` delete from rel_event_service where fk_id_js_event = $1 `;
    await db.queryBuilderPromise(query, [object]);

}



module.exports = {
    deleteRelByEventId,
    insertService,
    insertRel,
    getTypeByKey
}