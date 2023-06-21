package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Model.Pages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PagesRepository extends MongoRepository<Pages,String> {
    List<Pages> findByActivity(Activity activity);


    Pages findByTitle(String pageTitle);
}
