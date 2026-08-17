package org.example.service.wine.import_csv;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.exception.FileUploadException;
import org.example.model.Wine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportCsvServiceImpl implements ImportCsvService {

    @Override
    public List<Wine> importFile(String filename) {
        List<Wine> wines = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource(filename);

        if (!resource.exists()) {
            throw new RuntimeException("File not found in resources: " + filename);
        }

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(";", -1);

                Wine wine = new Wine();
                String imageUrl = (data.length > 0 && !data[0].trim().isEmpty()) ? data[0] : "default.png";
                wine.setProductImage(imageUrl);

                wine.setWineName(data[1])
                        .setPrice(new BigDecimal(
                                data[2].replace(",", ".")))
                        .setCountryOfOrigin(data[3])
                        .setWineType(data[4])
                        .setPopularityRating(new BigDecimal(
                                data[5].replace(",", ".")))
                        .setOccasions(List.of(data[6]))
                        .setYear(Integer.parseInt(data[7]));

                wines.add(wine);
            }

            return wines;
        } catch (Exception e) {
            throw new FileUploadException("Error while importing file from resources: "
                    + filename);
        }
    }
}
