package org.example.repository.filter.wine;

import lombok.RequiredArgsConstructor;
import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationBuilder;
import org.example.repository.filter.general.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WineSpecificationBuilder implements SpecificationBuilder<Wine> {
    private static final String WINE_TYPE_KEY = "wineType";
    private static final String COUNTRY_OF_ORIGIN_KEY = "countryOfOrigin";
    private static final String MIN_PRICE_KEY = "minPrice";
    private static final String MAX_PRICE_KEY = "maxPrice";
    private static final String MIN_POPULARITY_RATING_KEY = "minPopularityRating";
    private static final String MAX_POPULARITY_RATING_KEY = "maxPopularityRating";

    private final SpecificationProviderManager<Wine> wineSpecificationProviderManager;

    @Override
    public Specification<Wine> buildSpecification(WineSearchParameters searchParameters) {
        Specification<Wine> specification = Specification.where(null);
        if (searchParameters.wineTypes() != null && searchParameters.wineTypes().length > 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(WINE_TYPE_KEY)
                            .getSpecification(searchParameters.wineTypes()));
        }
        if (searchParameters.countriesOfOrigin() != null && searchParameters.countriesOfOrigin().length > 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(COUNTRY_OF_ORIGIN_KEY)
                            .getSpecification(searchParameters.countriesOfOrigin()));
        }
        if (searchParameters.minPrice() != 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(MIN_PRICE_KEY)
                            .getSpecification(searchParameters.minPrice()));
        }
        if (searchParameters.maxPrice() != 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(MAX_PRICE_KEY)
                            .getSpecification(searchParameters.maxPrice()));
        }
        if (searchParameters.minPopularityRating() != 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(MIN_POPULARITY_RATING_KEY)
                            .getSpecification(searchParameters.minPopularityRating()));
        }
        if (searchParameters.maxPopularityRating() != 0) {
            specification = specification.and(
                    wineSpecificationProviderManager
                            .getSpecificationProvider(MAX_POPULARITY_RATING_KEY)
                            .getSpecification(searchParameters.maxPopularityRating()));
        }
        return specification;
    }
}