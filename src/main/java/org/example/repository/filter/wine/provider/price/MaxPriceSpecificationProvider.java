package org.example.repository.filter.wine.provider.price;

import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MaxPriceSpecificationProvider
        implements SpecificationProvider<Wine> {

    private static final String KEY = "maxPrice";
    private static final String PRICE_FIELD = "price";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Wine> getSpecification(String[] params) {
        throw new UnsupportedOperationException(
                "MaxPriceSpecificationProvider requires a numeric parameter"
        );
    }

    @Override
    public Specification<Wine> getSpecification(double param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get(PRICE_FIELD),
                        param
                );
    }
}
