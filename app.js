var express = require('express');
var app = express();

require('dotenv').config();
const logger = require('./src/main/api/configuration/logger')();


const {
    API_PORT,
    VERSION
} = process.env;


app.use(`/${VERSION}/scraps`, require('./src/main/api/controller/scrap/files'));
app.use(`/${VERSION}/scraps`, require('./src/main/api/controller/scrap/analysis'));
app.use(`/${VERSION}/scraps`, require('./src/main/api/controller/scrap/data'));


app.listen(`${API_PORT}`, () => {
    logger.info("API ScrapAthle started on port : " + process.env.API_PORT);
});