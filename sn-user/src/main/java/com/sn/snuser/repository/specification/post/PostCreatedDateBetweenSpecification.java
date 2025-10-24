package com.sn.snuser.repository.specification.post;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostCreatedDateBetweenSpecification implements SpecificationProvider<Post> {
    private static final String FILTER_KEY = "createdAtBetween";
    private static final String FIELD_NAME = "createdAt";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Post> getSpecification(String[] params) {
        return (root, query, cb) -> {
            // params[0] = dateFrom (optional, can be empty string)
            // params[1] = dateTo (optional, can be empty string)

            boolean hasDateFrom = params.length > 0 && params[0] != null && !params[0].isEmpty();
            boolean hasDateTo = params.length > 1 && params[1] != null && !params[1].isEmpty();

            if (hasDateFrom && hasDateTo) {
                // Both dates provided: createdAt BETWEEN dateFrom AND dateTo
                LocalDateTime dateFrom = LocalDateTime.parse(params[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime dateTo = LocalDateTime.parse(params[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return cb.between(root.get(FIELD_NAME), dateFrom, dateTo);
            } else if (hasDateFrom) {
                // Only dateFrom: createdAt >= dateFrom
                LocalDateTime dateFrom = LocalDateTime.parse(params[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return cb.greaterThanOrEqualTo(root.get(FIELD_NAME), dateFrom);
            } else if (hasDateTo) {
                // Only dateTo: createdAt <= dateTo
                LocalDateTime dateTo = LocalDateTime.parse(params[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return cb.lessThanOrEqualTo(root.get(FIELD_NAME), dateTo);
            }

            // No dates provided: return always true (no filter)
            return cb.conjunction();
        };
    }
}