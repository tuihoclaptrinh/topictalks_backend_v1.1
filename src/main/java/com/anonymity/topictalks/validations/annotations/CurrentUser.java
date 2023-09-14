package com.anonymity.topictalks.validations.annotations;

import jakarta.validation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Custom annotation used to access the currently authenticated user in Spring Security.
 *
 * This annotation can be applied to method parameters or class-level elements to inject the currently
 * authenticated user principal into the annotated parameter or field. It is typically used in Spring
 * Security-enabled applications to access user information.
 *
 * @see org.springframework.security.core.annotation.AuthenticationPrincipal
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.validations.annotations
 * - Created At: 14-09-2023 20:39:59
 * @since 1.0 - version of class
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
