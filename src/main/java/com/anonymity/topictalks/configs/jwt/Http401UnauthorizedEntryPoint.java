package com.anonymity.topictalks.configs.jwt;

import com.anonymity.topictalks.models.payloads.responses.TokenErrorResponse;
import com.anonymity.topictalks.utils.logger.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Custom HTTP 401 Unauthorized Entry Point to handle authentication exceptions.
 * This entry point logs the exception, sets the HTTP response status to 401 Unauthorized,
 * and sends a JSON response containing error details.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs.jwt
 * - Created At: 15-09-2023 13:21:34
 * @since 1.0 - version of class
 */

@Component
@Slf4j
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final LoggerUtils<Http401UnauthorizedEntryPoint> logger = new LoggerUtils<>(Http401UnauthorizedEntryPoint.class);

    /**
     * Commences an authentication exception by logging it, setting the response status to 401 Unauthorized,
     * and sending a JSON response with error details.
     *
     * @param request       The HTTP request that caused the authentication exception.
     * @param response      The HTTP response to be modified.
     * @param authException The authentication exception that occurred.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        logger.logException(authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        TokenErrorResponse body = TokenErrorResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .timestamp(LocalDateTime.now())
                .message(authException.getMessage())
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
