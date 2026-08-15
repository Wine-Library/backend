package org.example.service.wine;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImportFileService {
    String uploadFile(MultipartFile file) throws IOException;
}
