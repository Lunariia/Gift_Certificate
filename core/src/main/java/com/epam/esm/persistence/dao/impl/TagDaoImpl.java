package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String SQL_FIND_BY_NAME = "\n" +
            "SELECT tag.id AS tag_id, tag.name AS tag_name \n" +
            "FROM tag WHERE tag.name = :tagName ";

    private static final String SQL_FIND_BY_ID = "\n" +
            "SELECT tag.id AS tag_id, tag.name AS tag_name \n" +
            "FROM tag WHERE tag.id = :tagId";

    private static final String SQL_FIND_ALL = "\n" +
            "SELECT tag.id AS tag_id, tag.name AS tag_name \n" +
            "FROM tag \n";

    private static final String SQL_INSERT = "INSERT INTO tag (name) VALUES (:name)";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM tag WHERE id = :id ";

    private static final String SQL_DELETE_UNUSED = "DELETE FROM tag \n" +
            "WHERE id NOT IN (SELECT DISTINCT tag_id FROM certificate_tag); \n";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    public static final long MIN_ID_VALUE = 1L;

    @Autowired
    public TagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, RowMapper<Tag> tagRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        Preconditions.checkNotNull(tagId, "Invalid ID parameter: " + tagId);
        Preconditions.checkArgument(tagId >= MIN_ID_VALUE, "Invalid ID parameter: " + tagId);

        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagId", tagId);
        List<Tag> results = namedParameterJdbcTemplate.query(SQL_FIND_BY_ID, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tagName", tagName);
        List<Tag> results = namedParameterJdbcTemplate.query(SQL_FIND_BY_NAME, namedParameters, tagRowMapper);
        return results.stream().findFirst();
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, tagRowMapper);
    }

    @Override
    public Tag create(Tag tag) {
        Preconditions.checkNotNull(tag, "Tag invalid: " + tag);

        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, sqlParameterSource, keyHolder, new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        return Tag.Builder
                .from(tag)
                .setId(id)
                .build();
    }

    @Override
    public void delete(Long id) {
        Preconditions.checkNotNull(id, "Invalid ID parameter: " + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_ID, namedParameters);
    }

    @Override
    public void deleteUnused() {
        Map<String, Object> namedParameters = Collections.emptyMap();
        namedParameterJdbcTemplate.update(SQL_DELETE_UNUSED, namedParameters);
    }
}
