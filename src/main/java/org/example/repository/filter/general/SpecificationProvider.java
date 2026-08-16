package org.example.repository.filter.general;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(String[] params);

    Specification<T> getSpecification(double param);
}
