package com.epam.esm.api.validator.impl;

import com.epam.esm.api.dto.TagDto;
import com.epam.esm.api.validator.TagDtoValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TagDtoValidatorImpl implements TagDtoValidator {

    private static final int MAX_TAG_NAME_LENGTH = 50;

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        TagDto tagDto = (TagDto) object;

        Long id = tagDto.getId();
        if (id != null) {
            errors.rejectValue(TagDto.Field.ID, "tag.dto.id.specified", "Specifying tag id is not allowed for all operations!");
        }

        String name = tagDto.getName();
        if (name == null) {
            errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.null", "Tag name required!");
        } else {
            if (name.length() > MAX_TAG_NAME_LENGTH) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.invalid", "Tag name should be not greater than 50 characters!");
            }
            if (name.trim().isEmpty()) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.blank", "Blank tag name is not allowed!");
            }
        }
    }
}
