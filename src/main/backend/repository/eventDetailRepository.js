const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();
const familyRepository = require('./familyRepository');
const eventTypeRepository = require('./eventTypeRepository');


async function handleEventTypes(object) {

    if (object.event_types === null || object.event_types.length === 0) {
        return;
    }

    // delete types from id_js key
    await eventTypeRepository.deleteByEventId(object);

    for (let eventType of object.event_types) {

        // insert event
        let objEventType = {
            label: eventType
        }
        await eventTypeRepository.insert(objEventType);
        // get key
        let eventTypeKey = await eventTypeRepository.getByKey(objEventType);
        eventTypeKey = eventTypeKey.rows[0].id;

        // insert into table rel_event_type
        let objRelEventType = {
            id_js: object.id_js,
            fk_id_event_type: eventTypeKey
        }
        await eventTypeRepository.insertRel(objRelEventType);

    }

}

async function handleFamilyAndReturnId(object) {

    let objFamily = {
        label: object.family
    }
    await familyRepository.insert(objFamily);
    let family = await familyRepository.getByKey(objFamily);

    return family.rows[0].id;

}

async function upsert(object) {

    try {
        await update(object);
    } catch (ex) {
        logger.debug(`KO => eventDetailRepository.upsert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventDetailRepository.upsert() : err => ${JSON.stringify(ex)}`);
    }

}

async function update(object) {

    // FAMILY
    object.family = await handleFamilyAndReturnId(object);

    // ADD EVENT_TYPES in a foreign table 
    await handleEventTypes(object);

    let query = ` update event set  date_modification = current_timestamp, code = $2, fk_id_family = $3, 
                                    date_event_begin = $4, date_event_end = $5 
                                    where id_js = $1 `;

    await db.queryBuilderPromise(query, [object.id_js,
        object.code, object.family,
        object.date_event.begin, object.date_event.end
    ]);

}

module.exports = {
    upsert
}