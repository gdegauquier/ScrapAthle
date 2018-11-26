"use strict";
const QueryStream = require('pg-query-stream');
const pg = require('pg');

// TODO : add current logger

const pool = new pg.Pool({
    user: process.env.PGUSER,
    database: process.env.PGDATABASE,
    password: process.env.PGPASSWORD,
    host: process.env.PGHOST,
    port: process.env.PGPORT,
    max: 18
});

pool.on('error', (err) => {
    // if an error is encountered by a client while it sits idle in the pool
    // the pool itself will emit an error event with both the error and
    // the client which emitted the original error
    // this is a rare occurrence but can happen if there is a network partition
    // between your application and the database, the database restarts, etc.
    // and so you might want to handle it and at least log it out

    console.log("pool error : " + err.message + ", " + err.stack);

});

const queryBuilder = (queryText, values, cb) => {
    pool.connect((err, client, done) => {
        if (err) {
            done();
            //logger.error(err);
            // logger.info(`Connection released`);
            cb(err, null);
        } else {
            client.query(queryText, values, (err, result) => {
                done();
                //    logger.info(`Connection released`);
                cb(err, result);
            });
        }
    });
};
const queryBuilderStream = (queryText, values) => {
    return new Promise((resolve, reject) => {
        pool.connect()
            .then(client => {
                const query = new QueryStream(queryText, values);
                const stream = client.query(query);
                client.release();
                //  logger.info(`Connection released`);
                resolve(stream);
            })
            .catch(error => {
                //logger.error(error);
                reject({ error: error, code: 500 });
            });
    });
};

const queryBuilderPromise = (queryText, values) => {
    return new Promise((resolve, reject) => {
        pool.connect()
            .then(client => {
                return client.query(queryText, values)
                    .then(result => {
                        client.release();
                        //    logger.info(`Connection released`);
                        resolve(result);
                    })
                    .catch(err => {
                        client.release();
                        //    logger.info(`Connection released`);
                        //logger.error(err.stack);
                        reject({ error: err, code: 401 });
                    });
            })
            .catch(error => {
                //logger.error(error);
                reject({ error: error, code: 500 });
            });
    });
};

module.exports = {
    queryBuilder: queryBuilder,
    queryBuilderPromise: queryBuilderPromise,
    queryBuilderStream: queryBuilderStream
}