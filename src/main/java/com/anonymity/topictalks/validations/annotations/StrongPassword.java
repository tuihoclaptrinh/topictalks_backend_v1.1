package com.anonymity.topictalks.validations.annotations;

import com.anonymity.topictalks.validations.validators.StrongPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation for validating strong passwords.
 *
 * This annotation is used to mark fields or methods that should be validated as strong passwords.
 * It enforces that the annotated element must be a string representing a strong password, which
 * should contain at least one digit, one lowercase letter, one uppercase letter, one special
 * character, and be at least 8 characters long.
 *
 * @see StrongPasswordValidator
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.validations.annotations
 * - Created At: 14-09-2023 20:27:07
 * @since 1.0 - version of class
 */
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {

    /**
     * Default error message to be used when the validation fails.
     *
     * @return The default error message.
     */
    String message() default "Must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.";

    /**
     * Default groups for validation purposes.
     *
     * @return An array of default validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Default payload classes for validation purposes.
     *
     * @return An array of default payload classes.
     */
    Class<? extends Payload>[] payload() default {};

}
