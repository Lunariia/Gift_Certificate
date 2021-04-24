package com.epam.esm.service.validator;

/**
 * Predicate validator interface
 *
 * @param <T> is object type
 */
public interface Validator<T> {
    /**
     * Validate object and return true if valid, otherwise return false
     *
     * @param object to validate
     * @return true - valid, false - not valid
     */
    boolean isValid(T object);
}
