package mk.ukim.finki.wp.kol2023.g1.service;


import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class FieldFilterSpecification {

    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V> Specification<T> filterEqualsV(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V extends Comparable<V>> Specification<T> lassThan(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(fieldToPath(field, (Root<V>) root), value);
    }

    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, Long value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T> Specification<T> filterContainsText(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(fieldToPath(field, (Root<String>) root)),
                "%" + value.toLowerCase() + "%"
        );
    }

    private static <T> Path<T> fieldToPath(String field, Root<T> root) {
        String[] parts = field.split("\\.");
        Path<T> res = root;
        for (String p : parts) {
            res = res.get(p);
        }
        return res;
    }
}

