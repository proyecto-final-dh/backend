package com.company.constants;

import java.util.List;

public class Constants {
    public static final String PET_IMAGES_FOLDER = "petImages/";
    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("image/jpeg", "image/png", "image/jpg");
    public static final int MAXIMUM_IMAGES = 5;

    // ERROR MESSAGES

    // IMAGE ERRORS
    public static final String INVALID_IMAGE_EXTENSION = "Only images are allowed";
    public static final String MAXIMUM_IMAGES_EXCEEDED = "No more than 5 images can be uploaded";
    public static final String MAXIMUM_IMAGE_SIZE_EXCEEDED = "No more than 1MB per image is allowed";
    public static final String EMPTY_IMAGE = "Images can not be empty";

    // PET ERRORS
    public static final String PET_NAME_REQUIRED = "Pet name is required";
    public static final String PET_GENDER_REQUIRED = "Pet gender is required";
    public static final String WRONG_PET_GENDER = "Pet gender must be valid";
    public static final String PET_SIZE_REQUIRED = "Pet size is required";
    public static final String WRONG_PET_SIZE = "Pet size must be valid";
    public static final String PET_OWNER_REQUIRED = "Pet owner is required and needs to be greater than 0";
    public static final String PET_BREED_REQUIRED = "Pet breed is required and needs to be greater than 0";
    public static final String PET_DESCRIPTION_REQUIRED = "Pet description is required";
    public static final String PET_AGE_MUST_BE_VALID = "Pet age must be equal or grater than 0";
    public static final String PET_UPDATE_UNAUTHORIZED= "You are not authorized to update this pet";
    public static final String WRONG_PET_UPDATE_STATUS = "It is not possible to update pets that have been adopted";

    // NOT FOUND MESSAGES
    public static final String BREED_NOT_FOUND = "Breed not found";
    public static final String OWNER_NOT_FOUND = "Owner not found";
    public static final String LOCATION_NOT_FOUND = "Location not found";
    public static final String PET_NOT_FOUND = "Pet not found";
    public static final String USER_NOT_FOUND = "User not found";


    // SUCCESS MESSAGES

    // CREATION
    public static final String PET_CREATED = "Pet created successfully";

    // UPDATE
    public static final String PET_UPDATED = "Pet updated successfully";

}
