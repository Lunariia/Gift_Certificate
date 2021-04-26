package com.epam.esm.api.validator;

import com.epam.esm.api.dto.CertificateDto;
import org.springframework.lang.NonNull;
import org.springframework.validation.Validator;

public interface CertificateDtoUpdateValidator extends Validator {

    @Override
    default boolean supports(@NonNull Class<?> clazz) {
        return CertificateDto.class.equals(clazz);
    }
}
