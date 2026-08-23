package org.example.service.wine.import_image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.example.exception.EmptyFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class ImportImageServiceImpl implements ImportImageService {

    private final S3Client s3Client;

    @Value("${cloud.r2.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(Long wineId, MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new EmptyFileException("Cannot upload an empty file for wine " + wineId);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (InputStream is = file.getInputStream()) {
            Thumbnails.of(is)
                    .size(800, 800)
                    .outputFormat("webp")
                    .toOutputStream(os);
        }
        byte[] webpBytes = os.toByteArray();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(webpBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String sha256 = hexString.toString();

        String key = String.format("wines/%d/%s.webp", wineId, sha256);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/webp")
                .cacheControl("public, max-age=31536000, immutable")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(webpBytes));

        return key;
    }

    @Override
    public void deleteFile(String objectKey) {
        if (objectKey == null || objectKey.trim().isEmpty() || objectKey.equals("pending")) {
            return;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            System.err.println("Failed to delete file from R2 "
                    + objectKey + ". Reason: " + e.getMessage());
        }
    }
}
