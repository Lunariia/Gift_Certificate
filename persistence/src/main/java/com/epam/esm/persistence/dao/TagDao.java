package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Common DAO pattern interface for Tag entity
 */
public interface TagDao {

    /**
     * Find and return Tag by id
     *
     * @param id of Tag to find
     * @return Optional of Tag found or empty if nothing found
     */
    Optional<Tag> findById(Long id);

    /**
     * @param name of Tag to find
     * @return Optional of Tag found or empty if nothing found
     */
    Optional<Tag> findByName(String name);

    /**
     * Find and return all Tags
     *
     * @return List of Tags
     */
    List<Tag> findAll();

    /**
     * Create new Tag
     *
     * @param tag to create
     * @return Tag created using auto generated id
     */
    Tag create(Tag tag);

    /**
     * Delete Tag by id
     *
     * @param id of Tag to delete
     */
    void delete(Long id);

    /**
     * Deletes all Tags which not attached to any certificate
     */
    void deleteNoUsed();
}
