package ru.renattele.admin95.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import ru.renattele.admin95.util.RegexConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a string matches the name regex pattern defined in RegexConstants.
 */
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = RegexConstants.NAME_REGEX)
public @interface NameRegexPattern {
    
    /**
     * Error message to be used when validation fails.
     */
    String message() default "Invalid name format";
    
    /**
     * Validation groups, allowing validation to be applied only if 
     * the current validation belongs to one of these groups.
     */
    Class<?>[] groups() default {};
    
    /**
     * Payloads, which can be used to carry metadata information consumed by 
     * validation clients.
     */
    Class<? extends Payload>[] payload() default {};
}