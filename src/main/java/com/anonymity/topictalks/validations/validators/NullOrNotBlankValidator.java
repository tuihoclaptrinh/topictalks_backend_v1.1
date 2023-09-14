package com.anonymity.topictalks.validations.validators;

import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

/**
 * The {@code NullOrNotBlankValidator} class is a custom constraint validator used to validate that a value
 * is either null, not blank (for strings), not zero (for numbers), or not null (for Instant objects).
 *
 * It implements the {@code ConstraintValidator<NullOrNotBlank, Object>} interface, allowing it to be used
 * with the {@code @NullOrNotBlank} custom constraint annotation.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.validations.validators
 * - Created At: 14-09-2023 19:46:15
 * @since 1.0 - version of class
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, Object> {

    /**
     * Initializes the validator with any custom parameters passed via the {@code NullOrNotBlank} annotation.
     * This method is called once during initialization.
     *
     * @param parameters The annotation parameters (not used in this validator).
     */
    @Override
    public void initialize(NullOrNotBlank parameters) {
        // No custom parameters or resource setup needed
    }

    /**
     * Validates the given value based on its data type. It returns {@code true} if the value is null or contains
     * meaningful data, depending on its data type. For example, for strings, it checks if the string is not blank;
     * for numbers, it checks if the number is not zero; for instants, it always returns true.
     *
     * @param value                        The value to be validated.
     * @param constraintValidatorContext   The context in which the constraint is evaluated.
     * @return                            {@code true} if the value is null or contains meaningful data,
     *                                    {@code false} otherwise.
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        // Check if the value is null; if so, consider it valid (return true)
        if (null == value) { // yoda condition
            return true;
        }

        // Check if the value is an instance of String and if it contains only whitespace
        if (value instanceof String strValue ) {
            if(strValue.matches("^\\s*$")) {
                return false;
            }
            return !strValue.isBlank();
        }

        // Check if the value is an instance of Number and if its double value is not equal to 0.0
        if (value instanceof Number numValue) {
            return numValue.doubleValue() != 0.0;
        }

        // Check if the value is an instance of Instant and if it is not null
        return value instanceof Instant instantValue;

        // Add more checks for other data types as needed

    }
}
