const { createLogger, format, transports } = require('winston');
const { combine, timestamp, label, printf } = format;

const myFormat = printf(info => {
    return `[${info.timestamp}] ${info.level.toUpperCase()} | ${info.message}`;
});

const logger = createLogger({
    format: combine(
        timestamp(),
        myFormat
    ),
    transports: [
        //
        // - Write to all logs with level `info` and below to `combined.log` 
        // - Write all logs error (and below) to `error.log`.
        //
        new transports.Console({ timestamp: true, level: 'debug' }),
        new transports.File({ filename: 'error.log', level: 'error', timestamp: true }),
        new transports.File({ filename: 'combined.log', level: 'debug', timestamp: true })
    ]
});


module.exports = function() {
    return logger;
}