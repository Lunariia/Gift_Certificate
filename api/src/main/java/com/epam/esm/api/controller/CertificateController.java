package com.epam.esm.api.controller;

import com.epam.esm.api.dto.CertificateDto;
import com.epam.esm.api.dto.FilterRequestDto;
import com.epam.esm.api.dto.SortRequestDto;
import com.epam.esm.api.validator.CertificateDtoCreateValidator;
import com.epam.esm.api.validator.CertificateDtoUpdateValidator;
import com.epam.esm.api.validator.CertificateSortRequestDtoValidator;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.FilterRequest;
import com.epam.esm.model.SortRequest;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.logic.CertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateDtoUpdateValidator certificateDtoUpdateValidator;
    private final CertificateDtoCreateValidator certificateDtoCreateValidator;
    private final CertificateSortRequestDtoValidator certificateSortRequestDtoValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoUpdateValidator certificateDtoUpdateValidator,
                                 CertificateDtoCreateValidator certificateDtoCreateValidator,
                                 CertificateSortRequestDtoValidator certificateSortRequestDtoValidator,
                                 ModelMapper modelMapper) {
        this.certificateService = certificateService;
        this.certificateDtoUpdateValidator = certificateDtoUpdateValidator;
        this.certificateDtoCreateValidator = certificateDtoCreateValidator;
        this.certificateSortRequestDtoValidator = certificateSortRequestDtoValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) throws EntityNotFoundException {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCertificateList(SortRequestDto sortRequestDto,
                                                BindingResult bindingResult,
                                                FilterRequestDto filterRequestDto) throws BindException,DaoException {
        certificateSortRequestDtoValidator.validate(sortRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        SortRequest sortRequest = modelMapper.map(sortRequestDto, SortRequest.class);
        FilterRequest filterRequest = modelMapper.map(filterRequestDto, FilterRequest.class);
        List<Certificate> result = certificateService.findAll(sortRequest, filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException, DaoException {
        certificateDtoCreateValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = modelMapper.map(created, CertificateDto.class);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException, DaoException, EntityNotFoundException {
        certificateDtoUpdateValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate updated = certificateService.selectiveUpdate(certificate);
        CertificateDto updatedDto = modelMapper.map(updated, CertificateDto.class);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDto> deleteById(@PathVariable("id") Long id) throws EntityNotFoundException {
        Certificate deleted = certificateService.deleteById(id);
        CertificateDto deletedDto = modelMapper.map(deleted, CertificateDto.class);
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }
}
