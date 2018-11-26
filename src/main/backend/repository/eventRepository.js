const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

const townRepository = require('./townRepository');
const familyRepository = require('./familyRepository');


async function upsert(object) {

    // TOWN
    let objTown = {
        label: object.town,
        department: object.department
    }
    await townRepository.insert(objTown);

    // GET TOWN ID / SET ID
    let town = await townRepository.getByKey(objTown);
    object.town = town.rows[0].id;

    // FAMILY
    let objFamily = {
        label: object.family
    }
    await familyRepository.insert(objFamily);
    let family = await familyRepository.getByKey(objFamily);
    object.family = family.rows[0].id;

    try {
        await insert(object);
    } catch (ex) {
        logger.debug(`KO => eventRepository.upsert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventRepository.upsert() : err => ${JSON.stringify(ex)}`);

        await update(object);
    }

}

async function insert(object) {
    let query = ` insert into event (id_js, date_event_begin, label, date_creation, date_modification, date_event_end, fk_id_town, fk_id_family) 
                  values ($1, $2, $3, current_timestamp, current_timestamp, $4, $5, $6) `;
    await db.queryBuilderPromise(query, [object.id_js, object.date_event.begin, object.label, object.date_event.end, object.town, object.family]);
}

async function update(object) {
    let query = ` update event set date_event_begin = $2, label = $3, date_modification = current_timestamp, date_event_end = $4, fk_id_town = $5, fk_id_family = $6 where id_js = $1 `;
    await db.queryBuilderPromise(query, [object.id_js, object.date_event.begin, object.label, object.date_event.end, object.town, object.family]);
}

module.exports = {
    upsert
}