package com.epam.esm.service.logic.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.logic.CertificateTagService;
import com.epam.esm.service.logic.TagService;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    public static final long MIN_ID_VALUE = 1L;

    private final TagDao tagDao;
    private final CertificateTagService certificateTagService;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao,
                          CertificateTagService certificateTagService,
                          Validator<Tag> tagValidator) {
        this.tagDao = tagDao;
        this.certificateTagService = certificateTagService;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag findById(Long id) throws EntityNotFoundException {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        return tagDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag does not exists! ID: " + id));
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public Tag create(Tag tag) throws DaoException {
        Preconditions.checkNotNull(tag, "Tag invalid: " + tag);

        if (tag.getId() != null) {
            throw new DaoException("this id now forbidden for new tag! Tag invalid: " + tag);
        }
        if (!tagValidator.isValid(tag)) {
            throw new DaoException("Tag invalid: " + tag);
        }
        return tagDao.create(tag);
    }

    @Override
    @Transactional
    public Set<Tag> createIfNotExist(Set<Tag> tags) throws DaoException {
        Preconditions.checkNotNull(tags, "Invalid tags parameter: " + tags);

        for (Tag tag : tags) {
            if (!tagValidator.isValid(tag)) {
                throw new DaoException("Tag invalid: " + tag);
            }
        }

        Set<Tag> results = new HashSet<>();
        for (Tag tag : tags) {
            String name = tag.getName();
            Optional<Tag> result = tagDao.findByName(name);
            if (result.isPresent()) {
                Tag found = result.get();
                results.add(found);
            } else {
                Tag created = tagDao.create(tag);
                results.add(created);
            }
        }
        return results;
    }

    @Override
    @Transactional
    public Tag deleteById(Long id) throws EntityNotFoundException {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Tag target = tagDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag does not exists! ID: " + id));

        certificateTagService.deleteByTagId(id);
        tagDao.delete(id);

        return target;
    }

    @Override
    public void deleteUnused() {
        tagDao.deleteUnused();
    }
}
