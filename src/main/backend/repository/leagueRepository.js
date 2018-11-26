const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

async function insert(object) {

    let query = ` insert into league (code, date_creation, date_modification) 
                  SELECT CAST($1 AS VARCHAR), current_timestamp, current_timestamp
                  WHERE NOT EXISTS ( SELECT 1 FROM league WHERE code = $1 ) `;

    try {
        await db.queryBuilderPromise(query, [object.code]);
    } catch (ex) {
        logger.debug(`KO => leagueRepository.insert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => leagueRepository.insert() : err => ${JSON.stringify(ex)}`);
    }

}

async function getByKey(object) {

    let query = ` select * from league where code = $1 `;
    let ret = await db.queryBuilderPromise(query, [object.code]);
    return ret;

}



module.exports = {
    insert,
    getByKey
}