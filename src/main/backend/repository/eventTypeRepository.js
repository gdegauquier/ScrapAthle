const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();


async function insert(object) {

    let query = ` insert into event_type (label, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM event_type WHERE label = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.label]);
    } catch (ex) {
        logger.debug(`KO => eventTypeRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventTypeRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}


async function insertRel(object) {

    let query = ` insert into rel_event_type (fk_id_js_event, fk_id_event_type, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), $2, current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM rel_event_type WHERE fk_id_js_event = $1 and fk_id_event_type = $2 ) `;

    try {
        await db.queryBuilderPromise(query, [object.id_js, object.fk_id_event_type]);
    } catch (ex) {
        logger.debug(`KO => eventTypeRepository.insertRel() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventTypeRepository.insertRel() : err => ${JSON.stringify(ex)}`);
    }

}


async function getByKey(object) {

    let query = ` select * from event_type where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}


async function deleteByEventId(object) {

    let query = ` delete from rel_event_type where fk_id_js_event = $1 `;
    await db.queryBuilderPromise(query, [object.id_js]);

}



module.exports = {
    deleteByEventId,
    insert,
    insertRel,
    getByKey
}