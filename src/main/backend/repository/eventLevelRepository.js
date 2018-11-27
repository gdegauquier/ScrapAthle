const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function insert(object) {

    let query = ` insert into event_level (label, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM event_level WHERE label = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.label]);
    } catch (ex) {
        logger.debug(`KO => eventLevelRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventLevelRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}

async function getByKey(object) {

    let query = ` select * from event_level where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}



module.exports = {
    insert,
    getByKey
}