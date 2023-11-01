package com.company.controller;//package com.grupo4.hotels.controller;

import com.company.service.BucketImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class BucketImagesController {

    private BucketImageService amazonClient;

    @Autowired
    BucketImagesController(BucketImageService amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("image") MultipartFile[] files) {
        return ResponseEntity.ok(this.amazonClient.uploadFile(files));
    }

}