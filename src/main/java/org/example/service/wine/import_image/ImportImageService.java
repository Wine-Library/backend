package org.example.service.wine.import_image;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImportImageService {
    String uploadFile(MultipartFile file) throws IOException;
}
