package com.anonymity.topictalks.handlers;

import com.anonymity.topictalks.exceptions.*;
import com.anonymity.topictalks.models.payloads.responses.GlobalErrorResponse;
import com.anonymity.topictalks.models.payloads.responses.TokenErrorResponse;
import com.anonymity.topictalks.utils.logger.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * GlobalExceptionHandler is a controller advice class responsible for handling exceptions globally
 * and providing standardized error responses.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.handlers
 * - Created At: 14-09-2023 20:43:34
 * @since 1.0 - version of class
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoggerUtils<GlobalExceptionHandler> logger = new LoggerUtils<>(GlobalExceptionHandler.class);

    /**
     * Build an error response object with the given message and HTTP status.
     *
     * @param message The error message.
     * @param status  The HTTP status code.
     * @return A GlobalErrorResponse object containing error details.
     */
    private GlobalErrorResponse buildErrorResponse(String message, HttpStatus status) {
        return GlobalErrorResponse.builder()
                .error(status.getReasonPhrase())
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    /**
     * Handle TokenException and return a Forbidden response.
     *
     * @param ex      The TokenException that occurred.
     * @param request The web request.
     * @return A ResponseEntity with a TokenErrorResponse and HTTP status Forbidden.
     */
    @ExceptionHandler(value = TokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<TokenErrorResponse> handleRefreshTokenException(TokenException ex, WebRequest request){
        final TokenErrorResponse errorResponse = TokenErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Invalid Token")
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        logger.logException(ex);
        return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }

    /**
     * Handle MethodArgumentNotValidException and return a Bad Request response.
     *
     * @param ex The MethodArgumentNotValidException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Handle NoHandlerFoundException and return a Bad Request response.
     *
     * @param ex The NoHandlerFoundException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Bad Request.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleMessageException(NoHandlerFoundException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Handle AppException and return an Internal Server Error response.
     *
     * @param ex The AppException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Internal Server Error.
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalErrorResponse> handleAppException(AppException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Handle InvalidOldPasswordException and return a Bad Request response.
     *
     * @param ex The InvalidOldPasswordException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Bad Request.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalErrorResponse> handleBadRequestException(BadRequestException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Handle InvalidOldPasswordException and return a Bad Request response.
     *
     * @param ex The InvalidOldPasswordException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Bad Request.
     */
    @ExceptionHandler(InvalidOldPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalErrorResponse> handleInvalidOldPasswordException(InvalidOldPasswordException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Handle ResourceNotFoundException and return a Not Found response.
     *
     * @param ex The ResourceNotFoundException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<GlobalErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    /**
     * Handle HttpMessageNotReadableException and return a Bad Request response.
     *
     * @param ex The HttpMessageNotReadableException that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Bad Request.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("Malformed JSON request: " + ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Handle generic Exception and return an Internal Server Error response.
     *
     * @param ex The Exception that occurred.
     * @return A ResponseEntity with a GlobalErrorResponse and HTTP status Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalErrorResponse> handleGenericException(Exception ex) {
        logger.logException(ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("Internal Server Error: "+ ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
