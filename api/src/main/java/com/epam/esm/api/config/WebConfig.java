package com.epam.esm.api.config;

import com.epam.esm.api.converter.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm.api")
@Profile("dev")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = objectMapper();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = mappingJackson2HttpMessageConverter();
        converters.add(messageConverter);
    }

    @Bean
    public ModelMapper modelMapper(CertificateDtoToEntityConverter certificateDtoToEntityConverter,
                                   CertificateEntityToDtoConverter certificateEntityToDtoConverter,
                                   FilterRequestDtoToModelConverter filterRequestDtoToModelConverter,
                                   SortRequestDtoToModelConverter sortRequestDtoToModelConverter,
                                   TagDtoToEntityConverter tagDtoToEntityConverter,
                                   TagEntityToDtoConverter tagEntityToDtoConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.addConverter(certificateDtoToEntityConverter);
        modelMapper.addConverter(certificateEntityToDtoConverter);
        modelMapper.addConverter(filterRequestDtoToModelConverter);
        modelMapper.addConverter(sortRequestDtoToModelConverter);
        modelMapper.addConverter(tagDtoToEntityConverter);
        modelMapper.addConverter(tagEntityToDtoConverter);
        return modelMapper;
    }

}
