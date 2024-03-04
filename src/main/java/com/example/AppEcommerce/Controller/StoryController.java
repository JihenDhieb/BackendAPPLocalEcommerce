package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.StoryDto;
import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.Story;
import com.example.AppEcommerce.Model.User;
import com.example.AppEcommerce.Service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/story")
public class StoryController {
    @Autowired
    private StoryService storyService;
    @PostMapping(value ="/addStory/{id}")
        public String addStory(@PathVariable String id , @RequestBody StoryDto storyDto) {
            return storyService.addStory(id,storyDto);
    }
    @PostMapping(value ="/addImage/{id}")
    public ResponseEntity<?> addImageToStory(@RequestPart(name = "image", required = false) MultipartFile file,@PathVariable String id) throws IOException {
        return storyService.addImageToStory(id, file);
    }
    @GetMapping(value = "/getstory/{id}")
    public List<Story> getStoriesByPageId(@PathVariable String id) {
        return storyService.getStoriesByPageId(id);
    }
    @GetMapping(value = "/getListstory/{lat}/{lon}")
    public List<Story> StoriessLess7(@PathVariable double lat, @PathVariable double lon) {
        return storyService.StoriessLess7(lat,lon);
    }
    @GetMapping("/{id}")
    public Story getStoryById(@PathVariable("id") String id) {
        return storyService.getStoryById(id);

    }
}
