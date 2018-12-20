var express = require('express');
var app = express();

require('dotenv').config();
const logger = require('./src/main/api/configuration/logger')();


const {
    API_PORT,
    VERSION
} = process.env;


// _ [underscore] means it's a route of the system (won't be used after cron is set up)
app.use(`/${VERSION}/_scraps`, require('./src/main/api/controller/scrap/filesController'));
app.use(`/${VERSION}/_scraps`, require('./src/main/api/controller/scrap/analysisController'));


app.use(`/${VERSION}/`, require('./src/main/api/controller/event/eventsController'));



app.listen(`${API_PORT}`, () => {
    logger.info("API ScrapAthle started on port : " + process.env.API_PORT);
});