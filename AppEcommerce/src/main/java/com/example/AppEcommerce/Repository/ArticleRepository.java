package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.File;
import com.example.AppEcommerce.Model.Pages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article,String> {

   List<Article> findByPage(Pages page);

}
