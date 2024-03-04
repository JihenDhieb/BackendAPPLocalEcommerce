package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends MongoRepository<Story,String> {
    List<Story> findByPageId(String pageId);

}
