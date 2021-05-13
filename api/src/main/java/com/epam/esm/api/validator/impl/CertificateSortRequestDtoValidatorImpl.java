package com.epam.esm.api.validator.impl;

import com.epam.esm.api.dto.CertificateDto;
import com.epam.esm.api.dto.SortRequestDto;
import com.epam.esm.api.validator.CertificateSortRequestDtoValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CertificateSortRequestDtoValidatorImpl implements CertificateSortRequestDtoValidator {

    private static final String REGEX_SORT = "([A-Za-z]+)(?i)(,asc|,desc)*";
    private static final Pattern PATTERN_SORT = Pattern.compile(REGEX_SORT);

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        SortRequestDto sortRequestDto = (SortRequestDto) object;

        String sort = sortRequestDto.getSort();
        if (sort != null) {
            Matcher matcher = PATTERN_SORT.matcher(sort);
            if (!matcher.matches()) {
                errors.rejectValue("sort", "sort.request.dto.sort.invalid", "Invalid primary sort parameter!");
            } else {
                String field = matcher.group(1);
                if (!this.hasSortMapping(field)) {
                    errors.rejectValue("sort", "sort.request.dto.sort.field.wrong", "Wrong field name in primary sort parameter!");
                }
            }
        }

        String thenSort = sortRequestDto.getThenSort();
        if (thenSort != null) {
            Matcher matcher = PATTERN_SORT.matcher(thenSort);
            if (!matcher.matches()) {
                errors.rejectValue("thenSort", "sort.request.dto.then.sort.invalid", "Invalid secondary sort (thenSort) parameter!");
            } else {
                String field = matcher.group(1);
                if (!this.hasSortMapping(field)) {
                    errors.rejectValue("thenSort", "sort.request.dto.then.sort.field.wrong", "Wrong field name in secondary sort (thenSort) parameter!");
                }
            }
        }
    }

    private boolean hasSortMapping(String field) {
        return CertificateDto.Field.NAME.equals(field)
                || CertificateDto.Field.DESCRIPTION.equals(field)
                || CertificateDto.Field.PRICE.equals(field)
                || CertificateDto.Field.DURATION.equals(field)
                || CertificateDto.Field.CREATE_DATE.equals(field)
                || CertificateDto.Field.LAST_UPDATE_DATE.equals(field);
    }
}
