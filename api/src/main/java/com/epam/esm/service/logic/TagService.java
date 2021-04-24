package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Set;

/**
 * Business logic interface for Tags
 */
public interface TagService {

    /**
     * Finds tag by specified id
     *
     * @param id of tag to find
     * @return Tag found
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when found nothing
     */
    Tag findById(Long id) throws EntityNotFoundException;

    /**
     * Finds and returns list of all Tags
     *
     * @return List of all Tags
     */
    List<Tag> findAll();

    /**
     * Creates new Tag
     *
     * @param tag to create
     * @return Tag created in effect
     * @throws NullPointerException when tag parameter is null
     * @throws ServiceException     when tag id specified, or tag invalid
     */
    Tag create(Tag tag) throws ServiceException;

    /**
     * Manages several tags to find each by name and create new one if found nothing
     * This method returns all tags with id, either found or created
     *
     * @param tags to manage
     * @return Set of all found and created tags
     * @throws NullPointerException when tags parameter is null
     * @throws ServiceException     when one of tags invalid
     */
    Set<Tag> createIfNotExist(Set<Tag> tags) throws ServiceException;

    /**
     * Deletes tag by specified id
     *
     * @param id of tag being deleted
     * @return Tag deleted in effect
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when deleting tag not found
     */
    Tag deleteById(Long id) throws EntityNotFoundException;

    /**
     * Deletes all Tags not attached to any certificate
     */
    void deleteUnused();
}
