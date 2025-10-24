package com.sn.snuser.repository.specification.post;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class PostTextSpecification implements SpecificationProvider<Post> {
    private static final String FILTER_KEY = "textLike";
    private static final String FILED_NAME = "text";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Post> getSpecification(String[] texts) {
        return ((root, query, cb) ->  cb.like(root.get(FILED_NAME), texts[0]));
    }
}
