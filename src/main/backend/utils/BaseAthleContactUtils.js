function isNameValid(string) {

    if (string === null) {
        return false;
    }

    if (string.indexOf("... ") > -1) {
        return false;
    }

    return true;

}



module.exports = {
    isNameValid
}