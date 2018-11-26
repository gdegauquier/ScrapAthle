const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function insert(object) {

    let query = ` insert into town (label, fk_code_department, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), CAST($2 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM TOWN WHERE label = $1 AND fk_code_department = $2) `;

    try {
        await db.queryBuilderPromise(query, [object.label, object.department]);
    } catch (ex) {
        logger.debug(`KO => eventRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}

async function getByKey(object) {

    let query = ` select * from town where label = $1 and fk_code_department = $2 `;
    let ret = await db.queryBuilderPromise(query, [object.label, object.department]);
    return ret;

}



module.exports = {
    insert,
    getByKey
}