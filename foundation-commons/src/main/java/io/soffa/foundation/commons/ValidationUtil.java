package io.soffa.foundation.commons;

import io.soffa.foundation.errors.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public final class ValidationUtil {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();

    private ValidationUtil() {
    }

    public static <T> void validate(T input) {
        Validator validator = FACTORY.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(input);
        if (violations.isEmpty()) {
            return;
        }
        ConstraintViolation<T> c = violations.iterator().next();
        String prop = c.getPropertyPath().toString();
        throw new ValidationException(prop, prop + " " + c.getMessage());
    }
}
