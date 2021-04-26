package com.epam.esm.service.validator.sortRequest;

import com.epam.esm.model.Sort;
import com.epam.esm.model.SortRequest;
import com.epam.esm.persistence.entity.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateSortRequestValidatorImpl implements CertificateSortRequestValidator {

    @Override
    public boolean isValid(SortRequest sortRequest) {
        if (sortRequest == null) {
            return false;
        }
        Sort sort = sortRequest.getSort();
        Sort thenSort = sortRequest.getThenSort();
        return this.isValid(sort) && this.isValid(thenSort);
    }

    private boolean isValid(Sort sort) {
        if (sort == null) {
            return true;
        }
        String field = sort.getField();
        Sort.SortOrder direction = sort.getDirection();
        return this.isValid(field) && this.isValid(direction);
    }

    private boolean isValid(String sortField) {
        return Certificate.Field.NAME.equals(sortField)
                || Certificate.Field.DESCRIPTION.equals(sortField)
                || Certificate.Field.PRICE.equals(sortField)
                || Certificate.Field.DURATION.equals(sortField)
                || Certificate.Field.CREATE_DATE.equals(sortField)
                || Certificate.Field.LAST_UPDATE_DATE.equals(sortField);
    }

    private boolean isValid(Sort.SortOrder direction) {
        return direction != null;
    }
}
