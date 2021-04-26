package com.epam.esm.model;

public class Sort {

    private final String field;
    private final SortOrder direction;

    public Sort(String field, SortOrder direction) {
        this.field = field;
        this.direction = direction;
    }

    public static Sort asc(String field) {
        return new Sort(field, SortOrder.ASC);
    }

    public static Sort desc(String field) {
        return new Sort(field, SortOrder.DESC);
    }

    public String getField() {
        return field;
    }

    public SortOrder getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return field.equals(sort.field) &&
                direction == sort.direction;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "field='" + field + '\'' +
                ", direction=" + direction +
                '}';
    }

    public enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        private final String keyword;

        SortOrder(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }

    }
}
