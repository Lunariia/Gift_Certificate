package com.epam.esm;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.impl.CertificateTagDaoImpl;
import com.epam.esm.persistence.dao.impl.TagDaoImpl;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.mapper.CertificateTagRowMapper;
import com.epam.esm.persistence.mapper.TagRowMapper;
import com.epam.esm.service.logic.CertificateTagService;
import com.epam.esm.service.logic.TagService;
import com.epam.esm.service.logic.impl.CertificateTagServiceImpl;
import com.epam.esm.service.logic.impl.TagServiceImpl;
import com.epam.esm.service.validator.CertificateTagValidator;
import com.epam.esm.service.validator.TagValidator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {

        Resource resource = new ClassPathResource("properties/hikari.properties");
        Properties hikariProperties = PropertiesLoaderUtils.loadProperties(resource);
        HikariConfig hikariConfig = new HikariConfig(hikariProperties);

        DataSource dataSource = new HikariDataSource(hikariConfig);;

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RowMapper<Tag> tagRowMapper = new TagRowMapper();
        RowMapper<CertificateTag> certificateTagRowMapper = new CertificateTagRowMapper();

        TagDao tagDao = new TagDaoImpl(namedParameterJdbcTemplate,tagRowMapper);
        CertificateTagDao certificateTagDao = new CertificateTagDaoImpl(namedParameterJdbcTemplate,certificateTagRowMapper);

        CertificateTagService certificateTagService = new CertificateTagServiceImpl(certificateTagDao,new CertificateTagValidator());
        TagService tagService = new TagServiceImpl(tagDao,certificateTagService,new TagValidator());

        Tag tag = tagService.findById(1L);
        System.out.println(tag.getId() + " " + tag.getName());
    }
}
