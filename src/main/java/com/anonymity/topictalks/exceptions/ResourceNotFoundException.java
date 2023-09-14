package com.anonymity.topictalks.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class representing a resource not found error.
 *
 * This exception is typically used to indicate that a specific resource with the provided field value does not exist
 * in the system. It extends the {@link RuntimeException} class, allowing it to be used without being explicitly caught
 * or declared.
 *
 * @see java.io.Serial
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 14-09-2023 20:37:37
 * @since 1.0 - version of class
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Constructs a new instance of the {@code ResourceNotFoundException} class with the specified resource name, field
     * name, and field value.
     *
     * @param resourceName The name of the resource.
     * @param fieldName    The name of the field.
     * @param fieldValue   The value of the field.
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
