const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function insert(object) {

    let query = ` insert into event_detail_category ( code, date_creation, date_modification )
                  SELECT CAST($1 as VARCHAR), current_timestamp, current_timestamp 
                  WHERE NOT EXISTS ( SELECT 1 FROM event_detail_category WHERE code = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.code]);
    } catch (ex) {
        logger.debug(`KO => eventDetailCategoryRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventDetailCategoryRepository.insert() : err => ${JSON.stringify(ex)}`);
    }


}

async function insertRel(object) {

    let query = ` insert into rel_event_detail_category (fk_code_event_detail_category, fk_id_event_detail, fk_id_js_event, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), CAST($2 AS integer), CAST($3 AS varchar), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 
                                     FROM rel_event_detail_category 
                                     WHERE fk_code_event_detail_category = $1 and fk_id_js_event = $3 and fk_id_event_detail = $2 ) `;

    try {
        await db.queryBuilderPromise(query, [object.fk_code_event_detail_category, object.fk_id_event_detail, object.fk_id_js_event]);
    } catch (ex) {
        logger.debug(`KO => eventDetailCategoryRepository.insertRel() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventDetailCategoryRepository.insertRel() : err => ${JSON.stringify(ex)}`);
    }

}


async function getByKey(eventId, eventDetailId) {

    let query = ` select * from event_detail_type where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}



module.exports = {
    insert,
    insertRel,
    getByKey
}