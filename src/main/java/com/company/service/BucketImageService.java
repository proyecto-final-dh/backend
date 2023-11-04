package com.company.service;

import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.company.constants.Constants.ALLOWED_IMAGE_EXTENSIONS;
import static com.company.constants.Constants.EMPTY_IMAGE;
import static com.company.constants.Constants.INVALID_IMAGE_EXTENSION;
import static com.company.constants.Constants.MAXIMUM_IMAGES_EXCEEDED;
import static com.company.constants.Constants.MAXIMUM_IMAGE_SIZE_EXCEEDED;
import static com.company.constants.Constants.PET_IMAGES_FOLDER;

@Service
public class BucketImageService {

    private S3Client s3client;
    String bucketName = System.getenv("BUCKET_NAME");
    String accessKey = System.getenv("S3_ACCESS_KEY_ID");
        String secretKey = System.getenv("S3_SECRET_ACCESS_KEY");
    private String endpointUrl = String.format("https://%s.s3.us-east-1.amazonaws.com", bucketName);

    @PostConstruct
    private void initializeAmazon() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(this.accessKey, this.secretKey);
        this.s3client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromFile(file.toPath()));
    }

    public List<String> uploadFile(MultipartFile[] files) {
        validateFiles(files);

        List<String> urls = new ArrayList<>();
        String fileUrl = "";

        for (MultipartFile multipartFile : files) {
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = PET_IMAGES_FOLDER + generateFileName(multipartFile);
                fileUrl = endpointUrl + "/" + fileName;
                urls.add(fileUrl);
                uploadFileTos3bucket(fileName, file);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private void validateFiles(MultipartFile[] files) {
        if(files.length < 1) throw new ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, EMPTY_IMAGE);

        if(files.length > 5) throw new ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, MAXIMUM_IMAGES_EXCEEDED);

        if(!validateImageExtension(files)) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, INVALID_IMAGE_EXTENSION);
        }

        if(!validateImageSize(files)) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, MAXIMUM_IMAGE_SIZE_EXCEEDED);
        }
    }

    private Boolean validateImageExtension(MultipartFile[] files) {
        for (MultipartFile f : files) {
            if (!ALLOWED_IMAGE_EXTENSIONS.contains(f.getContentType())) {
                return false;
            }
        }
        return true;
    }

    private Boolean validateImageSize(MultipartFile[] files) {
        for (MultipartFile f : files) {
            if (f.getSize() > 1000000) {
                return false;
            }
        }
        return true;
    }
}
