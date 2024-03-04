package com.example.AppEcommerce.Impl;

import com.example.AppEcommerce.Dto.StoryDto;
import com.example.AppEcommerce.Model.Story;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StoryServiceImp {


    String addStory(String id, StoryDto storyDto);

    ResponseEntity<?> addImageToStory(String id, MultipartFile file)throws IOException;



    List<Story> getStoriesByPageId(String pageId);




    Story getStoryById(String id);
}
