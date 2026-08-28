package org.example.service.wine.import_csv;

import org.example.model.Wine;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportCsvService {
    List<Wine> importFile(String filename);
}
