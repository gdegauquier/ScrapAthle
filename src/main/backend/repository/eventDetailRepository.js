const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();
const familyRepository = require('./familyRepository');
const eventTypeRepository = require('./eventTypeRepository');
const eventContactRepository = require('./eventContactRepository');

const eventLevelRepository = require('./eventLevelRepository');
const eventServiceRepository = require('./eventServiceRepository');


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



async function handleServices(object) {

    if (object.services === null || object.services.length === 0) {
        return;
    }

    await eventServiceRepository.deleteRelByEventId(object.id_js);

    for (let service of object.services) {

        if (service === null) {
            continue;
        }

        let objService = {
            label: service
        }

        // insert type
        await eventServiceRepository.insertService(objService);

        // get key
        let serviceKey = await eventServiceRepository.getTypeByKey(objService);
        serviceKey = serviceKey.rows[0].id;

        // insert rel 
        objService.fk_id_event_service = serviceKey;
        objService.fk_id_js_event = object.id_js;
        await eventServiceRepository.insertRel(objService);

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

    if (object.family === null) {
        return null;
    }

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

    if (object.level === null) {
        return null;
    }

    let objLevel = {
        label: object.level
    }
    await eventLevelRepository.insert(objLevel);
    let level = await eventLevelRepository.getByKey(objLevel);
    return level.rows[0].id;

}

async function handleDetails(object) {

    await deleteByEventId(object.id_js);

    for (let event of object.events) {

        let eventToAdd = {
            fk_id_js_event: object.id_js,
            date_event_string: event.date,
            hour_event_string: event.hour,
            distance: event.distance,
            label: event.label
        }

        await insert(eventToAdd);

    }

}

async function insert(object) {

    let query = ` insert into event_detail ( fk_id_js_event, date_event_string, hour_event_string, distance, label, date_creation, date_modification ) 
                                    SELECT  CAST($1 as varchar), CAST($2 as varchar),CAST( $3 as varchar), CAST($4 as varchar), CAST($5 as varchar), 
                                            current_timestamp, current_timestamp 
                                    WHERE NOT EXISTS ( SELECT 1 FROM event_detail WHERE FK_ID_JS_EVENT = $1 AND LABEL = $5) `;
    await db.queryBuilderPromise(query, [object.fk_id_js_event, object.date_event_string, object.hour_event_string, object.distance, object.label]);
}

async function deleteByEventId(key) {

    let query = ` delete from event_detail where fk_id_js_event = $1 `;
    await db.queryBuilderPromise(query, [key]);

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

    // ADD SERVICES in a foreign table
    await handleServices(object);

    // ADD DETAILS in a foreign table 
    await handleDetails(object);

    let query = ` update event set  date_modification = current_timestamp, code = $2, fk_id_family = $3, 
                                    date_event_begin = $4, date_event_end = $5, stadium = $6, fk_id_event_level = $7,
                                    website = $8, conditions = $9, other_information = $10, technical_security_advice = $11, 
                                    online_engagement = $12
                                    where id_js = $1 `;

    await db.queryBuilderPromise(query, [object.id_js,
        object.code, object.family,
        object.date_event.begin, object.date_event.end, object.stadium, object.level,
        object.website, object.conditions, object.other_information, object.technical_security_advice,
        object.online_engagement
    ]);

}

module.exports = {
    upsert
}