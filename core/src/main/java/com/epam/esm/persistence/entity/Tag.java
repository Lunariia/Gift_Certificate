package com.epam.esm.persistence.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public final class Tag {

    @NotNull(message = "Id cannot be null")
    @Size(min = 1, message = "Id should be more than zero")
    private final Long id;

    @NotNull(message = "Name cannot be null")
    @Size(max = 50, message= "Name must be less then 200 characters")
    @NotEmpty(message = "Name cannot be empty")
    private final String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id.equals(tag.id) &&
                name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String name;

        public Builder() {
        }

        private Builder(Tag tag) {
            id = tag.id;
            name = tag.name;
        }

        public static Builder from(Tag tag) {
            return new Builder(tag);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Tag build() {
            return new Tag(
                    id,
                    name
            );
        }
    }

}
