package com.anonymity.topictalks.validations.annotations;

import com.anonymity.topictalks.validations.validators.NullOrNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation for checking if a field is null or not blank (for Strings).
 *
 * This annotation can be applied to fields and parameters and is used in conjunction with the
 * {@link NullOrNotBlankValidator} class to perform the validation.
 *
 *
 * @see NullOrNotBlankValidator
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.annotations
 * - Created At: 14-09-2023 19:42:55
 * @since 1.0 - version of class
 */

@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {

    /**
     * The error message to be displayed when validation fails.
     *
     * @return The error message.
     */
    String message() default "This field can not be null or blank!";

    /**
     * Defines the validation groups to which this constraint belongs.
     *
     * @return The validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload object that can be used to carry additional information about the validation.
     *
     * @return The payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * A container for multiple {@link NullOrNotBlank} annotations.
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * An array of {@link NullOrNotBlank} annotations.
         *
         * @return The array of annotations.
         */
        NullOrNotBlank[] value();
    }
}
