package org.example.repository.filter.wine.provider.popularity_rating;

import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MaxPopularityRatingSpecificationProvider
        implements SpecificationProvider<Wine> {

    private static final String KEY = "maxPopularityRating";
    private static final String POPULARITY_RATING_FIELD = "popularityRating";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Wine> getSpecification(String[] params) {
        throw new UnsupportedOperationException(
                "MaxPopularityRatingSpecificationProvider requires a numeric parameter"
        );
    }

    @Override
    public Specification<Wine> getSpecification(double param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get(POPULARITY_RATING_FIELD),
                        param
                );
    }

    @Override
    public Specification<Wine> getSpecification(int param) {
        throw new UnsupportedOperationException(
                "MaxPopularityRatingSpecificationProvider requires a numeric parameter"
        );
    }
}
