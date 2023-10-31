package com.company.controller;

import com.company.model.entity.Stories;
import com.company.service.StoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/stories")
public class StoriesController {
    private final StoriesService storiesService;

    @Autowired
    public StoriesController(StoriesService storiesService) {
        this.storiesService = storiesService;
    }

    @PostMapping
    public ResponseEntity<Object> createStory(@RequestBody Stories story) {
        try {
            Stories newStory = storiesService.createStories(story);
            return new ResponseEntity<>(newStory, HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating the story");
        }
    }
}