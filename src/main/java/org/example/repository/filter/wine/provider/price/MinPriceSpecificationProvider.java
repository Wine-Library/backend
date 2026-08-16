package org.example.repository.filter.wine.provider.price;

import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MinPriceSpecificationProvider
        implements SpecificationProvider<Wine> {

    private static final String KEY = "minPrice";
    private static final String PRICE_FIELD = "price";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Wine> getSpecification(String[] params) {
        throw new UnsupportedOperationException(
                "MinPriceSpecificationProvider requires a numeric parameter"
        );
    }

    @Override
    public Specification<Wine> getSpecification(double param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get(PRICE_FIELD),
                        param
                );
    }

    @Override
    public Specification<Wine> getSpecification(int param) {
        throw new UnsupportedOperationException(
                "MinPriceSpecificationProvider requires a numeric parameter"
        );
    }
}
