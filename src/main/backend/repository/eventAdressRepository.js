const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function deleteByEventId(eventId) {

    let query = ` delete from event_adress where fk_id_js_event = $1 `;

    try {
        await db.queryBuilderPromise(query, [eventId]);
    } catch (ex) {
        logger.debug(`KO => eventAdressRepository.deleteByEventId() : object => ${JSON.stringify(eventId)}`);
        logger.debug(`KO => eventAdressRepository.deleteByEventId() : err => ${JSON.stringify(ex)}`);
    }


}

async function insert(object) {

    let query = ` insert into event_adress (fk_id_js_event, postal_code, town,line_1,line_2,line_3 ) 
                  VALUES ( $1, $2, $3, $4, $5, $6 )  `;

    try {
        await db.queryBuilderPromise(query, [object.fk_id_js_event, object.postal_code, object.town, object.line_1, object.line_2, object.line_3]);
    } catch (ex) {
        logger.debug(`KO => eventAdressRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventAdressRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}





module.exports = {
    insert,
    deleteByEventId
}