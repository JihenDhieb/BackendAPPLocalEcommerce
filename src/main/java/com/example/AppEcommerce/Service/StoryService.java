package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Dto.MessageResponse;
import com.example.AppEcommerce.Dto.StoryDto;
import com.example.AppEcommerce.Impl.StoryServiceImp;
import com.example.AppEcommerce.Model.File;
import com.example.AppEcommerce.Model.Pages;
import com.example.AppEcommerce.Model.Story;
import com.example.AppEcommerce.Repository.FileRepository;
import com.example.AppEcommerce.Repository.PagesRepository;
import com.example.AppEcommerce.Repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.AppEcommerce.Service.ArticleService.RADIUS_OF_EARTH;

@Service
public class StoryService implements StoryServiceImp {
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private PagesRepository pagesRepository;
    @Autowired
    private FileRepository fileRepository;

    @Override
    public String addStory(String id, StoryDto storyDto){
        Pages page = pagesRepository.findById(id).orElseThrow(null);
        Story story = new Story(storyDto.getNamearticle(),storyDto.getDescription(),storyDto.getNbarticle(),storyDto
                .getPrix(),page);
        Story story1 = storyRepository.save(story);
        return story1.getId();
    }

   @Override
   public ResponseEntity<?> addImageToStory(String id, MultipartFile file)throws IOException {
       File image = new File(file.getOriginalFilename(), file.getContentType(), file.getBytes());
       Optional<Story> story =storyRepository.findById(id);
       fileRepository.save(image);
      story.get().setImage(image);
       storyRepository.save(story.get());
       return ResponseEntity.ok(new MessageResponse("images registred succssefuly to story   "));
   }
    @Override
    public List<Story> getStoriesByPageId(String pageId) {
        return storyRepository.findByPageId(pageId);
    }
    @Override
    public Story getStoryById(String id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }
    public double calculate(double lat1, double long1,
                            double lat2, double long2){
        double deltaLat = Math.toRadians(lat2 - lat1 );
        double deltaLon = Math.toRadians(long2 - long1 );
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_OF_EARTH * c;
    }


    public List<Story> StoriessLess7(double lat1, double long1){
        List<Story> stories = storyRepository.findAll();
        List<Story> stories1 = new ArrayList<>();
        stories.forEach(story ->{
            if(calculate(lat1,long1, story.getPage().getLatitude(), story.getPage().getLongitude()) < 7){
                stories1.add(story);
            }
        } );
        return stories1;
    }
}
