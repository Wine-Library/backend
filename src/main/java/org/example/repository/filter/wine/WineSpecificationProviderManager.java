package org.example.repository.filter.wine;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.exception.SpecificationNotFoundException;
import org.example.model.Wine;
import org.example.repository.filter.general.SpecificationProvider;
import org.example.repository.filter.general.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WineSpecificationProviderManager implements SpecificationProviderManager<Wine> {
    private final List<SpecificationProvider<Wine>> specificationProviders;

    @Override
    public SpecificationProvider<Wine> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(spec -> spec.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationNotFoundException(
                        "No specification provider found for " + key));
    }
}
