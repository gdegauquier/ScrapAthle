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

async function getByKey(object) {

    let query = ` select * from event_type where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}



module.exports = {
    insert,
    getByKey
}