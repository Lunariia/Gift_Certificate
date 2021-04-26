package com.epam.esm.api.validator;

import com.epam.esm.api.dto.TagDto;

import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

/**
 * Extension of Spring Validator interface for TagDto object
 */
public interface TagDtoValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return TagDto.class.equals(clazz);
    }
}
