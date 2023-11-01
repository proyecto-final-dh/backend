package com.company.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.company.model.dto.ImageWithTitle;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BucketImageService {

    private S3Client s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.access_key_id}")
    private String accessKey;
    @Value("${aws.secret_access_key}")
    private String secretKey;

    private String endpointUrl = "https://s3imagesgittravel.s3.sa-east-1.amazonaws.com";

    @PostConstruct
    private void initializeAmazon() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(this.accessKey, this.secretKey);
        this.s3client = S3Client.builder()
                .region(Region.SA_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromFile(file.toPath()));
    }

    public List<String> uploadFile(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        String fileUrl = "";
        for (MultipartFile multipartFile : files) {
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
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

    public List<ImageWithTitle> uploadFileWithTitle(MultipartFile[] files) {
        List<ImageWithTitle> images = new ArrayList<>();
        String fileUrl = "";
        for (MultipartFile multipartFile : files) {
            try {
                ImageWithTitle NewImage = new ImageWithTitle();
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                fileUrl = endpointUrl + "/" + fileName;
                NewImage.setUrl(fileUrl);
                NewImage.setTitle(multipartFile.getOriginalFilename());
                images.add(NewImage);
                uploadFileTos3bucket(fileName, file);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public ImageWithTitle uploadMainImage(MultipartFile mainFile) {
        ImageWithTitle image = new ImageWithTitle();
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(mainFile);
            String fileName = generateFileName(mainFile);
            fileUrl = endpointUrl + "/" + fileName;
            image.setUrl(fileUrl);
            image.setTitle(mainFile.getOriginalFilename());
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
