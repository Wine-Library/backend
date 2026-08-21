package org.example.service.wine.import_csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.FileUploadException;
import org.example.model.Wine;
import org.example.service.wine.import_image.ImportImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImportCsvServiceImpl implements ImportCsvService {

    private final ImportImageService importImageService;

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

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                Wine wine = new Wine();
                String wineName = data[1].replace("\"", "").trim();

                String imageFileName = wineName.replace(" ", "_").replace("'", "") + ".jpg";
                ClassPathResource imageResource = new ClassPathResource("dataset_images/" + imageFileName);

                String imageUrl = "default.jpg";

                if (imageResource.exists()) {
                    MultipartFile multipartFile = createMultipartFile(imageResource, imageFileName);
                    imageUrl = importImageService.uploadFile(multipartFile);
                }

                wine.setProductImage(imageUrl)
                        .setWineName(wineName)
                        .setPrice(new BigDecimal(data[2].replace("\"", "").replace(",", ".").trim()))
                        .setCountryOfOrigin(data[3].replace("\"", "").trim())
                        .setWineType(data[4].replace("\"", "").trim())
                        .setPopularityRating(new BigDecimal(data[5].replace("\"", "").replace(",", ".").trim()))
                        .setOccasions(List.of(data[6].replace("\"", "").trim()))
                        .setYear(Integer.parseInt(data[7].replace("\"", "").trim()));

                wines.add(wine);
            }

            return wines;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadException("Error while importing file from resources: " + filename);
        }
    }

    private MultipartFile createMultipartFile(ClassPathResource resource, String filename) {
        return new MultipartFile() {
            @Override
            public String getName() { return filename; }

            @Override
            public String getOriginalFilename() { return filename; }

            @Override
            public String getContentType() { return "image/jpeg"; }

            @Override
            public boolean isEmpty() { return false; }

            @Override
            public long getSize() {
                try {
                    return resource.contentLength();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public byte[] getBytes() throws IOException {
                return resource.getInputStream().readAllBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.copy(resource.getInputStream(), dest.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        };
    }
}
