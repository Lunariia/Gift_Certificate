package com.epam.esm.persistence.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CertificateTag {

    @NotNull(message = "Id cannot be null")
    @Size(min = 1, message = "Id should be more than zero")
    private final Long id;

    @NotNull(message = "CertificateId cannot be null")
    @Size(min = 1, message = "CertificateId should be more than zero")
    private final Long certificateId;

    @NotNull(message = "TagId cannot be null")
    @Size(min = 1, message = "TagId should be more than zero")
    private final Long tagId;

    public CertificateTag(Long id, Long certificateId, Long tagId) {
        this.id = id;
        this.certificateId = certificateId;
        this.tagId = tagId;
    }

    public Long getId() {
        return id;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public Long getTagId() {
        return tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateTag that = (CertificateTag) o;
        return id.equals(that.id) &&
                certificateId.equals(that.certificateId) &&
                tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (certificateId != null ? certificateId.hashCode() : 0);
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", certificateId=" + certificateId +
                ", tagId=" + tagId +
                '}';
    }

    public static final class Builder {

        private Long id;
        private Long certificateId;
        private Long tagId;

        public Builder() {
        }

        private Builder(CertificateTag certificateTag) {
            id = certificateTag.id;
            certificateId = certificateTag.certificateId;
            tagId = certificateTag.tagId;
        }

        public static Builder from(CertificateTag certificateTag) {
            return new Builder(certificateTag);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setCertificateId(Long certificateId) {
            this.certificateId = certificateId;
            return this;
        }

        public Builder setTagId(Long tagId) {
            this.tagId = tagId;
            return this;
        }

        public CertificateTag build() {
            return new CertificateTag(
                    id,
                    certificateId,
                    tagId
            );
        }

    }
}
