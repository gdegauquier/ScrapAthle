const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function insert(object) {

    let query = ` insert into family (label, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM family WHERE label = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.label]);
    } catch (ex) {
        logger.debug(`KO => familyRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => familyRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}

async function getByKey(object) {

    let query = ` select * from family where label = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.label]);
    return ret;

}



module.exports = {
    insert,
    getByKey
}