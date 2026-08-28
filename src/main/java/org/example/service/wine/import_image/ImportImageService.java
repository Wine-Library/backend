package org.example.service.wine.import_image;

import org.springframework.web.multipart.MultipartFile;

public interface ImportImageService {
    String uploadFile(Long wineId, MultipartFile file) throws Exception;

    void deleteFile(String objectKey);
}
