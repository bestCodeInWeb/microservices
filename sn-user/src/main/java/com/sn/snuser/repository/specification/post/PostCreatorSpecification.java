package com.sn.snuser.repository.specification.post;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class PostCreatorSpecification implements SpecificationProvider<Post> {
    private static final String FILTER_KEY = "creatorId";
    private static final String FIELD_NAME = "creator";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Post> getSpecification(String[] params) {
        return (root, query, cb) -> cb.equal(root.get(FIELD_NAME).get("id"), params[0]);
    }
}