package com.company.constants;

import java.util.List;

public class Constants {
    public static final String PET_IMAGES_FOLDER = "petImages/";
    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("image/jpeg", "image/png", "image/jpg");

    // ERROR MESSAGES
    public static final String INVALID_IMAGE_EXTENSION = "Only images are allowed";
    public static final String MAXIMUM_IMAGES_EXCEEDED = "No more than 10 images can be uploaded";
    public static final String MAXIMUM_IMAGE_SIZE_EXCEEDED = "No more than 1MB per image is allowed";
    public static final String EMPTY_IMAGE = "Images can not be empty";
}
