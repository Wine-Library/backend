package org.example.repository.filter.wine.provider;

import java.util.Arrays;
import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CountryOfOriginSpecificationProvider implements SpecificationProvider<Wine> {
    private static final String KEY = "countryOfOrigin";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Wine> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder)
                -> root.get(KEY).in(Arrays.stream(params).toArray()));
    }

    @Override
    public Specification<Wine> getSpecification(double param) {
        throw new UnsupportedOperationException(
                "CountryOfOriginSpecificationProvider requires a string parameter"
        );
    }
}
