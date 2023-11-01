package com.company.controller;

import com.company.model.entity.Stories;
import com.company.service.StoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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


    @GetMapping
    public List<Stories> getAllStories() {
        try {
            return storiesService.getAllStories();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getStoryById(@PathVariable int id) {
        try {
            Stories story = storiesService.getStoryById(id);
            return ResponseEntity.ok(story);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the story");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStory(@PathVariable int id) {
        try {
            storiesService.deleteStory(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the story");
        }
    }


}