package org.example.repository.filter.wine.provider.year;

import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MinYearSpecificationProvider
        implements SpecificationProvider<Wine> {

    private static final String KEY = "minYear";
    private static final String YEAR_FIELD = "year";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Wine> getSpecification(String[] params) {
        throw new UnsupportedOperationException(
                "MinYearSpecificationProvider requires an integer parameter"
        );
    }

    @Override
    public Specification<Wine> getSpecification(int param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get(YEAR_FIELD),
                        param
                );
    }

    @Override
    public Specification<Wine> getSpecification(double param) {
        throw new UnsupportedOperationException(
                "MinYearSpecificationProvider requires an integer parameter"
        );
    }
}
