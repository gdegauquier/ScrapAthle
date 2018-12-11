function extractDatesFromString(string, year) {

    if (string.indexOf("/") === -1) {
        logger.error(`KO => BaseAthleDateUtils.extractDateFromString()`);
        throw BaseAthleDateUtils.extractDateFromString();
    }

    let dateToExtract = string.split("/");

    let dayB = dateToExtract[0].indexOf("-") > -1 ? dateToExtract[0].split("-")[0] : dateToExtract[0];
    let dayE = dateToExtract[0].indexOf("-") > -1 ? dateToExtract[0].split("-")[1] : dateToExtract[0];

    return {
        begin: year + '-' + dateToExtract[1] + '-' + dayB,
        end: year + '-' + dateToExtract[1] + '-' + dayE
    }

}

function formatDateForDB(string) {
    let parsed = string.split("/");
    return parsed[2] + "-" + parsed[1] + "-" + parsed[0];
}



module.exports = {
    extractDatesFromString,
    formatDateForDB
}