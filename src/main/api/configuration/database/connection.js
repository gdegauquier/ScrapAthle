var pg = require('pg');

// create a config to configure both pooling behavior
// and client options
// note: all config is optional and the environment variables
// will be read if the config is not present
var pool = new pg.Pool(
    {
        idleTimeoutMillis: 300,
        connectionTimeoutMillis: 2000,
    });
module.exports = pool;