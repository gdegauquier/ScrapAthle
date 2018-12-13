const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();


async function insertType(object) {

    let query = ` insert into contact_type (label, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM contact_type WHERE label = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.label]);
    } catch (ex) {
        logger.debug(`KO => eventContactRepository.insertType() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventContactRepository.insertType() : err => ${JSON.stringify(ex)}`);
    }

}


async function insertRel(object) {

    let query = ` insert into rel_event_contact ( name, email, fk_id_contact_type, date_creation, date_modification, fk_id_js_event ) 
                  SELECT CAST($1 as varchar), CAST($2 as varchar), CAST( $3 as integer), current_timestamp, current_timestamp, CAST ( $4 as varchar )
                  WHERE NOT EXISTS ( SELECT 1 FROM rel_event_contact WHERE (name = $1 OR email = $2) AND fk_id_js_event = $4 ) `;

    try {
        await db.queryBuilderPromise(query, [object.name, object.email, object.fk_id_contact_type, object.fk_id_js_event]);
    } catch (ex) {
        logger.debug(`KO => eventContactRepository.insertRel() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventContactRepository.insertRel() : err => ${JSON.stringify(ex)}`);
    }

}


async function getTypeByKey(object) {

    let query = ` select * from contact_type where label = $1`;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}


async function deleteRelByEventId(object) {

    let query = ` delete from rel_event_contact where fk_id_js_event = $1 `;
    await db.queryBuilderPromise(query, [object]);

}



module.exports = {
    deleteRelByEventId,
    insertType,
    insertRel,
    getTypeByKey
}