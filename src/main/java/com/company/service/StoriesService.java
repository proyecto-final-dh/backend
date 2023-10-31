package com.company.service;

import com.company.model.entity.Stories;
import com.company.repository.IStoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoriesService implements IStoriesService {
    private final IStoriesRepository storiesRepository;

    @Autowired
    public StoriesService(IStoriesRepository storiesRepository) {
        this.storiesRepository = storiesRepository;
    }

    @Override
    public Stories createStories(Stories stories) {
        System.out.println(stories);
        if (stories.getDate1() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Date is required");
        }



        return storiesRepository.save(stories);
    }
}
