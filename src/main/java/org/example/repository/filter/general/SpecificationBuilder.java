package org.example.repository.filter.general;

import org.example.repository.filter.wine.WineSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> buildSpecification(WineSearchParameters searchParameters);
}
