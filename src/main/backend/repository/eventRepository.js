const db = require("./../../api/configuration/database/queryBuilder");
const logger = require('./../../api/configuration/logger')();

const townRepository = require('./townRepository');
const familyRepository = require('./familyRepository');
const leagueRepository = require('./leagueRepository');
const eventTypeRepository = require('./eventTypeRepository');
const eventLevelRepository = require('./eventLevelRepository');
const eventLabelRepository = require('./eventLabelRepository');


async function handleTownAndReturnId(object) {

    let objTown = {
        label: object.town,
        department: object.department
    }
    await townRepository.insert(objTown);
    let town = await townRepository.getByKey(objTown);

    return town.rows[0].id;

}

async function handleFamilyAndReturnId(object) {

    let objFamily = {
        label: object.family
    }
    await familyRepository.insert(objFamily);
    let family = await familyRepository.getByKey(objFamily);

    return family.rows[0].id;

}

async function handleLeagueAndReturnId(object) {

    let objLeague = {
        code: object.league
    }
    await leagueRepository.insert(objLeague);
    let league = await leagueRepository.getByKey(objLeague);
    return league.rows[0].code;

}

async function handleEventTypeAndReturnId(object) {

    if (object.event_type === null || object.event_type.trim() === "") {
        return null;
    }

    let objEventType = {
        label: object.event_type
    }
    await eventTypeRepository.insert(objEventType);
    let eventType = await eventTypeRepository.getByKey(objEventType);
    return eventType.rows[0].id;

}

async function handleLevelAndReturnId(object) {

    let objLevel = {
        label: object.level
    }
    await eventLevelRepository.insert(objLevel);
    let level = await eventLevelRepository.getByKey(objLevel);
    return level.rows[0].id;

}

async function handleEventLabelAndReturnId(object) {

    if (object.stamp !== null) {
        let objLabel = {
            label: object.stamp
        }
        await eventLabelRepository.insert(objLabel);
        let label = await eventLabelRepository.getByKey(objLabel);
        return label.rows[0].id;
    }
    return null;

}




async function upsert(object) {

    // TOWN
    object.town = await handleTownAndReturnId(object);

    // FAMILY
    object.family = await handleFamilyAndReturnId(object);

    // LEAGUE
    object.league = await handleLeagueAndReturnId(object);

    // EVENT_TYPE => handled in the details

    // LEVEL
    object.level = await handleLevelAndReturnId(object);

    // EVENT LABEL
    object.stamp = await handleEventLabelAndReturnId(object);


    try {
        await insert(object);
    } catch (ex) {
        logger.debug(`KO => eventRepository.upsert() : object => ${JSON.stringify(object)}`);
        logger.debug(`KO => eventRepository.upsert() : err => ${JSON.stringify(ex)}`);

        await update(object);
    }

}

async function insert(object) {
    let query = ` insert into event (id_js, date_event_begin, label, 
                                     date_creation, date_modification, date_event_end, 
                                     fk_id_town, fk_id_family, fk_code_league, 
                                     fk_id_event_level, fk_id_event_label ) 
                  values ($1, $2, $3, 
                          current_timestamp, current_timestamp, $4, 
                          $5, $6, $7, 
                          $8, $9) `;
    await db.queryBuilderPromise(query, [object.id_js, object.date_event.begin, object.label,
        object.date_event.end,
        object.town, object.family, object.league,
        object.level, object.stamp
    ]);
}

async function update(object) {
    let query = ` update event set  date_event_begin = $2, label = $3, date_modification = current_timestamp,
                                    date_event_end = $4, fk_id_town = $5, fk_id_family = $6, fk_code_league = $7, 
                                    fk_id_event_level = $8, fk_id_event_label = $9 where id_js = $1 `;
    await db.queryBuilderPromise(query, [object.id_js, object.date_event.begin, object.label, object.date_event.end,
        object.town, object.family, object.league, object.level, object.stamp
    ]);
}

module.exports = {
    upsert
}