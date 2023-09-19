package com.anonymity.topictalks.utils.logger;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Utility class for logging exceptions with a specified logger.
 * @param <T> The class for which the logger is created.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils.logger
 * - Created At: 15-09-2023 00:04:06
 * @since 1.0 - version of class
 */

public class LoggerUtils<T> {

    private final Logger logger;

    /**
     * Constructs a LoggerUtils instance with a logger for the specified class.
     *
     * @param clazz The class for which the logger is created.
     */
    public LoggerUtils(Class<T> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Logs an exception's message, cause, and the exception itself at different log levels.
     *
     * @param ex The exception to be logged.
     */
    public void logException(Exception ex) {
        logger.info("Exception message: {}", ex.getMessage());
        logger.warn("Exception from: {}", ex.getCause());
        logger.error("Exception print: ", ex);
    }

}
