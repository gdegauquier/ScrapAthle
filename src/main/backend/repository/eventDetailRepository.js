const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();
const familyRepository = require('./familyRepository');
const eventTypeRepository = require('./eventTypeRepository');
const eventContactRepository = require('./eventContactRepository');

const eventLevelRepository = require('./eventLevelRepository');

const baseAthleContactUtils = require("./../utils/BaseAthleContactUtils");



async function handleContacts(object) {


    if (object.contacts === null || object.contacts.length === 0) {
        return;
    }

    await eventContactRepository.deleteRelByEventId(object.id_js);

    for (let contact of object.contacts) {

        if (contact.email === null &&
            !baseAthleContactUtils.isNameValid(contact.name)) {
            continue;
        }

        let objType = {
            label: contact.type,
        }

        // insert type
        await eventContactRepository.insertType(objType);

        // get key
        let typeKey = await eventContactRepository.getTypeByKey(objType);
        typeKey = typeKey.rows[0].id;

        // insert rel 
        contact.fk_id_contact_type = typeKey;
        contact.fk_id_js_event = object.id_js;
        await eventContactRepository.insertRel(contact);

    }

}

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

async function handleLevelAndReturnId(object) {

    let objLevel = {
        label: object.level
    }
    await eventLevelRepository.insert(objLevel);
    let level = await eventLevelRepository.getByKey(objLevel);
    return level.rows[0].id;

}

async function update(object) {

    // FAMILY
    object.family = await handleFamilyAndReturnId(object);

    // LEVEL
    object.level = await handleLevelAndReturnId(object);

    // ADD EVENT_TYPES in a foreign table 
    await handleEventTypes(object);

    // ADD CONTACTS in a foreign table
    await handleContacts(object);

    let query = ` update event set  date_modification = current_timestamp, code = $2, fk_id_family = $3, 
                                    date_event_begin = $4, date_event_end = $5, stadium = $6, fk_id_event_level = $7,
                                    website = $8, conditions = $9, other_information = $10, technical_security_advice = $11
                                    where id_js = $1 `;

    await db.queryBuilderPromise(query, [object.id_js,
        object.code, object.family,
        object.date_event.begin, object.date_event.end, object.stadium, object.level,
        object.website, object.conditions, object.other_information, object.technical_security_advice
    ]);

}

module.exports = {
    upsert
}