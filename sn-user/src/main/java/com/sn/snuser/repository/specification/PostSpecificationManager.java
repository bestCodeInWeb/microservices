package com.sn.snuser.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
public class PostSpecificationManager<Post> implements SpecificationManager<Post> {
    private final Map<String, SpecificationProvider<Post>> providersMap;

    public PostSpecificationManager(List<SpecificationProvider<Post>> providers) {
        this.providersMap = providers.stream().collect(Collectors.toMap(SpecificationProvider::getFilterKey, identity()));
    }

    @Override
    public Specification<Post> get(String filterKey, String[] params) {
        if (!providersMap.containsKey(filterKey)) {
            throw new RuntimeException("Key " + filterKey + " is not supported for data filtering");
        }
        return providersMap.get(filterKey).getSpecification(params);
    }
}
