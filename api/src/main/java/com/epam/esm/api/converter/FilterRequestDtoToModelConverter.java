package com.epam.esm.api.converter;

import com.epam.esm.api.dto.FilterRequestDto;
import com.epam.esm.model.FilterRequest;
import org.modelmapper.AbstractConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterRequestDtoToModelConverter extends AbstractConverter<FilterRequestDto, FilterRequest> implements Converter<FilterRequestDto, FilterRequest> {
    @Override
    public FilterRequest convert(FilterRequestDto filterRequestDto) {
        String search = filterRequestDto.getSearch();
        String tagName = filterRequestDto.getTag();
        return new FilterRequest(search, tagName);
    }

}
