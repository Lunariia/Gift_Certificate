package com.epam.esm.api.converter;

import com.epam.esm.api.dto.SortRequestDto;
import com.epam.esm.model.Sort;
import com.epam.esm.model.SortRequest;
import org.modelmapper.AbstractConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SortRequestDtoToModelConverter extends AbstractConverter<SortRequestDto, SortRequest> implements Converter<SortRequestDto, SortRequest> {

    private static final Sort.SortOrder DEFAULT_SORT_DIRECTION = Sort.SortOrder.ASC;

    private static final String SPLITERATOR = ",";
    private static final int LENGTH_SORT_FIELD_ONLY = 1;
    private static final int INDEX_SORT_FIELD = 0;
    private static final int INDEX_SORT_DIRECTION = 1;

    @Override
    public SortRequest convert(SortRequestDto sortRequestDto) {
        String sortDto = sortRequestDto.getSort();
        Sort sort = sortDto == null
                ? null
                : this.convert(sortDto);
        String thenSortDto = sortRequestDto.getThenSort();
        Sort thenSort = thenSortDto == null
                ? null
                : this.convert(thenSortDto);
        return new SortRequest(sort, thenSort);
    }

    private Sort convert(String sortDto) {
        String[] parameters = sortDto.split(SPLITERATOR);
        String field = parameters[INDEX_SORT_FIELD];
        if (parameters.length == LENGTH_SORT_FIELD_ONLY) {
            return new Sort(field, DEFAULT_SORT_DIRECTION);
        }
        String directionValue = parameters[INDEX_SORT_DIRECTION];
        String upperCaseDirectionValue = directionValue.toUpperCase();
        Sort.SortOrder direction = Sort.SortOrder.valueOf(upperCaseDirectionValue);
        return new Sort(field, direction);
    }
}
