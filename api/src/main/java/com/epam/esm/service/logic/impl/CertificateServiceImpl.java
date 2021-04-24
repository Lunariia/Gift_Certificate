package com.epam.esm.service.logic.impl;

import com.epam.esm.persistence.dao.CertificateDao;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.model.FilterRequest;
import com.epam.esm.model.SortRequest;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.logic.CertificateService;
import com.epam.esm.service.logic.CertificateTagService;
import com.epam.esm.service.logic.TagService;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.sortRequest.CertificateSortRequestValidator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final long MIN_ID_VALUE = 1L;

    private final CertificateDao certificateDao;
    private final TagService tagService;
    private final CertificateTagService certificateTagService;
    private final Validator<Certificate> certificateValidator;
    private final CertificateSortRequestValidator certificateSortRequestValidator;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  TagService tagService,
                                  CertificateTagService certificateTagService,
                                  Validator<Certificate> certificateValidator,
                                  CertificateSortRequestValidator certificateSortRequestValidator) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.certificateTagService = certificateTagService;
        this.certificateValidator = certificateValidator;
        this.certificateSortRequestValidator = certificateSortRequestValidator;
    }

    @Override
    public Certificate findById(Long id) throws EntityNotFoundException {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Optional<Certificate> certificate = certificateDao.findById(id);
        return certificate
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) throws ServiceException {
        Preconditions.checkNotNull(sortRequest, "Invalid SortRequest parameter: " + sortRequest);
        Preconditions.checkNotNull(filterRequest, "Invalid FilterRequest parameter: " + filterRequest);

        if (!certificateSortRequestValidator.isValid(sortRequest)) {
            throw new ServiceException("Invalid SortRequest parameter: " + sortRequest);
        }
        return certificateDao.findAll(sortRequest, filterRequest);
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) throws ServiceException {
        Preconditions.checkNotNull(certificate, "Certificate invalid: " + certificate);

        Long id = certificate.getId();
        if (id != null) {
            throw new ServiceException("Specifying ids is now allowed for new certificates!");
        }

        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(certificate)
                .setCreateDate(now)
                .setLastUpdateDate(now)
                .build();

        if (!certificateValidator.isValid(dated)) {
            throw new ServiceException("Certificate invalid: " + dated);
        }
        Certificate savedCertificate = certificateDao.create(dated);

        Set<Tag> tags = certificate.getTags();
        if (!tags.isEmpty()) {
            Set<Tag> savedTags = tagService.createIfNotExist(tags);
            savedCertificate = Certificate.Builder
                    .from(savedCertificate)
                    .setTags(savedTags)
                    .build();
            Long generatedId = savedCertificate.getId();
            certificateTagService.addTagSet(generatedId, savedTags);
        }
        return savedCertificate;
    }

    @Override
    @Transactional
    public Certificate selectiveUpdate(Certificate certificate) throws EntityNotFoundException, ServiceException {
        Preconditions.checkNotNull(certificate, "Certificate invalid: " + certificate);

        Long id = certificate.getId();
        if (id == null || id < MIN_ID_VALUE) {
            throw new ServiceException("Unable to update certificate! Id is not specified or invalid: " + id);
        }

        Certificate target = certificateDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));

        String sourceName = certificate.getName();
        String sourceDescription = certificate.getDescription();
        BigDecimal sourcePrice = certificate.getPrice();
        Integer sourceDuration = certificate.getDuration();
        Set<Tag> sourceTags = certificate.getTags();

        String name = sourceName == null ? target.getName() : sourceName;
        String description = sourceDescription == null ? target.getDescription() : sourceDescription;
        BigDecimal price = sourcePrice == null ? target.getPrice() : sourcePrice;
        Integer duration = sourceDuration == null ? target.getDuration() : sourceDuration;
        Set<Tag> tags = sourceTags == null ? target.getTags() : sourceTags;

        Certificate resultCertificate = Certificate.Builder
                .from(target)
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setDuration(duration)
                .setTags(tags)
                .build();
        return this.update(resultCertificate);
    }

    private Certificate update(Certificate resultCertificate) throws ServiceException {
        if (!certificateValidator.isValid(resultCertificate)) {
            throw new ServiceException("Certificate invalid: " + resultCertificate);
        }
        LocalDateTime now = LocalDateTime.now();
        Certificate dated = Certificate.Builder
                .from(resultCertificate)
                .setLastUpdateDate(now)
                .build();
        Certificate updated = certificateDao.update(dated);

        Set<Tag> tags = resultCertificate.getTags();
        Set<Tag> savedTags = tagService.createIfNotExist(tags);
        Certificate updatedWithTags = Certificate.Builder
                .from(updated)
                .setTags(savedTags)
                .build();

        Long id = updatedWithTags.getId();
        certificateTagService.updateTagSet(id, savedTags);

        return updatedWithTags;
    }

    @Override
    @Transactional
    public Certificate deleteById(Long id) throws EntityNotFoundException {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Certificate target = certificateDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate does not exists! ID: " + id));
        certificateTagService.deleteByCertificateId(id);
        certificateDao.delete(id);

        return target;
    }
}
