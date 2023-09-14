package com.anonymity.topictalks.validations.validators;

import com.anonymity.topictalks.validations.annotations.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validation class for checking if a string represents a strong password.
 *
 * This class implements the {@link ConstraintValidator} interface for the custom
 * {@link StrongPassword} annotation. It validates if a string contains at least one digit,
 * one lowercase letter, one uppercase letter, one special character, and is at least
 * 8 characters long.
 *
 * @see StrongPassword
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.validations.validators
 * - Created At: 14-09-2023 20:27:19
 * @since 1.0 - version of class
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String > {

    /**
     * Validates if the input string represents a strong password.
     *
     * @param value   The input string to be validated.
     * @param context The context in which the validation is performed.
     * @return {@code true} if the string is a strong password, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // check if string contains at least one digit, one lowercase letter, one uppercase letter, one special character and 8 characters long
        return value.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$");
    }
}
