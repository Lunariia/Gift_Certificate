package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.model.FilterRequest;
import com.epam.esm.model.SortRequest;

import java.util.List;
import java.util.Optional;

/**
 * Common DAO pattern interface for Certificate entity
 */
public interface CertificateDao {

    /**
     * Find and return Certificate by id
     *
     * @param id of Certificate to find
     * @return Optional of certificate found or empty if nothing found
     */
    Optional<Certificate> findById(Long id);

    /**
     * Finds and returns a list of certificates (with tags) based on filtering and sorting options
     *
     * @param sortRequest   an object that encapsulates sorting options
     * @param filterRequest an object that encapsulates filtering options
     * @return sorted List of certificates found
     */
    List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest);

    /**
     * Creates new certificate. Certificate tags are not involved in operation
     *
     * @param certificate to create
     * @return certificate created including auto generated id
     */
    Certificate create(Certificate certificate);

    /**
     * Finds and returns all certificates with tags
     *
     * @return List of all Certificates
     */
    List<Certificate> findAll();

    /**
     * Updates existing certificate based on specified id.
     *
     * @param certificate updated version of certificate to save
     * @return certificate updated including auto generated values
     */
    Certificate update(Certificate certificate);

    /**
     * Deletes certificate by specified id.
     *
     * @param id of certificate to delete
     */
    void delete(Long id);
}
