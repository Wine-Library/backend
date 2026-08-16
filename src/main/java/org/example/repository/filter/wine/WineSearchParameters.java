package org.example.repository.filter.wine;

public record WineSearchParameters(double minPrice,
                                   double maxPrice,
                                   String[] wineTypes,
                                   String[] countriesOfOrigin,
                                   double minPopularityRating,
                                   double maxPopularityRating,
                                   int minYear,
                                   int maxYear) {
}
