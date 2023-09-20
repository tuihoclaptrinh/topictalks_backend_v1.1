package com.anonymity.topictalks.handlers;

import com.anonymity.topictalks.models.payloads.responses.TokenErrorResponse;
import com.anonymity.topictalks.utils.logger.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Custom Access Denied Handler to handle access denied exceptions.
 * This handler logs the exception, sets the HTTP response status to 403 Forbidden,
 * and sends a JSON response containing error details.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.handlers
 * - Created At: 15-09-2023 13:09:38
 * @since 1.0 - version of class
 */

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final LoggerUtils<CustomAccessDeniedHandler> logger = new LoggerUtils<>(CustomAccessDeniedHandler.class);

    /**
     * Handles access denied exception by logging it, setting the response status to 403 Forbidden,
     * and sending a JSON response with error details.
     *
     * @param request               The HTTP request that caused the access denied exception.
     * @param response              The HTTP response to be modified.
     * @param accessDeniedException The access denied exception that occurred.
     * @throws IOException          If an I/O error occurs while writing the response.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        logger.logException(accessDeniedException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        TokenErrorResponse body = TokenErrorResponse.builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error("Forbidden")
                .timestamp(LocalDateTime.now())
                .message(accessDeniedException.getMessage())
                .path(request.getServletPath())
                .build();

        final ObjectMapper mapper = new ObjectMapper();
        // register the JavaTimeModule, which enables Jackson to support Java 8 and higher date and time types
        mapper.registerModule(new JavaTimeModule());
        // ask Jackson to serialize dates as strings in the ISO 8601 format
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        mapper.writeValue(response.getOutputStream(), body);
    }

}
