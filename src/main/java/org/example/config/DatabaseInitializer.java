package org.example.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.repository.WineRepository;
import org.example.service.wine.import_csv.ImportCsvService;
import org.example.model.Wine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final ImportCsvService importCsvService;
    private final WineRepository wineRepository;

    @Override
    public void run(String... args) {
        if (wineRepository.count() == 0) {
            List<Wine> wines = importCsvService.importFile("dataset.csv");
            wineRepository.saveAll(wines);
            System.out.println("Successfully imported " + wines.size() + " wines");
        }
    }
}
