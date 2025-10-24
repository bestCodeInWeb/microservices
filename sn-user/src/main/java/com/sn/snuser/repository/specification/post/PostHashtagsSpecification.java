package com.sn.snuser.repository.specification.post;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.specification.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PostHashtagsSpecification implements SpecificationProvider<Post> {
    private static final String FILTER_KEY = "hashtagsIn";
    private static final String FIELD_NAME = "hashtags";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Post> getSpecification(String[] hashtags) {
        return (root, query, cb) -> {
            Join<Object, Object> hashtagsJoin = root.join(FIELD_NAME);
            return hashtagsJoin.in((Object[]) hashtags);
        };
    }
}